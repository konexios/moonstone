package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AcErrorCode implements Attribute<SetSensorData> {
	COMPRESSOR_FAILURE_OR_REFRIGERANT_LEAKAGE,
	ROOM_TEMPERATURE_SENSOR_FAILURE,
	OUTDOOR_TEMPERATURE_SENSOR_FAILURE,
	INDOOR_COIL_TEMPERATURE_SENSOR_FAILURE,
	FAN_FAILURE;

	public static Set<AcErrorCode> getByValue(byte... value) {
		Set<AcErrorCode> result = EnumSet.noneOf(AcErrorCode.class);
		for (AcErrorCode item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.AC_ERROR_CODE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
