package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acn.MqttConstants;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.GatewayPayloadSigner;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.GatewayEventModel;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.service.EventService;

@Service
public class GatewayCommandService extends KronosServiceAbstract {

	private static final String PAYLOAD_SIGNATURE_VERSION = "1";
	private static final String MQTT_SUBSCRIPTION_FORMAT = "mqtt-subscription-%sqos1";

	// TODO revisit
	public static final long DEFAULT_MESSAGE_EXPIRATION = 1000 * 60 * 60 * 24; // 24
																				// hours

	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private EventService eventService;
	@Autowired
	private RabbitTemplate rabbit;

	private RabbitAdmin rabbitAdmin;
	private TopicExchange exchange;

	// public Event sendEvent(Event event, String gatewayId) {
	// return sendEvent(event, gatewayId, (AccessKey) null, null, (Long) null);
	// }

	public Event sendEvent(Event event, String gatewayId, String who) {
		return sendEvent(event, gatewayId, (AccessKey) null, who, (Long) null);
	}

	// public Event sendEvent(Event event, String gatewayId, Long
	// messageExpiration) {
	// return sendEvent(event, gatewayId, (AccessKey) null, null,
	// messageExpiration);
	// }

	public Event sendEvent(Event event, String gatewayId, AccessKey accessKey) {
		return sendEvent(event, gatewayId, accessKey, null, (Long) null);
	}

	public Event sendEvent(Event event, String gatewayId, AccessKey accessKey, String who, Long messageExpiration) {
		String method = "send";
		Assert.notNull(event, "event is null");
		Assert.hasText(event.getApplicationId(), "applicationId is empty");
		Assert.hasText(event.getName(), "name is empty");
		Assert.notNull(event.getType(), "type is null");

		if (rabbitAdmin == null) {
			initializeRabbit();
		}

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		checkEnabled(gateway, "gateway");

		if (messageExpiration == null || messageExpiration < 0) {
			messageExpiration = getDefaultMessageExpirationMillis();
		}

		// needs to be captured after logical check, may have needed to be
		// defaulted
		event.getParameters().add(EventParameter.InLong("messageExpiration", messageExpiration));
		event.getParameters()
				.add(EventParameter.InLong("messageExpiresAt", (System.currentTimeMillis() + messageExpiration)));

		event = eventService.getEventRespository().doInsert(event, null);
		logInfo(method, "created event name: %s, id: %s", event.getName(), event.getId());

		// copy event data to gateway event
		GatewayEventModel gatewayEvent = new GatewayEventModel();
		gatewayEvent.setHid(event.getHid());
		gatewayEvent.setName(event.getName());
		gatewayEvent.setEncrypted(event.typeSecured());

		event.getParameters().forEach(parameter -> {
			gatewayEvent.getParameters().put(parameter.getName(), parameter.getValue());
		});

		if (accessKey != null) {
			// need to populate access key
			accessKey = getCoreCacheHelper().populateAccessKey(accessKey);

			// decrypt keys and cache them
			accessKey = decryptKeys(accessKey);
			String apiKey = accessKey.getApiKey();
			String secretKey = accessKey.getSecretKey();

			GatewayPayloadSigner signer = GatewayPayloadSigner.create(secretKey).withApiKey(apiKey)
					.withEncrypted(gatewayEvent.isEncrypted()).withName(gatewayEvent.getName())
					.withHid(gatewayEvent.getHid());
			event.getParameters().forEach(param -> signer.withParameter(param.getName(), param.getValue()));

			gatewayEvent.setSignature(createSignature(signer, PAYLOAD_SIGNATURE_VERSION));
			gatewayEvent.setSignatureVersion(PAYLOAD_SIGNATURE_VERSION);
		}

		String routing = MqttConstants.serverToGatewayCommandRouting(gateway.getHid());
		MessageBuilder messageBuilder = MessageBuilder.withBody(JsonUtils.toJsonBytes(gatewayEvent));
		messageBuilder.setExpiration(messageExpiration.toString());
		rabbit.convertAndSend(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE, routing, messageBuilder.build());

		if (isDebugEnabled()) {
			logDebug(method, "sent to routing: %s, json: %s, messageExpiration: %s", routing,
					JsonUtils.toJson(gatewayEvent), messageExpiration);
			logDebug(method, "gatewayHid: %s", gateway.getHid());
		}

		createAuditLog(event, (StringUtils.isEmpty(who) ? accessKey.getPri() : who));

		return event;
	}

	/**
	 * @param event
	 * @param accessKey
	 */
	private void createAuditLog(Event event, String who) {
		Assert.hasText(who, "who is empty");

		String method = "createAuditLog";

		String deviceHid = null;
		String command = null;
		String payload = null;

		for (EventParameter param : event.getParameters()) {
			logDebug(method, "param = name: %s, value: %s", param.getName(), param.getValue());
			switch (param.getName()) {
			case "deviceHid":
				deviceHid = param.getValue();
				break;
			case "command":
				command = param.getValue();
				break;
			case "payload":
				payload = param.getValue();
				break;
			default:
				break;
			}
		}

		logDebug(method, "deviceHid: %s, command: %s, payload: %s", deviceHid, command, payload);

		if (!StringUtils.isEmpty(deviceHid)) {
			Device device = getKronosCache().findDeviceByHid(deviceHid);
			logDebug(method, "device: " + (device == null ? "null" : device.getId()));
			if (device != null) {

				AuditLogBuilder builder = AuditLogBuilder.create().type(KronosAuditLog.Device.SendCommand)
						.applicationId(device.getApplicationId()).objectId(device.getId())
						.productName(ProductSystemNames.KRONOS).by(who).parameter("eventHid", event.getHid())
						.parameter("eventName", event.getName()).parameter("command", command)
						.parameter("payload", payload);

				getAuditLogService().create(builder);
			}
		}
	}

	public void createMqttSubscriptionQueue(String gatewayHid) {
		Assert.hasText(gatewayHid, "gatewayHid is empty");
		String method = "createMqttSubscriptionQueue";
		if (rabbitAdmin == null) {
			initializeRabbit();
		}
		// create durable, non-exclusive, non-auto-delete queue
		Queue queue = new Queue(String.format(MQTT_SUBSCRIPTION_FORMAT, gatewayHid), true, false, false);
		logDebug(method, "declare queue: %s", queue.getName());
		rabbitAdmin.declareQueue(queue);
		Binding binding = BindingBuilder.bind(queue).to(exchange)
				.with(MqttConstants.serverToGatewayCommandRouting(gatewayHid));
		logDebug(method, "declare binding: %s", binding.getRoutingKey());
		rabbitAdmin.declareBinding(binding);
	}

	public void deleteMqttSubscriptionQueue(String gatewayHid) {
		Assert.hasText(gatewayHid, "gatewayHid is empty");
		String method = "deleteMqttSubscriptionQueue";
		if (rabbitAdmin == null) {
			initializeRabbit();
		}
		String queueName = String.format(MQTT_SUBSCRIPTION_FORMAT, gatewayHid);
		boolean result = rabbitAdmin.deleteQueue(queueName);
		logInfo(method, "delete queue %s: %s", queueName, result);
	}

	public void deleteMqttSubscriptionQueue(String gatewayHid, boolean unused, boolean empty) {
		Assert.hasText(gatewayHid, "gatewayHid is empty");
		String method = "deleteMqttSubscriptionQueue";
		if (rabbitAdmin == null) {
			initializeRabbit();
		}
		String queueName = String.format(MQTT_SUBSCRIPTION_FORMAT, gatewayHid);
		logInfo(method, "delete queue %s", queueName);
		rabbitAdmin.deleteQueue(queueName, unused, empty);
	}

	// TODO revisit
	public long getDefaultMessageExpirationMillis() {
		return DEFAULT_MESSAGE_EXPIRATION;
	}

	private synchronized void initializeRabbit() {
		String method = "initializeRabbit";

		if (rabbitAdmin == null) {
			rabbitAdmin = new RabbitAdmin(connectionFactory);
			logDebug(method, "declaring exchange %s", MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
			exchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
			rabbitAdmin.declareExchange(exchange);
		}
	}

	private String createSignature(GatewayPayloadSigner signer, String version) {
		switch (version) {
		case GatewayPayloadSigner.PAYLOAD_SIGNATURE_VERSION_1:
			return signer.signV1();
		default:
			throw new AcsLogicalException("Unsupported signature version");
		}
	}

	private synchronized AccessKey decryptKeys(AccessKey accessKey) {
		if (StringUtils.isEmpty(accessKey.getApiKey())) {
			accessKey.setApiKey(
					getCryptoService().decrypt(accessKey.getRefApplication().getId(), accessKey.getEncryptedApiKey()));
		}
		if (StringUtils.isEmpty(accessKey.getSecretKey())) {
			accessKey.setSecretKey(
					getCryptoService().decrypt(accessKey.getRefApplication().getId(), accessKey.getEncryptedSecretKey()));
		}
		return accessKey;
	}
}
