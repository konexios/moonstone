package moonstone.selene.device.xbee.zcl.domain.general.power.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum BatterySize implements Attribute<StringSensorData> {
	NO_BATTERY((byte) 0x00),
	BUILT_IN((byte) 0x01),
	OTHER((byte) 0x02),
	AA((byte) 0x03),
	AAA((byte) 0x04),
	C((byte) 0x05),
	D((byte) 0x06),
	RESERVED((byte) 0x07, (byte) 0xfe),
	UNKNOWN((byte) 0xff);

	private final byte min;
	private final byte max;

	BatterySize(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	BatterySize(byte value) {
		min = value;
		max = value;
	}

	public static BatterySize getByValue(byte... value) {
		for (BatterySize item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.BATTERY_SIZE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
