package com.arrow.selene.device.mqtt;

public class DefaultMqttDeviceProperties extends MqttDeviceProperties {
	private static final long serialVersionUID = -8987490752594217175L;
	private String mqttTopics;
	private boolean payloadFormatted = false;

	public String getMqttTopics() {
		return mqttTopics;
	}

	public void setMqttTopics(String mqttTopics) {
		this.mqttTopics = mqttTopics;
	}

	public boolean isPayloadFormatted() {
		return payloadFormatted;
	}

	public void setPayloadFormatted(boolean payloadFormatted) {
		this.payloadFormatted = payloadFormatted;
	}
}
