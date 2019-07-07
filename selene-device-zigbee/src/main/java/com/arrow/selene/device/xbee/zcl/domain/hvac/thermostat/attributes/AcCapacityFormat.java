package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AcCapacityFormat implements Attribute<StringSensorData> {
	BTUH;

	public static AcCapacityFormat getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.AC_CAPACITY_FORMAT_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
