package moonstone.selene.device.mqttrouter;

import moonstone.selene.device.mqtt.MqttDeviceInfo;

public class MqttRouterInfo extends MqttDeviceInfo {
	private static final long serialVersionUID = 5920408855038323345L;

	public final static String DEFAULT_DEVICE_TYPE = "mqttRouter";

	public MqttRouterInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
