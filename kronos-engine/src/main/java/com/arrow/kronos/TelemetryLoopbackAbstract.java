package com.arrow.kronos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

import com.arrow.acn.MqttConstants;
import com.arrow.acs.JsonUtils;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.util.CustomIntervalRetry;
import com.arrow.kronos.util.RetryStrategy;
import com.arrow.pegasus.service.ProcessorAbstract;
import com.fasterxml.jackson.core.type.TypeReference;

public abstract class TelemetryLoopbackAbstract extends ProcessorAbstract
		implements CommandLineRunner, ContextListener {

	static final TypeReference<Map<String, String>> MAP_TYPE_REF = new TypeReference<Map<String, String>>() {
	};

	static final long DEFAULT_ACCOUNT_REFRESH_INTERVAL_SECS = 60;
	static final String DEFAULT_RETRY_INTERVAL = "PT5S,PT1H,PT24H";
	static final int DEFAULT_MEM_CACHE_EXPIRES_MINS = 60;

	@Autowired
	private Environment env;
	@Autowired
	private KronosEngineContext context;

	private Set<String> connectingClients = new HashSet<>();
	private ScheduledExecutorService accountExecutor = Executors.newScheduledThreadPool(1);
	private RetryStrategy retryStrategy;

	@Override
	protected void postConstruct() {
		super.postConstruct();
		if (isEnabled()) {
			String[] retryIntervals = getEnvProperty("retryInterval", DEFAULT_RETRY_INTERVAL).split(",");
			retryStrategy = new CustomIntervalRetry(retryIntervals);
		}
	}

	@Override
	protected void preDestroy() {
		super.preDestroy();
		accountExecutor.shutdownNow();
	}

	@Override
	public void applicationListChanged(Collection<String> applicationIds) {
		String method = "applicationListChanged";
		logInfo(method, "...");
	}

	@Override
	public void applicationSettingsChanged(String applicationId) {
		String method = "applicationSettingsChanged";
		logInfo(method, "...");
	}

	@Override
	public void run(String... args) throws Exception {
		String method = "run";
		if (isEnabled()) {
			start();
		} else {
			logWarn(method, "%s is DISABLED!", getClass().getSimpleName());
		}
	}

	@Override
	public void start() {
		super.start();
		String method = "start";
		long refreshInterval = Long
				.parseLong(getEnvProperty("accountRefreshIntervalSecs", "" + DEFAULT_ACCOUNT_REFRESH_INTERVAL_SECS));
		logInfo(method, "accountRefreshIntervalSecs: %s", refreshInterval);
		getAccountExecutor().scheduleWithFixedDelay(createAccountMonitor(), 0, refreshInterval, TimeUnit.SECONDS);
		getContext().addListener(this);
		logInfo(method, "started!");
	}

	@Override
	protected int getServiceNumThreads() {
		return Integer.parseInt(getEnvProperty("numThreads", "" + KronosEngineConstants.DEFAULT_PROCESSOR_NUM_THREADS));
	}

	private String getEnvProperty(String property, String defaultValue) {
		return env.getProperty(String.format("%s.%s", getClass().getName(), property), defaultValue).trim();
	}

	protected synchronized boolean atomicModifyConnectingClient(String organizationId, boolean adding) {
		if (adding) {
			return connectingClients.add(organizationId);
		} else {
			return connectingClients.remove(organizationId);
		}
	}

	protected boolean checkLoopbackEnabled(String applicationId) {
		KronosApplication kronosApplication = context.getKronosCache()
				.findKronosApplicationByApplicationId(applicationId);
		return kronosApplication == null || (kronosApplication != null && kronosApplication.isIotProviderLoopback());
	}

	protected ScheduledExecutorService getAccountExecutor() {
		return accountExecutor;
	}

	protected KronosEngineContext getContext() {
		return context;
	}

	protected RetryStrategy getRetryStrategy() {
		return retryStrategy;
	}

	protected boolean isEnabled() {
		return StringUtils.equalsIgnoreCase(getEnvProperty("enabled", "true"), "true");
	}

	class RabbitWorker implements Runnable {
		private final String message;

		public RabbitWorker(String message) {
			this.message = message;
		}

		@Override
		public void run() {
			String method = "RabbitWorker.run";
			try {
				Map<String, String> payload = JsonUtils.fromJson(message, MAP_TYPE_REF);
				logInfo(method, "payload keys: %d", payload.size());
				String deviceHid = payload.get("_|deviceHid");
				if (!StringUtils.isEmpty(deviceHid)) {
					Device device = context.getKronosCache().findDeviceByHid(deviceHid);
					if (device != null) {
						Gateway gateway = context.getKronosCache().findGatewayById(device.getGatewayId());
						if (gateway != null) {
							context.getRabbit().convertAndSend(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE,
									MqttConstants.gatewayToServerTelemetryRouting(gateway.getHid()), message);
							logInfo(method, "message sent for deviceHid: %s", deviceHid);
						} else {
							logError(method, "gateway not found, id: %s", device.getGatewayId());
						}
					} else {
						logError(method, "device not found, hid: %s", deviceHid);
					}
				} else {
					logError(method, "deviceHid not found!");
				}
			} catch (Throwable t) {
				logError(method, "error processing message", t);
			}
		}
	}

	protected abstract Runnable createAccountMonitor();
}
