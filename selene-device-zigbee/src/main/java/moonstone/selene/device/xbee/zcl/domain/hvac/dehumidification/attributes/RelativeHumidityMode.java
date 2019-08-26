package moonstone.selene.device.xbee.zcl.domain.hvac.dehumidification.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum RelativeHumidityMode implements Attribute<StringSensorData> {
	RELATIVE_HUMIDITY_MEASURED_LOCALLY((byte) 0x00),
	RELATIVE_HUMIDITY_UPDATED_OVER_NETWORK((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xff);

	private final byte min;
	private final byte max;

	RelativeHumidityMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	RelativeHumidityMode(byte value) {
		min = value;
		max = value;
	}

	public static RelativeHumidityMode getByValue(byte... value) {
		for (RelativeHumidityMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return DehumidificationClusterAttributes.DEHUMIDIFICATION_LOCKOUT_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
