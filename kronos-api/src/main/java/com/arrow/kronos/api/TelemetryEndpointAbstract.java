package com.arrow.kronos.api;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.Session;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosApiConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.service.CoreCacheService;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.Loggable;

public abstract class TelemetryEndpointAbstract<T> extends Endpoint
        implements ApplicationListener<ListenerContainerConsumerFailedEvent> {

	private final static long DEFAULT_KEEP_ALIVE_INTERVAL_SECS = 180;

	private final Loggable logger = new Loggable(self().getClass().getName()) {
	};

	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DeviceTelemetryListener deviceTelemetryListener;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private RabbitAdmin rabbitAdmin;
	private ScheduledThreadPoolExecutor keepAliveThread;
	private TopicExchange topic;

	protected ConcurrentMap<String, SessionWorker> sessions = new ConcurrentHashMap<>();
	protected ConcurrentMap<SimpleMessageListenerContainer, SessionWorker> containers = new ConcurrentHashMap<>();

	public abstract T self();

	@PostConstruct
	public void init() {
		String method = "init";
		logger.logDebug(method, "...");

		topic = new TopicExchange(KronosApiConstants.DEVICE_TELEMETRY_EXCHANGE, true, false);

		rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.declareExchange(topic);

		startKeepAliveThread();
	}

	@PreDestroy
	public void preDestroy() {
		String method = "preDestroy";
		logger.logDebug(method, "...");
		logger.logDebug(method, "%s", this);
		if (keepAliveThread != null) {
			keepAliveThread.shutdownNow();
		}
	}

	@Override
	public void onClose(Session session, CloseReason closeReason) {
		String method = "onClose";
		logger.logInfo(method, "sessionId: %s", session.getId());
		logger.logInfo(method, "reason: %d / %s", closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
		closeSession(session);
	}

	@Override
	public void onError(Session session, Throwable thr) {
		String method = "onError";
		logger.logInfo(method, "sessionId: %s", session.getId());
		logger.logInfo(method, "exception: %s", ExceptionUtils.getStackTrace(thr));
		closeSession(session);
	}

	protected KronosCache getKronosCache() {
		return kronosCache;
	}

	protected CoreCacheService getCoreCacheService() {
		return coreCacheService;
	}

	protected DeviceService getDeviceService() {
		return deviceService;
	}

	protected Loggable getLogger() {
		return logger;
	}

	protected AccessKey getAccessKey(Session session) {
		Principal principal = session.getUserPrincipal();
		Assert.isTrue(principal != null && principal instanceof UsernamePasswordAuthenticationToken,
		        "principal not found!");
		Object key = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		Assert.isTrue(key != null && key instanceof AccessKey, "invalid principal found!");
		return (AccessKey) key;
	}

	protected void validateCanReadNode(Session session, Node node) {
		Assert.notNull(node, "node is null");
		Assert.isTrue(node.isEnabled(), "node is disabled");

		AccessKey accessKey = getAccessKey(session);

		Application application = coreCacheService.findApplicationById(node.getApplicationId());
		Assert.notNull(application, "application is not found");

		if (!accessKey.canRead(node) && !accessKey.canRead(application)) {
			throw new NotAuthorizedException();
		}
	}

	protected void validateCanReadDevice(Session session, Device device) {
		Assert.notNull(device, "device is null");

		AccessKey accessKey = getAccessKey(session);

		Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway not found");
		Application application = coreCacheService.findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found");

		if (!accessKey.getApplicationId().equals(application.getId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}
		if (!accessKey.canRead(application) && !accessKey.canRead(gateway) && !accessKey.canRead(device)) {
			throw new NotAuthorizedException();
		}
	}

	private void startKeepAliveThread() {
		keepAliveThread = new ScheduledThreadPoolExecutor(1);
		keepAliveThread.scheduleWithFixedDelay(() -> {
			logger.logDebug("keepAliveThread", "...");
			for (SessionWorker worker : sessions.values()) {
				try {
					worker.keepAlive();
				} catch (Exception e) {
				}
			}
		}, 0, DEFAULT_KEEP_ALIVE_INTERVAL_SECS, TimeUnit.SECONDS);
	}

	private void closeSession(Session session) {
		String method = "doClose";
		try {
			String sessionId = session.getId();
			logger.logInfo(method, "looking up session: %s", sessionId);
			SessionWorker worker = sessions.get(sessionId);
			if (worker != null) {
				logger.logInfo(method, "found session worker, closing it ...");
				worker.terminate();
			} else {
				logger.logWarn(method, "session worker not found: %s", sessionId);
			}
		} catch (Exception e) {
			logger.logError(method, e);
		}
	}

	@Override
	public void onApplicationEvent(ListenerContainerConsumerFailedEvent event) {
		String method = "onApplicationEvent";
		logger.logDebug(method, "event: %s", event);
		if (event.isFatal() && event.getSource() instanceof SimpleMessageListenerContainer) {
			SimpleMessageListenerContainer container = (SimpleMessageListenerContainer) event.getSource();
			SessionWorker sessionWorker = containers.get(container);
			logger.logDebug(method, "sessionWorker: %s", sessionWorker);
			if (sessionWorker != null) {
				AtomicBoolean shutdownFlag = sessionWorker.shuttingDown.computeIfAbsent(container,
				        c -> new AtomicBoolean());
				if (shutdownFlag.compareAndSet(false, true)) {
					Executors.newSingleThreadExecutor().execute(() -> {
						// wait for container shutdown
						while (container.isActive()) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}

						container.removeQueues(sessionWorker.queue);

						sessionWorker.declareQueue();
						sessionWorker.declareBindings();
 
						logger.logDebug(method, "start container...");
						container.addQueues(sessionWorker.queue);
						container.afterPropertiesSet();
						container.start();
						logger.logDebug(method, "container restarted");

						sessionWorker.shuttingDown.remove(container);
					});
				}
			}
		}
	}

	protected class SessionWorker implements MessageListener, ConnectionListener {

		private final Session session;
		private Queue queue;
		private final Map<String, String> devices;
		private final String nodeId;
		private final String telemetryName;
		private SimpleMessageListenerContainer container;
		private boolean terminated = false;
		private final Map<SimpleMessageListenerContainer, AtomicBoolean> shuttingDown = new ConcurrentHashMap<>();

		public SessionWorker(Session session, String telemetryName) {
			this(session, telemetryName, null);
		}

		public SessionWorker(Session session, String telemetryName, String nodeId) {
			this.devices = new ConcurrentHashMap<>();
			this.session = session;
			this.nodeId = nodeId;
			this.telemetryName = telemetryName;
			declareQueue();

			connectionFactory.addConnectionListener(this);

			this.container = new SimpleMessageListenerContainer();
			this.container.setConnectionFactory(connectionFactory);
			this.container.setQueueNames(queue.getName());
			this.container.setMessageListener(SessionWorker.this);
			this.container.setApplicationEventPublisher(applicationEventPublisher);
			containers.putIfAbsent(container, this);
			this.container.start();
		}

		private Queue declareQueue() {
			String method = "SessionWorker.declareQueue";
			String queueName = String.format("%s.%s", KronosApiConstants.deviceTelemetryQueueName(session.getId()),
			        RandomStringUtils.randomAlphanumeric(10).toLowerCase());
			getLogger().logDebug(method, "session=%s, queueName=%s", session.getId(), queueName);
			queue = new Queue(queueName, false, true, true);
			rabbitAdmin.declareQueue(queue);
			return queue;
		}

		private void declareBindings() {
			devices.keySet().forEach(deviceHid -> rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topic)
			        .with(KronosApiConstants.deviceTelemetryRouting(deviceHid, telemetryName))));
		}

		public String getNodeId() {
			return nodeId;
		}

		public Session getSession() {
			return session;
		}

		public void createSubscription(Device device) {
			List<Device> devices = new ArrayList<>();
			devices.add(device);
			createSubscription(devices);
		}

		public void createSubscription(List<Device> devices) {
			String method = "SessionWorker.createSubscription";
			if (terminated) {
				logger.logWarn(method, "worker is already terminated!");
				return;
			}
			if (devices != null) {
				devices.forEach(this::addDevice);
			}
			sessions.putIfAbsent(session.getId(), this);
			logger.logInfo(method, "new session worker created, current size: %d", sessions.size());
		}

		public void updateSubscription(List<Device> newDevices) {
			String method = "SessionWorker.updateSubscription";
			if (terminated) {
				logger.logWarn(method, "worker is already terminated!");
				return;
			}
			if (newDevices != null) {
				// add devices
				newDevices.stream().filter(device -> !devices.containsKey(device.getHid())).forEach(this::addDevice);
				// remove devices
				devices.keySet().stream().filter(deviceHid -> !newDevices.stream()
				        .filter(device -> deviceHid.equals(device.getHid())).findAny().isPresent())
				        .forEach(this::removeDevice);
			}
		}

		private void addDevice(Device device) {
			String method = "SessionWorker.addDevice";
			logger.logDebug(method, "session=%s, device id=%s, hid=%s", session.getId(), device.getId(),
			        device.getHid());
			if (device.getGatewayId() == null) {
				logger.logWarn(method, "gateway does not exist for device id: %s", device.getId());
				return;
			}
			Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
			if (gateway == null) {
				logger.logWarn(method, "gateway is not found for id: %s", device.getGatewayId());
				return;
			}

			devices.putIfAbsent(device.getHid(), gateway.getHid());
			rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topic)
			        .with(KronosApiConstants.deviceTelemetryRouting(device.getHid(), telemetryName)));

			// add subscription
			logger.logInfo(method, "adding subscription to telemetry listener ...");
			deviceTelemetryListener.addSubscription(gateway.getHid(), device.getHid());
		}

		private void removeDevice(String deviceHid) {
			String method = "SessionWorker.removeDevice";
			logger.logDebug(method, "session=%s, device hid=%s", session.getId(), deviceHid);
			String gatewayHid = devices.remove(deviceHid);
			rabbitAdmin.removeBinding(BindingBuilder.bind(queue).to(topic)
			        .with(KronosApiConstants.deviceTelemetryRouting(deviceHid, telemetryName)));
			logger.logInfo(method, "removing subscription from telemetry listener ...");
			deviceTelemetryListener.removeSubscription(gatewayHid, deviceHid);
			logger.logDebug(method, "removed gatewayHid=%s, deviceHid=%s", gatewayHid, deviceHid);
		}

		@Override
		public void onMessage(Message message) {
			String method = "SessionWorker.onMessage";

			if (terminated) {
				logger.logWarn(method, "worker is already terminated!");
				return;
			}

			try {
				if (session.isOpen()) {
					try {
						String json = new String(message.getBody(), StandardCharsets.UTF_8);
						logger.logInfo(method, "send message: %s %s ", session.getId(), json);
						session.getBasicRemote().sendText(json);
					} catch (Exception e) {
						logger.logError(method, e);
					}
				} else {
					logger.logError(method, "session is already closed!");
					terminate();
				}
			} catch (Exception e) {
				logger.logError(method, e);
			}
		}

		public void keepAlive() {
			String method = "SessionWorker.keepAlive";
			logger.logDebug(method, "session: %s", session.getId());
			try {
				if (!terminated && session != null && session.isOpen()) {
					session.getBasicRemote().sendText("{}");
				}
			} catch (Exception e) {
				logger.logError(method, e);
			}
		}

		public synchronized void terminate() {
			String method = "SessionWorker.terminate";
			if (!terminated) {
				logger.logDebug(method, "session: %s", session.getId());

				containers.remove(container);
				connectionFactory.removeConnectionListener(this);

				logger.logInfo(method, "stopping container ...");
				container.stop();
				container.destroy();
				container = null;

				logger.logInfo(method, "deleting queue: %s", queue.getName());
				rabbitAdmin.deleteQueue(queue.getName());

				logger.logInfo(method, "removing subscription ...");
				devices.forEach(
				        (deviceHid, gatewayHid) -> deviceTelemetryListener.removeSubscription(gatewayHid, deviceHid));
				sessions.remove(session.getId());
			}
			terminated = true;
		}

		@Override
		public void onCreate(Connection connection) {
			String method = "SessionWorker.onCreate";
			logger.logDebug(method, "connection: %s", connection);
			if (queue == null) {
				declareQueue();
			} else {
				rabbitAdmin.declareQueue(queue);
			}
			declareBindings();
		}

		@Override
		public void onClose(Connection connection) {
			String method = "SessionWorker.onClose";
			logger.logDebug(method, "connection: %s", connection);
		}
	}
}
