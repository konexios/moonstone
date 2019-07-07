package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AcType implements Attribute<StringSensorData> {
	RESERVED,
	COOLING_AND_FIXED_SPEED,
	HEAT_PUMP_AND_FIXED_SPEED,
	COOLING_AND_INVERTER,
	HEAT_PUMP_AND_INVERTER;

	public static AcType getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.AC_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
