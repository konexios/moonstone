package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ThermostatRunningMode implements Attribute<StringSensorData> {
	OFF((byte) 0x00),
	RESERVED_1_2((byte) 0x01, (byte) 0x02),
	COOL((byte) 0x03),
	HEAT((byte) 0x04),
	RESERVED((byte) 0x05, (byte) 0xfe);

	private final byte min;
	private final byte max;

	ThermostatRunningMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	ThermostatRunningMode(byte value) {
		min = value;
		max = value;
	}

	public static ThermostatRunningMode getByValue(byte... value) {
		for (ThermostatRunningMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.THERMOSTAT_RUNNING_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
