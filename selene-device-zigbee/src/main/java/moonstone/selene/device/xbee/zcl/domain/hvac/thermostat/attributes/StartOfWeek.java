package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum StartOfWeek implements Attribute<StringSensorData> {
	SUNDAY((byte) 0x00),
	MONDAY((byte) 0x01),
	TUESDAY((byte) 0x02),
	WEDNESDAY((byte) 0x03),
	THURSDAY((byte) 0x04),
	FRIDAY((byte) 0x05),
	SATURDAY((byte) 0x06),
	RESERVED((byte) 0x07, (byte) 0xff);

	private final byte min;
	private final byte max;

	StartOfWeek(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	StartOfWeek(byte value) {
		min = value;
		max = value;
	}

	public static StartOfWeek getByValue(byte... value) {
		for (StartOfWeek item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.START_OF_WEEK_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
