package com.arrow.selene.device.xbee.zcl.domain.general.power.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;

public class BatteryAHrRating implements Attribute<DoubleSensorData> {
	private static final double TO_AHR_SCALE = 0.1;

	public static double calculate(short value) {
		return Short.toUnsignedInt(value) * TO_AHR_SCALE;
	}

	public static double calculate(byte[] value) {
		return calculate(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.BATTERY_A_HR_RATING_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
