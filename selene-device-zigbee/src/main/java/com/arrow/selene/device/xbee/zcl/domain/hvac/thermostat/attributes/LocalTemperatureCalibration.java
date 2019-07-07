package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;

public class LocalTemperatureCalibration implements Attribute<DoubleSensorData> {
	private static final double TO_CELSIUS_SCALE = 0.1;

	public static double calculate(byte... value) {
		return value[0] * TO_CELSIUS_SCALE;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.LOCAL_TEMPERATURE_CALIBRATION_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
