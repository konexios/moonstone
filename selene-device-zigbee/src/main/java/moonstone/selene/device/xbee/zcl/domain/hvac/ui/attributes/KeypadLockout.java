package moonstone.selene.device.xbee.zcl.domain.hvac.ui.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum KeypadLockout implements Attribute<StringSensorData> {
	NO_LOCKOUT((byte) 0x00),
	LEVEL_1_LOCKOUT((byte) 0x01),
	LEVEL_2_LOCKOUT((byte) 0x02),
	LEVEL_3_LOCKOUT((byte) 0x03),
	LEVEL_4_LOCKOUT((byte) 0x04),
	LEVEL_5_LOCKOUT((byte) 0x05),
	RESERVED((byte) 0x06, (byte) 0xff);

	private final byte min;
	private final byte max;

	KeypadLockout(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	KeypadLockout(byte value) {
		min = value;
		max = value;
	}

	public static KeypadLockout getByValue(byte... value) {
		for (KeypadLockout item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacUiClusterAttributes.KEYPAD_LOCKOUT_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
