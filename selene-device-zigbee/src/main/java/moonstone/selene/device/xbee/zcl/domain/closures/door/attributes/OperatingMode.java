package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum OperatingMode implements Attribute<StringSensorData> {
	NORMAL,
	VACATION,
	PRIVACY,
	NO_RF_LOCK_UNLOCK,
	PASSAGE;

	public static OperatingMode getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.OPERATING_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
