package com.arrow.selene.device.xbee.zcl.domain.general.power.attributes;

import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;

public class BatteryVoltageMinThreshold implements Attribute<DoubleSensorData> {
	private static final double TO_VOLTS_SCALE = 0.1;

	public static double calculate(byte... value) {
		return Byte.toUnsignedInt(value[0]) * TO_VOLTS_SCALE;
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.BATTERY_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
