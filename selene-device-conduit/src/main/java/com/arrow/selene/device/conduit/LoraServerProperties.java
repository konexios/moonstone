package com.arrow.selene.device.conduit;

import com.arrow.selene.device.mqtt.MqttDeviceProperties;

public class LoraServerProperties extends MqttDeviceProperties {
	private static final long serialVersionUID = 8543373863728645292L;

	private String mqttTopics;

	private String loraDeviceType = LoraDeviceInfo.DEFAULT_DEVICE_TYPE;
	private String loraDeviceClass = LoraDeviceModuleImpl.class.getName();

	public String getMqttTopics() {
		return mqttTopics;
	}

	public void setMqttTopics(String mqttTopics) {
		this.mqttTopics = mqttTopics;
	}

	public String getLoraDeviceType() {
		return loraDeviceType;
	}

	public void setLoraDeviceType(String loraDeviceType) {
		this.loraDeviceType = loraDeviceType;
	}

	public String getLoraDeviceClass() {
		return loraDeviceClass;
	}

	public void setLoraDeviceClass(String loraDeviceClass) {
		this.loraDeviceClass = loraDeviceClass;
	}
}
