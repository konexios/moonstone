package moonstone.selene.device.mqtt;

public class DefaultMqttDeviceInfo extends MqttDeviceInfo {
	private static final long serialVersionUID = -4817845459510232961L;

	public final static String DEFAULT_DEVICE_TYPE = "default-mqtt-device";

	public DefaultMqttDeviceInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
