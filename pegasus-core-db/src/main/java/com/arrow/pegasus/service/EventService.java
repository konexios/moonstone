package com.arrow.pegasus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsSystemException;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.GatewayEventModel;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.event.EventParameterType;
import com.arrow.pegasus.data.event.EventStatus;
import com.arrow.pegasus.data.event.EventType;
import com.arrow.pegasus.repo.EventRepository;

@Service
@EnableConfigurationProperties(EventServiceProperties.class)
public class EventService extends BaseServiceAbstract {

	@Autowired
	private EventRepository eventRespository;
	@Autowired
	private EventServiceProperties properties;
	@Autowired
	private EventMonitorService eventMonitorService;
	@Autowired
	private RabbitTemplate rabbit;
	@Autowired
	private PlatformConfigService platformConfigService;

	public EventRepository getEventRespository() {
		return eventRespository;
	}

	public Event waitForResult(Event event, long timeoutMs) {
		return eventMonitorService.waitForResult(event, timeoutMs);
	}

	public Event send(String queue, Event event) {
		return send(queue, event, false);
	}

	public Event send(String queue, Event event, boolean passthrough) {
		String method = "send";
		Assert.notNull(event, "event is null");

		List<EventParameter> parameters = event.getParameters();
		if (parameters != null) {
			parameters.forEach(parameter -> {
				Assert.isTrue(parameter.getType() == EventParameterType.In, "OUT parameter is not allowed here");
			});

			// if passthrough, ignore encryption check
			if (!passthrough && event.typeSecured()) {
				encryptParameters(event, parameters);
			}
		}

		// backup parameters
		List<EventParameter> originals = event.getParameters();
		if (event.typeSecured()) {
			event.setParameters(cloneEmptyParameters(originals));
		}

		eventRespository.doInsert(event, null);
		logInfo(method, "created event name: %s, id: %s", event.getName(), event.getId());

		// restore parameters
		if (event.typeSecured()) {
			event.setParameters(originals);
		}

		rabbit.convertAndSend(queue, MessageBuilder.withBody(JsonUtils.toJsonBytes(event)).build());

		if (isDebugEnabled()) {
			logDebug(method, "sent to topic: %s, json: %s", queue, JsonUtils.toJson(event));
		}
		return event;
	}

	public Event sendToGateway(String queue, Event event) {
		return sendToGateway(queue, event, false);
	}

	public Event sendToGateway(String queue, Event event, boolean passthrough) {
		String method = "send";
		Assert.notNull(event, "event is null");

		Assert.isTrue(event.getType() == EventType.Normal, "Encrypted can't be used for gateway messaging");

		List<EventParameter> parameters = event.getParameters();
		if (parameters != null) {
			parameters.forEach(parameter -> {
				Assert.isTrue(parameter.getType() == EventParameterType.In, "OUT parameter is not allowed here");
			});

			// if pass-through, ignore encryption check
			if (!passthrough && event.typeSecured()) {
				encryptParameters(event, parameters);
			}
		}

		// backup parameters
		List<EventParameter> originals = event.getParameters();
		if (event.typeSecured()) {
			event.setParameters(cloneEmptyParameters(originals));
		}

		eventRespository.doInsert(event, null);
		logInfo(method, "created event name: %s, id: %s", event.getName(), event.getId());

		// restore parameters
		if (event.typeSecured()) {
			event.setParameters(originals);
		}

		// copy event data to gateway event
		GatewayEventModel gatewayEvent = new GatewayEventModel();
		gatewayEvent.setHid(event.getHid());
		gatewayEvent.setName(event.getName());
		gatewayEvent.setEncrypted(event.typeSecured());

		event.getParameters().forEach(parameter -> {
			gatewayEvent.getParameters().put(parameter.getName(), parameter.getValue());
		});

		rabbit.convertAndSend(queue, MessageBuilder.withBody(JsonUtils.toJsonBytes(gatewayEvent)).build());

		if (isDebugEnabled()) {
			logDebug(method, "sent to topic: %s, json: %s", queue, JsonUtils.toJson(gatewayEvent));
		}
		return event;
	}

	public void succeeded(String id, List<EventParameter> parameters) {
		signalReady(update(id, EventStatus.Succeeded, null, parameters, true));
	}

	public void receive(String id) {
		update(id, EventStatus.Received, null, null, false);
	}

	public void failed(String id, String error) {
		signalReady(update(id, EventStatus.Failed, error, null, true));
	}

	public Event update(String id, EventStatus status, String error, List<EventParameter> parameters,
			boolean clearVolatile) {
		String method = "update";
		Assert.hasLength(id, "empty id");
		Assert.notNull(status, "null status");
		Event event = eventRespository.findById(id).orElse(null);
		if (event != null) {
			event.setStatus(status);

			// append error message
			if (!StringUtils.isEmpty(error)) {
				if (!StringUtils.isEmpty(event.getError())) {
					event.setError(event.getError() + ". " + error);
				} else {
					event.setError(error);
				}
			}

			// add OUT parameters provided
			if (parameters != null) {
				if (event.typeSecured()) {
					encryptParameters(event, parameters);
				}

				for (EventParameter parameter : parameters) {
					Assert.isTrue(parameter.getType() == EventParameterType.Out, "update() only takes OUT parameters");
					event.getParameters().add(parameter);
				}
			}

			// backup parameters
			List<EventParameter> originals = event.getParameters();
			if (event.typeSecured()) {
				event.setParameters(cloneEmptyParameters(originals));
			}

			// persist changes
			eventRespository.doSave(event, null);
			logDebug(method, "updated event id: %s, status: %s, error: %s", id, status, error);

			// restore parameters
			if (event.typeSecured()) {
				event.setParameters(originals);
			}
		} else {
			logError(method, "event not found: %s", id);
		}
		return event;
	}

	public EventMonitorService getEventMonitorService() {
		return eventMonitorService;
	}

	public String getInput(Event event, String name, boolean required) {
		return getParameterValue(event, EventParameterType.In, name, required);
	}

	public String getOutput(Event event, String name, boolean required) {
		return getParameterValue(event, EventParameterType.Out, name, required);
	}

	private String getParameterValue(Event event, EventParameterType type, String name, boolean required) {
		for (EventParameter parameter : event.getParameters()) {
			if (parameter.getType() == type && parameter.getName().equals(name)) {
				if (event.getType() == EventType.Encrypted) {
					return getCryptoService().decrypt(event.getApplicationId(), parameter.getValue());
				} else {
					return parameter.getValue();
				}
			}
		}
		if (required) {
			throw new AcsSystemException("Required parameter not found: " + name);
		} else {
			return null;
		}
	}

	private void signalReady(Event event) {
		if (event != null) {
			String method = "signalReady";
			String key = event.buildRedisKey(platformConfigService.getConfig().getZoneSystemName());
			getRedis().opsForValue().set(key, JsonUtils.toJson(event), properties.getExpiration(),
					TimeUnit.MILLISECONDS);
			logDebug(method, "sent to redis key: %s, event: %s", key, event.getName());
		}
	}

	private List<EventParameter> cloneEmptyParameters(List<EventParameter> list) {
		List<EventParameter> result = new ArrayList<>(list.size());
		list.forEach(ep -> {
			result.add(new EventParameter(ep.getType(), ep.getDataType(), ep.getName(), "DELETED"));
		});
		return result;
	}

	private void encryptParameters(Event event, List<EventParameter> parameters) {
		String method = "checkAndEncryptParameters";
		if (parameters != null) {
			if (event.getType() == EventType.Encrypted) {
				logDebug(method, "encrypting parameters ...");
				parameters.forEach(parameter -> {
					parameter.setValue(getCryptoService().encrypt(event.getApplicationId(), parameter.getValue()));
				});
			}
		}
	}
}
