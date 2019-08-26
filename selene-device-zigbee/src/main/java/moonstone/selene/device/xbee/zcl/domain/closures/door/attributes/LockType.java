package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum LockType implements Attribute<StringSensorData> {
	DEADBOLT,
	MAGNETIC,
	OTHER,
	MORTISE,
	RIM,
	LATCH_BOLT,
	CYLINDRICAL_LOCK,
	TUBULAR_LOCK,
	INTERCONNECTED_LOCK,
	DEAD_LATCH,
	DOOR_FURNITURE;

	public static LockType getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.LOCK_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
