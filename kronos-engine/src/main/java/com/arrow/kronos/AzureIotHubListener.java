package com.arrow.kronos;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.AzureAccount.TelemetrySync;
import com.arrow.kronos.service.AzureAccountService;
import com.arrow.kronos.util.RetriableTask;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventData.SystemProperties;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.PartitionReceiveHandler;
import com.microsoft.azure.eventhubs.PartitionReceiver;

public class AzureIotHubListener extends TelemetryLoopbackAbstract {

	@Value("${azure.receiver.timeout:5}")
	private int receiveTimeout;
	@Value("${azure.receiver.max-event-count:10}")
	private int maxEventCount;

	@Autowired
	private AzureAccountService accountService;

	private ConcurrentHashMap<String, AccountWorker> accounts = new ConcurrentHashMap<>();

	@Override
	protected void preDestroy() {
		super.preDestroy();
		accounts.values().forEach(a -> a.shutdown());
	}

	@Override
	protected Runnable createAccountMonitor() {
		return new AccountMonitor();
	}

	public boolean containsAccount(String hostName) {
		return accounts.containsKey(hostName);
	}

	private class AccountWorker {

		final AzureAccount account;
		EventHubClient client;
		List<PartitionReceiver> receivers;
		boolean terminating = false;

		RetriableTask<EventHubClient> task = new RetriableTask<EventHubClient>(getRetryStrategy()) {

			@Override
			public EventHubClient execute() throws Exception {
				String method = "AccountWorker.RetriableTask.execute";
				client = EventHubClient
				        .createFromConnectionStringSync(accountService.buildEventHubConnectionString(account));
				String consumerGroupName = account.getConsumerGroupName();
				if (StringUtils.isEmpty(consumerGroupName)) {
					consumerGroupName = EventHubClient.DEFAULT_CONSUMER_GROUP_NAME;
				}
				logInfo(method, "consumerGroupName: %s", consumerGroupName);
				for (int i = 0; i < account.getNumPartitions(); i++) {
					TelemetrySync telemetrySync = account.getTelemetrySync();
					if (telemetrySync == null || telemetrySync == TelemetrySync.CURRENT) {
						PartitionReceiver receiver = client.createReceiverSync(consumerGroupName, Integer.toString(i),
						        Instant.now());
						receiver.setReceiveTimeout(Duration.ofSeconds(receiveTimeout));
						receiver.setReceiveHandler(new ReceiveHandler(maxEventCount, AccountWorker.this));
						receivers.add(receiver);
						logInfo(method, "created receiver for partition: %d", i);

					} else {
						String offsetKey = "offset-" + account.getId() + "-" + i;
						String offset = getContext().getRedis().opsForValue().get(offsetKey);
						if (StringUtils.isEmpty(offset)) {
							offset = PartitionReceiver.START_OF_STREAM;
							logWarn(method, "previous offset not found, start from the beginning!");
						} else {
							logInfo(method, "found existing offset: %s", offset);
						}
						PartitionReceiver receiver = client.createReceiverSync(consumerGroupName, Integer.toString(i),
						        offset, false);
						receiver.setReceiveTimeout(Duration.ofSeconds(receiveTimeout));
						receiver.setReceiveHandler(
						        new ReceiveHandler(maxEventCount, AccountWorker.this, offsetKey, offset));
						receivers.add(receiver);
						logInfo(method, "created receiver for partition: %d with offset: %s", i, offset);
					}
				}
				logInfo(method, "worker started!");
				return client;
			}
		};

		public AccountWorker(AzureAccount account) {
			this.account = account;
			this.receivers = new ArrayList<>();
		}

		synchronized void start() {
			String method = "AccountWorker.start";
			logDebug(method, "...");

			task.call();
		}

		synchronized void shutdown() {
			if (!terminating) {
				terminating = true;
				task.setTerminating(true);
				String method = "AccountWorker.shutdown";
				logDebug(method, "...");

				for (PartitionReceiver receiver : receivers) {
					try {
						logInfo(method, "closing receiver ...");
						receiver.closeSync();
					} catch (Throwable t) {
					}
				}
				receivers.clear();

				if (client != null) {
					try {
						logInfo(method, "closing client ...");
						client.closeSync();
						client = null;
					} catch (Throwable t) {
					}
				}
				terminating = false;
				logDebug(method, "done.");
			}
		}

		synchronized void restart() {
			String method = "restart";
			logDebug(method, "...");
			shutdown();
			start();
			logDebug(method, "restarted.");
		}
	}

	private class AccountMonitor implements Runnable {

		@Override
		public void run() {
			String method = "AccountExecutor.run";
			logDebug(method, "...");
			List<AzureAccount> azureAccounts = accountService.getAzureAccountRepository().findAll();
			Set<String> enabledHostNames = azureAccounts.stream()
			        .filter(account -> account.isEnabled() && checkLoopbackEnabled(account.getApplicationId()))
			        .map(account -> account.getHostName()).collect(Collectors.toSet());
			for (AzureAccount account : azureAccounts) {
				if (account.isEnabled() && checkLoopbackEnabled(account.getApplicationId())) {
					if (account.getTelemetrySync() == TelemetrySync.NONE) {
						logInfo(method, "telemetry is disabled for account: %s", account.getId());
					} else {
						if (!accounts.containsKey(account.getHostName())) {
							logInfo(method, "starting worker: %s", account.getHostName());
							AccountWorker worker = new AccountWorker(account);
							accounts.put(account.getHostName(), worker);
							worker.start();
						} else {
							logDebug(method, "hostName already added: %s", account.getHostName());
						}
					}
				} else if (!enabledHostNames.contains(account.getHostName())) {
					logInfo(method, "shutdown worker: %s", account.getHostName());
					AccountWorker worker = accounts.remove(account.getHostName());
					if (worker != null) {
						worker.shutdown();
					} else {
						logDebug(method, "hostName already removed: %s", account.getHostName());
					}
				}
			}
		}
	}

	private class ReceiveHandler extends PartitionReceiveHandler {
		private final static long OFFSET_PERSIST_INTERVAL_SECS = 10;
		final AccountWorker accountWorker;
		boolean trackingOffset = true;
		String offsetKey;
		String offset;
		long lastOffsetTs = 0;

		protected ReceiveHandler(int maxEventCount, AccountWorker accountWorker, String offsetKey, String offset) {
			super(maxEventCount);
			this.accountWorker = accountWorker;
			this.offsetKey = offsetKey;
			this.offset = offset;
			this.trackingOffset = true;
		}

		protected ReceiveHandler(int maxEventCount, AccountWorker accountWorker) {
			super(maxEventCount);
			this.accountWorker = accountWorker;
			this.trackingOffset = false;
		}

		@Override
		public void onReceive(Iterable<EventData> events) {
			String method = "ReceiveHandler.onReceive";
			logDebug(method, "...");
			if (events != null) {
				for (EventData event : events) {
					String message = new String(event.getBytes(), StandardCharsets.UTF_8);
					logInfo(method, "message: %s", message);
					if (!isTerminating()) {
						getService().execute(new RabbitWorker(message));
						if (trackingOffset) {
							SystemProperties props = event.getSystemProperties();
							if (props != null) {
								logDebug(method, "Offset: %s, SeqNo: %s, EnqueueTime: %s", props.getOffset(),
								        props.getSequenceNumber(), props.getEnqueuedTime());
								offset = props.getOffset();
							}
						}
					}
				}
			}

			if (trackingOffset) {
				// saving new offset to redis
				long now = Instant.now().getEpochSecond();
				if (now - lastOffsetTs > OFFSET_PERSIST_INTERVAL_SECS) {
					getContext().getRedis().opsForValue().set(offsetKey, offset);
					lastOffsetTs = now;
				}
			}
		}

		@Override
		public void onError(Throwable error) {
			String method = "ReceiveHandler.onError";
			logError(method, "ERROR", error);
			logInfo(method, "restarting account worker ...");
			accountWorker.restart();
		}
	}
}
