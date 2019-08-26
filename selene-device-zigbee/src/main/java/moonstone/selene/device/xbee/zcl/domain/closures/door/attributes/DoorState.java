package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum DoorState implements Attribute<StringSensorData> {
	OPEN((byte) 0x00),
	CLOSED((byte) 0x01),
	ERROR_JAMMED((byte) 0x02),
	ERROR_FORCED_OPEN((byte) 0x03),
	ERROR_UNSPECIFIED((byte) 0x04),
	RESERVED((byte) 0x05, (byte) 0xfe),
	NOT_DEFINED((byte) 0xff);

	private final byte min;
	private final byte max;

	DoorState(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	DoorState(byte value) {
		min = value;
		max = value;
	}

	public static DoorState getByValue(byte... value) {
		for (DoorState item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.DOOR_STATE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
