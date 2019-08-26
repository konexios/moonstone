package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ControlSequenceOfOperation implements Attribute<StringSensorData> {
	COOLING_ONLY((byte) 0x00),
	COOLING_WITH_REHEAT((byte) 0x01),
	HEATING_ONLY((byte) 0x02),
	HEATING_WITH_REHEAT((byte) 0x03),
	COOLING_AND_HEATING_4_PIPES((byte) 0x04),
	COOLING_AND_HEATING_4_PIPES_WITH_REHEAT((byte) 0x05),
	RESERVED((byte) 0x06, (byte) 0xfe);

	private final byte min;
	private final byte max;

	ControlSequenceOfOperation(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	ControlSequenceOfOperation(byte value) {
		min = value;
		max = value;
	}

	public static ControlSequenceOfOperation getByValue(byte... value) {
		for (ControlSequenceOfOperation item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.CONTROL_SEQUENCE_OF_OPERATION_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
