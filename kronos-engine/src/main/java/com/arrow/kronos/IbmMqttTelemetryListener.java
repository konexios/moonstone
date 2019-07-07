package com.arrow.kronos;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.text.StrSubstitutor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.service.IbmAccountService;
import com.arrow.kronos.util.RetriableTask;
import com.arrow.pegasus.data.profile.Application;

public class IbmMqttTelemetryListener extends TelemetryLoopbackAbstract {

	private static final String ORGANIZATION_ID_PLACEHOLDER = "organizationId";

	@Value("${ibm.mqtt.server-url}")
	private String serverUrlFormat;
	@Value("${ibm.mqtt.topic}")
	private String topic;
	@Value("${ibm.mqtt.client-id}")
	private String clientIdFormat;
	@Value("${ibm.mqtt.connection-timeout:60}")
	private int connectionTimeout;
	@Value("${ibm.mqtt.keep-alive-interval:60}")
	private int keepAliveInterval;

	@Autowired
	private IbmAccountService accountService;

	private ConcurrentHashMap<String, MqttClient> clients = new ConcurrentHashMap<>();

	@Override
	protected Runnable createAccountMonitor() {
		return new AccountMonitor();
	}

	private boolean connectClient(IbmAccount account) {
		String method = "connectClient";

		String organizationId = account.getOrganizationId();
		logInfo(method, "accountId: %s, organizationId: %s", account.getId(), organizationId);
		if (!atomicModifyConnectingClient(organizationId, true)) {
			logWarn(method, "connection already in progress for organizationId: %s", organizationId);
			return false;
		}

		close(clients.remove(organizationId));
		MqttClient client = createClient(account);
		if (client != null) {
			clients.put(organizationId, client);
			atomicModifyConnectingClient(organizationId, false);
		} else {
			logWarn(method, "skipping connect!");
		}
		return client != null;
	}

	private MqttClient createClient(IbmAccount account) {
		String method = "createClient";

		String organizationId = account.getOrganizationId();
		logInfo(method, "accountId: %s, organizationId: %s", account.getId(), organizationId);

		RetriableTask<MqttClient> task = new RetriableTask<MqttClient>(getRetryStrategy()) {

			MqttClient client = null;

			@Override
			public MqttClient execute() throws Exception {
				StrSubstitutor strSubstitutor = new StrSubstitutor(
						Collections.singletonMap(ORGANIZATION_ID_PLACEHOLDER, organizationId), "{", "}");

				String serverUrl = strSubstitutor.replace(serverUrlFormat);
				logInfo(method, "serverUrl: %s", serverUrl);

				Application application = getContext().getCoreCacheService()
						.findApplicationById(account.getApplicationId());
				checkEnabled(application, "application");

				String apiKey = getContext().getCryptoService().decrypt(application.getId(), account.getApiKey());
				String authToken = getContext().getCryptoService().decrypt(application.getId(), account.getAuthToken());

				String clientId = strSubstitutor.replace(clientIdFormat);
				logInfo(method, "clientId: %s", clientId);

				client = new MqttClient(serverUrl, clientId);
				client.setCallback(new Callback(account));

				MqttConnectOptions options = new MqttConnectOptions();
				options.setUserName(apiKey);
				options.setPassword(authToken.toCharArray());
				options.setConnectionTimeout(connectionTimeout);
				options.setKeepAliveInterval(keepAliveInterval);
				options.setCleanSession(true);

				logInfo(method, "connecting to: %s", serverUrl);
				client.connect(options);
				logInfo(method, "connected");

				logInfo(method, "subscribing to: %s", topic);
				client.subscribe(topic, 0);
				logInfo(method, "subscribed");
				return client;
			}

			@Override
			public void onError() {
				close(client);
			}
		};
		return task.call();
	}

	@Override
	protected void preDestroy() {
		super.preDestroy();
		clients.values().forEach(client -> close(client));
	}

	@Override
	protected void postConstruct() {
		super.postConstruct();
		if (isEnabled()) {
			Assert.hasText(serverUrlFormat, "ibm.mqtt.server-url is empty");
			Assert.hasText(topic, "ibm.mqtt.topic is empty");
			Assert.hasText(clientIdFormat, "ibm.mqtt.client-id is empty");
		}
	}

	private void close(MqttClient client) {
		String method = "close";
		try {
			if (client != null) {
				logInfo(method, "closing: %s", client.getServerURI());
				client.close();
			}
		} catch (Throwable t) {
		}
	}

	private class Callback implements MqttCallback {

		private final IbmAccount account;

		private Callback(IbmAccount account) {
			this.account = account;
		}

		@Override
		public void connectionLost(Throwable cause) {
			String method = "connectionLost";
			logInfo(method, cause.getMessage());
			connectClient(account);
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			String method = "messageArrived";
			try {
				logInfo(method, "applicationId: %s, topic: %s, message size: %s", account.getApplicationId(), topic,
						message.getPayload().length);
				if (!isTerminating()) {
					getService().execute(new RabbitWorker(new String(message.getPayload(), StandardCharsets.UTF_8)));
				}
			} catch (Throwable t) {
				logError(method, "error sending message to worker thread", t);
			}
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
		}
	}

	private class AccountMonitor implements Runnable {
		@Override
		public void run() {
			String method = "AccountMonitor.run";
			try {
				List<IbmAccount> accounts = accountService.getIbmAccountRepository().findAll();

				Set<String> activeOrgIds = new HashSet<>();
				accounts.stream()
						.filter(account -> account.isEnabled() && checkLoopbackEnabled(account.getApplicationId()))
						.forEach(account -> activeOrgIds.add(account.getOrganizationId()));

				for (IbmAccount account : accounts) {
					String organizationId = account.getOrganizationId();
					boolean isEnabled = account.isEnabled();
					boolean hasLoopback = checkLoopbackEnabled(account.getApplicationId());
					logInfo(method, "processing organizationId: %s, isEnabled: %s, hasLoopback: %s", organizationId,
							isEnabled, hasLoopback);
					if (isEnabled && hasLoopback) {
						try {
							// key is organizationId
							if (!clients.containsKey(organizationId)) {
								logInfo(method, "creating new client for organization: %s", organizationId);
								new Thread(() -> {
									try {
										connectClient(account);
									} catch (Exception e) {
										logError(method, "error connecting client", e);
									}
								}).start();
							} else {
								logInfo(method, "organizationId already added: %s", organizationId);
							}
						} catch (Throwable t) {
							logError(method, t);
						}
					} else if (!activeOrgIds.contains(organizationId)) {
						MqttClient client = clients.remove(organizationId);
						if (client != null) {
							close(client);
						} else {
							logInfo(method, "organizationId already removed: %s", organizationId);
						}
					}
				}
			} catch (Throwable t) {
				logError(method, "error refreshing accounts", t);
			}
		}
	}
}
