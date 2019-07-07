package com.arrow.kronos.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.arrow.acn.MqttConstants;
import com.arrow.acs.JsonUtils;
import com.arrow.kronos.KronosApiConstants;
import com.arrow.pegasus.service.RabbitListenerAbstract;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class DeviceTelemetryListener extends RabbitListenerAbstract implements CommandLineRunner, ConnectionListener {

	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private DeviceTelemetryProcessor deviceTelemetryProcessor;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private ConcurrentHashMap<String, Long> gateways = new ConcurrentHashMap<>();
	private TopicExchange exchange;
	private Queue queue;

	@Override
	protected void postConstruct() {
		super.postConstruct();
		init();
		setApplicationEventPublisher(applicationEventPublisher);
		connectionFactory.addConnectionListener(this);
	}

	private void init() {
		String method = "init";
		logDebug(method, "declare exchange: %s", MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		if (exchange == null) {
			exchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		}
		getRabbitAdmin().declareExchange(exchange);

		logDebug(method, "declare queue: %s", KronosApiConstants.DEVICE_TELEMETRY_QUEUE);
		if (queue == null) {
			queue = new Queue(KronosApiConstants.DEVICE_TELEMETRY_QUEUE, true, false, false);
		}
		getRabbitAdmin().declareQueue(queue);

		setQueues(new String[] { KronosApiConstants.DEVICE_TELEMETRY_QUEUE });
	}

	@Override
	public void onMessage(Message message) {
		if (message != null) {
			receiveMessage(message.getBody(), message.getMessageProperties().getReceivedRoutingKey());
		}
	}

	public void run(String... args) throws Exception {
		String method = "run";
		logDebug(method, "...");
		start();
	}

	@Override
	public void receiveMessage(byte[] message, String queueName) {
		String method = "receiveMessage";
		logDebug(method, "%s %s", queueName, message);
		if (MqttConstants.isGatewayToServerTelemetryRouting(queueName)) {
			blockDispatch(new Runnable() {
				@Override
				public void run() {
					try {
						Map<String, String> telemetry = JsonUtils.fromJsonBytes(message,
						        new TypeReference<Map<String, String>>() {
						        });
						deviceTelemetryProcessor.process(telemetry);
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
						telemetryList.forEach(telemetry -> deviceTelemetryProcessor.process(telemetry));
					} catch (Exception e) {
						logError(method, e);
					}
				}
			});
		} else {
			logError(method, "ERROR: queueName not supported: %s", queueName);
		}
	}

	@Override
	protected int getNumWorkerThreads() {
		return KronosApiConstants.DEFAULT_MQTT_LISTENER_NUM_THREADS;
	}

	public void addSubscription(String gatewayHid, String deviceHid) {
		if (gatewayHid == null) {
			return;
		}
		deviceTelemetryProcessor.addDevice(deviceHid);
		gateways.computeIfAbsent(gatewayHid, v -> new Long(0));
		gateways.computeIfPresent(gatewayHid, (k, v) -> {
			if (v.longValue() <= 0) {
				getRabbitAdmin().declareBinding(BindingBuilder.bind(queue).to(exchange)
				        .with(KronosApiConstants.kronosTelemetryRouting(gatewayHid)));
			}
			v++;
			return v;
		});
		logDebug("addSubscription", "%s %s", gatewayHid, gateways);
	}

	public void removeSubscription(String gatewayHid, String deviceHid) {
		if (gatewayHid == null) {
			return;
		}
		deviceTelemetryProcessor.removeDevice(deviceHid);
		gateways.computeIfPresent(gatewayHid, (k, v) -> {
			if (v.longValue() > 0) {
				v--;
			}
			if (v.longValue() <= 0) {
				getRabbitAdmin().removeBinding(BindingBuilder.bind(queue).to(exchange)
				        .with(KronosApiConstants.kronosTelemetryRouting(gatewayHid)));
			}
			return v;
		});
		gateways.remove(gatewayHid, new Long(0));
		logDebug("removeSubscription", "%s %s", gatewayHid, gateways);
	}

	@Override
	public void onCreate(Connection connection) {
		String method = "onCreate";
		logDebug(method, "connection: %s", connection);

		init();

		gateways.keySet().forEach(gatewayHid -> getRabbitAdmin().declareBinding(
		        BindingBuilder.bind(queue).to(exchange).with(KronosApiConstants.kronosTelemetryRouting(gatewayHid))));
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
		init();
	}
}
