package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ZigBeeSecurityLevel implements Attribute<StringSensorData> {
	NETWORK_SECURITY,
	APS_SECURITY;

	public static ZigBeeSecurityLevel getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.ZIGBEE_SECURITY_LEVEL_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
