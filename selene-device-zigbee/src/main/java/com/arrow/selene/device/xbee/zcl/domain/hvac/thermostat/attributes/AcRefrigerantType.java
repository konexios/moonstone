package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AcRefrigerantType implements Attribute<StringSensorData> {
	RESERVED,
	R22,
	R401A,
	R407C;

	public static AcRefrigerantType getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.AC_REFRIGERANT_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
