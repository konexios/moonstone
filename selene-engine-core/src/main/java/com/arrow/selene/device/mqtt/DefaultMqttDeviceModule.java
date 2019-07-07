package com.arrow.selene.device.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acs.AcsSystemException;
import com.arrow.acs.JsonUtils;
import com.arrow.selene.engine.Utils;
import com.arrow.selene.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public class DefaultMqttDeviceModule extends
		MqttDeviceModuleAbstract<DefaultMqttDeviceInfo, DefaultMqttDeviceProperties, MqttDeviceStates, DefaultMqttDeviceData> {

	private static TypeReference<Map<String, String>> mapTypeRef;

	@Override
	public void init(Properties props) {
		super.init(props);

		String mqttTopics = getProperties().getMqttTopics();
		if (StringUtils.isEmpty(mqttTopics)) {
			throw new AcsSystemException("mqttTopics is missing in configuration");
		}
		List<String> topics = new ArrayList<>();
		for (String token : mqttTopics.split(",")) {
			topics.add(token.trim());
		}
		setSubscriptionTopics(topics);
	}

	@Override
	public void processMessage(String topic, byte[] rawPayload) {
		String method = "processMessage";
		if (rawPayload == null || rawPayload.length == 0) {
			logWarn(method, "ignored empty payload");
		} else {
			DefaultMqttDeviceData data = new DefaultMqttDeviceData().withTopic(topic);
			if (getProperties().isPayloadFormatted()) {
				logDebug(method, "payload is formatted!");
				try {
					Utils.populateDeviceData(data, JsonUtils.fromJsonBytes(rawPayload, getMapTypeRef()));
					data.setParsedFully(true);
				} catch (Exception e) {
					logError(method, "error parsing formatted payload", e);
				}
			} else {
				data.withPayload(rawPayload);
			}
			getService().submit(() -> queueDataForSending(data));
		}
	}

	@Override
	public StatusModel performCommand(byte... bytes) {
		String method = "performCommand";

		logDebug(method, "command = %s", Arrays.toString(bytes));

		super.performCommand(bytes);
		Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
		mqttClient.publish(params.get("command"), params.get("payload").getBytes(StandardCharsets.UTF_8), 2);
		return StatusModel.OK;
	}

	@Override
	protected DefaultMqttDeviceProperties createProperties() {
		return new DefaultMqttDeviceProperties();
	}

	@Override
	protected DefaultMqttDeviceInfo createInfo() {
		return new DefaultMqttDeviceInfo();
	}

	@Override
	protected MqttDeviceStates createStates() {
		return new MqttDeviceStates();
	}

	private static TypeReference<Map<String, String>> getMapTypeRef() {
		return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
		});
	}
}
