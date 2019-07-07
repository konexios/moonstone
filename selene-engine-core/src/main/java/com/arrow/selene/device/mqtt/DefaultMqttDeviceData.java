package com.arrow.selene.device.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.DeviceDataAbstract;

public class DefaultMqttDeviceData extends DeviceDataAbstract implements MqttDeviceData {

	private String topic;
	private byte[] payload;

	public DefaultMqttDeviceData withTopic(String topic) {
		setTopic(topic);
		return this;
	}

	public DefaultMqttDeviceData withPayload(byte[] payload) {
		setPayload(payload);
		return this;
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = getParsedIotParameters();
		if (result == null) {
			result = new IotParameters();
		}
		if (!isParsedFully()) {
			if (!StringUtils.isEmpty(topic)) {
				result.setString("topic", topic);
			}
			if (payload != null && payload.length > 0) {
				result.setString("payload", new String(payload, StandardCharsets.UTF_8));
			}
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = getParsedTelemetries();
		if (result == null) {
			result = new ArrayList<>();
		}
		if (!isParsedFully()) {
			if (!StringUtils.isEmpty(topic)) {
				result.add(writeStringTelemetry(TelemetryItemType.String, "topic", topic));
			}
			if (payload != null && payload.length > 0) {
				result.add(writeStringTelemetry(TelemetryItemType.String, "payload",
				        new String(payload, StandardCharsets.UTF_8)));
			}
		}
		return result;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
