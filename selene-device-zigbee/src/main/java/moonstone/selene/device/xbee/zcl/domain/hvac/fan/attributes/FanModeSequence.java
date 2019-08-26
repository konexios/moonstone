package moonstone.selene.device.xbee.zcl.domain.hvac.fan.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum FanModeSequence implements Attribute<StringSensorData> {
	LOW_MED_HIGH((byte) 0x00),
	LOW_HIGH((byte) 0x01),
	LOW_MED_HIGH_AUTO((byte) 0x02),
	LOW_HIGH_AUTO((byte) 0x03),
	ON_AUTO((byte) 0x04),
	RESERVED((byte) 0x05, (byte) 0xfe);

	private final byte min;
	private final byte max;

	FanModeSequence(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	FanModeSequence(byte value) {
		min = value;
		max = value;
	}

	public static FanModeSequence getByValue(byte... value) {
		for (FanModeSequence item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacFanClusterAttributes.FAN_MODE_SEQUENCE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
