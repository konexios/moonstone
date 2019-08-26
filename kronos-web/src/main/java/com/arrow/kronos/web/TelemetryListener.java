package com.arrow.kronos.web;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.arrow.pegasus.service.RabbitListenerAbstract;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.MqttConstants;
import moonstone.acs.JsonUtils;

@Component
public class TelemetryListener extends RabbitListenerAbstract implements CommandLineRunner, ConnectionListener {

	@Autowired
	private TelemetryProcessor telemetryProcessor;
	@Autowired
	private TelemetrySubscription telemetrySubscription;
	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private Queue queue;
	private TopicExchange exchange;
	private String queueName;

	private ConcurrentHashMap<String, LongAdder> gateways = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, LongAdder> devices = new ConcurrentHashMap<>();

	@Override
	public void run(String... args) throws Exception {
		String method = "run";
		logInfo(method, "...");
		start();
	}

	@Override
	protected void postConstruct() {
		super.postConstruct();
		init();
		setApplicationEventPublisher(applicationEventPublisher);
		connectionFactory.addConnectionListener(this);
	}

	private void init() {
		String method = "init";
		logDebug(method, "declaring exchange %s", MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		if (exchange == null) {
			exchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		}
		getRabbitAdmin().declareExchange(exchange);
		if (queue == null) {
			declareNewQueue();
		}
		synchronized (this) {
			gateways.keySet().forEach(gatewayHid -> {
				for (Binding binding : buildBinding(gatewayHid)) {
					logInfo(method, "add binding: %s", binding.getRoutingKey());
					getRabbitAdmin().declareBinding(binding);
				}
			});
		}
	}

	private void declareNewQueue() {
		String method = "declareQueue";
		queueName = String.format("%s.%s", KronosWebConstants.DEVICE_TELEMETRY_RABBITMQ_QUEUE_NAME,
		        RandomStringUtils.randomAlphanumeric(10).toLowerCase());
		queue = new Queue(queueName, true, true, true);
		if (getRabbitAdmin().getQueueProperties(queueName) == null) {
			logDebug(method, "declaring queue %s", queueName);
			getRabbitAdmin().declareQueue(queue);
		}

		setQueues(new String[] { queueName });
	}

	@Override
	public void receiveMessage(byte[] message, String queueName) {
		String method = "receiveMessage";
		if (MqttConstants.isGatewayToServerTelemetryRouting(queueName)) {
			blockDispatch(new Runnable() {
				@Override
				public void run() {
					try {
						Map<String, String> telemetry = JsonUtils.fromJsonBytes(message,
						        new TypeReference<Map<String, String>>() {
						        });
						telemetryProcessor.process(telemetry, devices.keySet());
					} catch (Exception e) {
						logError(method, e);
					}
				}
			});
		} else if (MqttConstants.isGatewayToServerTelemetryBatchRouting(queueName)) {
			blockDispatch(new Runnable() {
				@Override
				public void run() {
					try {
						List<Map<String, String>> telemetryList = JsonUtils.fromJsonBytes(message,
						        new TypeReference<List<Map<String, String>>>() {
						        });
						telemetryList.forEach(telemetry -> telemetryProcessor.process(telemetry, devices.keySet()));
					} catch (Exception e) {
						logError(method, e);
					}
				}
			});
		} else {
			logError(method, "ERROR: queueName not supported: %s", queueName);
		}
	}

	public void subscribe(String gatewayHid, String deviceHid, StompHeaderAccessor accessor) {
		String method = "subscribe";
		logDebug(method, "...");
		Assert.hasText(gatewayHid, "gatewayHid is empty");
		Assert.hasText(deviceHid, "deviceHid is empty");
		logDebug(method, "%s", accessor);
		synchronized (telemetrySubscription) {
			TelemetrySubscription.TelemetrySubscriptionModel sub = telemetrySubscription
			        .subscribe(accessor.getSubscriptionId(), gatewayHid, deviceHid);
			logDebug(method, "%s", sub);
			if (sub != null) {
				doSubscribe(sub.getGatewayHid(), sub.getDeviceHid());
			}
		}
	}

	public void unsubscribe(StompHeaderAccessor accessor) {
		String method = "unsubscribe";
		logDebug(method, "%s", accessor);
		if (StringUtils.isNotEmpty(accessor.getSubscriptionId())) {
			synchronized (telemetrySubscription) {
				TelemetrySubscription.TelemetrySubscriptionModel sub = telemetrySubscription
				        .unsubscribe(accessor.getSubscriptionId());
				logDebug(method, "%s %s", accessor.getSubscriptionId(), sub);
				if (sub != null) {
					doUnsubscribe(sub.getGatewayHid(), sub.getDeviceHid());
				}
			}
		}
	}

	public void disconnect(StompHeaderAccessor accessor) {
		String method = "disconnect";
		synchronized (telemetrySubscription) {
			for (TelemetrySubscription.TelemetrySubscriptionModel sub : telemetrySubscription.unsubscribeAll()
			        .values()) {
				logDebug(method, "%s", sub);
				if (sub != null) {
					doUnsubscribe(sub.getGatewayHid(), sub.getDeviceHid());
				}
			}
		}
	}

	@EventListener
	public void unsubscribeEvent(SessionUnsubscribeEvent event) {
		String method = "unsubscribeEvent";
		logDebug(method, "...");
		logDebug(method, "event=%s", event);
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		unsubscribe(accessor);
	}

	@EventListener
	public void disconnectEvent(SessionDisconnectEvent event) {
		String method = "unsubscribeEvent";
		logDebug(method, "...");
		logDebug(method, "event=%s", event);
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		disconnect(accessor);
	}

	@Override
	public void onCreate(Connection connection) {
		String method = "onCreate";
		logDebug(method, "...");
		logDebug(method, "connection: %s", connection);
		init();
	}

	@Override
	public void onClose(Connection connection) {
		String method = "onClose";
		logDebug(method, "connection: %s", connection);
	}

	@Override
	protected void onMessageListenerContainerRestart() {
		String method = "onMessageListenerContainerRestart";
		logDebug(method, "...");
		declareNewQueue();
		init();
	}

	@Override
	protected int getNumWorkerThreads() {
		return KronosWebConstants.DEVICE_TELEMETRY_LISTENER_NUM_THREADS;
	}

	private synchronized void doSubscribe(String gatewayHid, String deviceHid) {
		String method = "doSubscribe";
		if (gatewayHid == null || deviceHid == null) {
			return;
		}
		gateways.computeIfAbsent(gatewayHid, v -> {
			for (Binding binding : buildBinding(gatewayHid)) {
				logInfo(method, "add binding: %s", binding.getRoutingKey());
				getRabbitAdmin().declareBinding(binding);
			}
			return new LongAdder();
		});
		gateways.computeIfPresent(gatewayHid, (k, v) -> v).increment();
		devices.computeIfAbsent(deviceHid, v -> new LongAdder());
		devices.computeIfPresent(deviceHid, (k, v) -> v).increment();
		logDebug(method, "gateways=%s devices=%s", gateways, devices);
	}

	private synchronized void doUnsubscribe(String gatewayHid, String deviceHid) {
		String method = "doUnsubscribe";
		if (gatewayHid == null || deviceHid == null) {
			return;
		}
		devices.computeIfPresent(deviceHid, (k, v) -> v).decrement();
		if (devices.get(deviceHid) != null && devices.get(deviceHid).longValue() < 1) {
			devices.remove(deviceHid);
		}
		gateways.computeIfPresent(gatewayHid, (k, v) -> v).decrement();
		if (gateways.get(gatewayHid) != null && gateways.get(gatewayHid).longValue() < 1) {
			for (Binding binding : buildBinding(gatewayHid)) {
				logInfo(method, "remove binding: %s", binding.getRoutingKey());
				getRabbitAdmin().removeBinding(binding);
			}
			gateways.remove(gatewayHid);
		}
		logDebug(method, "gateways=%s devices=%s", gateways, devices);
	}

	private Binding[] buildBinding(String gatewayHid) {
		String match1 = String.format("#.%s", gatewayHid);

		// TODO this is a work-round for a bug in iOS app that sent wrong
		// routing key
		String match2 = String.format("#.Optional(\"%s\")", gatewayHid);
		return new Binding[] { BindingBuilder.bind(queue).to(exchange).with(match1),
		        BindingBuilder.bind(queue).to(exchange).with(match2) };
	}
}
