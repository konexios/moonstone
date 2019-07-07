package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum TemperatureSetpointHold implements Attribute<StringSensorData> {
	SETPOINT_HOLD_OFF((byte) 0x00),
	SETPOINT_HOLD_ON((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xff);

	private final byte min;
	private final byte max;

	TemperatureSetpointHold(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	TemperatureSetpointHold(byte value) {
		min = value;
		max = value;
	}

	public static TemperatureSetpointHold getByValue(byte... value) {
		for (TemperatureSetpointHold item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.TEMPERATURE_SETPOINT_HOLD_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
