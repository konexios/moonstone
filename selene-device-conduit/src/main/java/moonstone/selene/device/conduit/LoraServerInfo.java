package moonstone.selene.device.conduit;

import moonstone.selene.device.mqtt.MqttDeviceInfo;

public class LoraServerInfo extends MqttDeviceInfo {
	private static final long serialVersionUID = -4449392265035074750L;

	public final static String DEFAULT_DEVICE_TYPE = "lora-server";

	public LoraServerInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
