package moonstone.selene.device.xbee.zcl.domain.general.switchconfig.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum SwitchType implements Attribute<StringSensorData> {
	TOGGLE((byte) 0x00),
	MOMENTARY((byte) 0x01),
	MULTI_FUNCTION((byte) 0x02),
	RESERVED((byte) 0x03, (byte) 0xff);

	private final byte min;
	private final byte max;

	SwitchType(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	SwitchType(byte value) {
		min = value;
		max = value;
	}

	public static SwitchType getByValue(byte... value) {
		for (SwitchType item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return SwitchConfigClusterAttributes.SWITCH_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
