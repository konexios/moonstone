package com.arrow.kronos;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.service.DeviceEventService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.kronos.service.TelemetryItemService;
import com.arrow.kronos.service.TelemetryProcessor;
import com.arrow.kronos.service.TelemetryService;
import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.LifeCycleAbstract;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientApplicationEngineApi;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.service.AuditLogService;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.CryptoService;

@Component
public class KronosEngineContext extends LifeCycleAbstract {

	private static final long CONTEXT_MONITOR_INTERVAL_MS = 60L;

	@Autowired
	private ApplicationContext springContext;
	@Autowired
	private KronosEngineProperties properties;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ClientApplicationApi clientApplicationApi;
	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private TelemetryService telemetryService;
	@Autowired
	private TelemetryItemService telemetryItemService;
	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private TelemetryProcessor telemetryProcessor;
	@Autowired
	private RabbitTemplate rabbit;
	@Autowired
	private StringRedisTemplate redis;
	@Autowired
	private CryptoService cryptoService;
	@Autowired
	private KronosApplicationService kronosApplicationService;
	@Autowired
	private ClientApplicationEngineApi clientApplicationEngineApi;

	private ApplicationEngine applicationEngine;
	private Map<String, Boolean> applicationIds = new HashMap<>();

	private List<ContextListener> listeners = new ArrayList<>();
	private ScheduledExecutorService contextMonitor = Executors.newScheduledThreadPool(1);

	public KronosEngineContext() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@PostConstruct
	private void init() {
		String method = "init";
		String applicationEngineId = properties.getApplicationEngineId();
		logInfo(method, "applicationEngineId: %s", applicationEngineId);
		if (!StringUtils.hasText(applicationEngineId)) {
			List<ApplicationEngine> engines = clientApplicationEngineApi.findAllByProductId(
					getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS).getId());
			if (engines.size() > 0) {
				applicationEngine = engines.get(0);
			} else {
				logError(method, "FATAL ERROR: no application engines found. Terminating application now!");
				SpringApplication.exit(springContext);
			}
		} else {
			applicationEngine = coreCacheService.findApplicationEngineById(applicationEngineId);
			if (applicationEngine == null) {
				logError(method, "FATAL ERROR: invalid applicationEngineId: " + applicationEngineId
						+ ". Terminating application now!");
				SpringApplication.exit(springContext);
			}
		}

		applicationIds.putAll(loadValidApplicationIds());

		logInfo(method, "found %d partitions for this engine", applicationIds.size());

		logInfo(method, "scheduling context monitor ...");
		contextMonitor.scheduleWithFixedDelay(new ContextMonitor(), CONTEXT_MONITOR_INTERVAL_MS,
				CONTEXT_MONITOR_INTERVAL_MS, TimeUnit.SECONDS);

		logInfo(method, "done");
	}

	@Override
	protected void preDestroy() {
		super.preDestroy();
		if (contextMonitor != null) {
			contextMonitor.shutdownNow();
		}
	}

	@EventListener
	private void applicationReadyEvent(ApplicationReadyEvent event) {
		String method = "applicationReadyEvent";
		String hostName = "Unknown";
		String ipAddress = "Unknown";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception ignored) {
		}
		logInfo(method, "id: %s, hostName: %s, ipAddress: %s", applicationEngine.getId(), hostName, ipAddress);
		getAuditLogService().save(AuditLogBuilder.create()
				.type(CoreAuditLog.ApplicationEngine.APPLICATION_ENGINE_STARTED).productName(ProductSystemNames.KRONOS)
				.objectId(applicationEngine.getId()).parameter("hostName", hostName).parameter("ipAddress", ipAddress));
	}

	@EventListener
	private void contextClosedEvent(ContextClosedEvent event) {
		String method = "contextClosedEvent";
		String hostName = "Unknown";
		String ipAddress = "Unknown";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception ignored) {
		}
		logInfo(method, "id: %s, hostName: %s, ipAddress: %s", applicationEngine.getId(), hostName, ipAddress);
		getAuditLogService().save(AuditLogBuilder.create()
				.type(CoreAuditLog.ApplicationEngine.APPLICATION_ENGINE_STOPPED).productName(ProductSystemNames.KRONOS)
				.objectId(applicationEngine.getId()).parameter("hostName", hostName).parameter("ipAddress", ipAddress));
	}

	public synchronized void addListener(ContextListener listener) {
		String method = "addListener";
		if (!listeners.contains(listener)) {
			logInfo(method, "adding context listener: %s", listener.getClass().getName());
			listeners.add(listener);
		}
	}

	public ApplicationEngine getApplicationEngine() {
		return applicationEngine;
	}

	public DeviceEventService getDeviceEventService() {
		return deviceEventService;
	}

	public TelemetryItemService getTelemetryItemService() {
		return telemetryItemService;
	}

	public TelemetryService getTelemetryService() {
		return telemetryService;
	}

	public CoreCacheService getCoreCacheService() {
		return coreCacheService;
	}

	public KronosCache getKronosCache() {
		return kronosCache;
	}

	public AuditLogService getAuditLogService() {
		return auditLogService;
	}

	public ApplicationContext getSpringContext() {
		return springContext;
	}

	protected Map<String, Boolean> getApplicationIds() {
		return applicationIds;
	}

	public TelemetryProcessor getTelemetryProcessor() {
		return telemetryProcessor;
	}

	public RabbitTemplate getRabbit() {
		return rabbit;
	}

	public StringRedisTemplate getRedis() {
		return redis;
	}

	public CryptoService getCryptoService() {
		return cryptoService;
	}

	public Map<String, Boolean> loadValidApplicationIds() {
		Map<String, Boolean> ids = new HashMap<>();
		Map<String, Product> localMap = new HashMap<>();
		Map<String, KronosApplication> map = kronosApplicationService.getKronosApplicationRepository().findAll()
				.stream().collect(Collectors.toMap(KronosApplication::getApplicationId, Function.identity()));
		clientApplicationApi.findAllByApplicationEngineId(applicationEngine.getId()).forEach(application -> {
			String productId = application.getProductId();
			Product product = localMap.get(productId);
			if (product == null) {
				localMap.put(productId, product = getCoreCacheService().findProductById(productId));
			}
			application.setRefProduct(product);
			if (isValidApplication(application)) {
				KronosApplication kronosApplication = map.get(application.getId());
				ids.put(application.getId(),
						kronosApplication != null && kronosApplication.isLiveTelemetryStreamingEnabled());
			}
		});
		return ids;
	}

	private boolean isValidApplication(Application app) {
		String method = "isValidApplication";
		if (app != null && app.isEnabled()) {
			Product product = app.getRefProduct();
			if (product != null && product.isEnabled()) {
				if (product.getSystemName().equals(ProductSystemNames.KRONOS)) {
					return true;
				} else {
					logWarn(method, "application is not of type kronos: %s / %s", app.getId(), product.getSystemName());
				}
			} else {
				logWarn(method, "product is invalid or not enabled: %s", app.getProductId());
			}
		} else {
			logWarn(method, "application is invalid or not enabled: %s", app.getId());
		}
		return false;
	}

	private class ContextMonitor implements Runnable {
		@Override
		public void run() {
			String method = "ContextMonitor.run";
			Map<String, Boolean> ids = loadValidApplicationIds();
			for (Entry<String, Boolean> entry : new HashMap<>(applicationIds).entrySet()) {
				Boolean streamingEnabled = ids.get(entry.getKey());
				if (Objects.equals(Boolean.TRUE, streamingEnabled) && !entry.getValue()) {
					for (ContextListener listener : listeners) {
						try {
							logInfo(method, "notifying context listener: %s", listener.getClass().getName());
							listener.applicationSettingsChanged(entry.getKey());
						} catch (Exception e) {
							logError(method, "error notifying context listener", e);
						}
					}
				}
				applicationIds.put(entry.getKey(), streamingEnabled);
			}
			if (!ids.keySet().containsAll(applicationIds.keySet())
					|| !applicationIds.keySet().containsAll(ids.keySet())) {
				logInfo(method, "applicationIds change detected!");
				applicationIds.clear();
				applicationIds.putAll(ids);
				for (ContextListener listener : listeners) {
					try {
						logInfo(method, "notifying context listener: %s", listener.getClass().getName());
						listener.applicationListChanged(applicationIds.keySet());
					} catch (Exception e) {
						logError(method, "error notifying context listener", e);
					}
				}
			} else {
				logInfo(method, "no change detected for applicationIds");
			}
		}
	}
}
