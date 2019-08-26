package moonstone.selene.device.xbee.zcl.domain.general.basic.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum PowerSource implements Attribute<StringSensorData> {
	UNKNOWN((byte) 0x00),
	MAINS_SINGLE_PHASE((byte) 0x01),
	MAINS_3_PHASE((byte) 0x02),
	BATTERY((byte) 0x03),
	DC_SOURCE((byte) 0x04),
	EMERGENCY_MAINS_CONSTANTLY_POWERED((byte) 0x05),
	EMERGENCY_MAINS_AND_TRANSFER_SWITCH((byte) 0x06),
	RESERVED((byte) 0x07, (byte) 0x7f);

	private final byte min;
	private final byte max;

	PowerSource(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	PowerSource(byte value) {
		min = value;
		max = value;
	}

	public static PowerSource getByValue(byte... value) {
		byte maskedValue = (byte) (value[0] & RESERVED.max);
		for (PowerSource item : values()) {
			if (maskedValue >= item.min && maskedValue <= item.max) {
				return item;
			}
		}
		return null;
	}

	public static boolean isSecondaryPowerSource(byte... value) {
		return (value[0] & 0b1_0000000) != 0;
	}

	@Override
	public int getId() {
		return BasicClusterAttributes.POWER_SOURCE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
