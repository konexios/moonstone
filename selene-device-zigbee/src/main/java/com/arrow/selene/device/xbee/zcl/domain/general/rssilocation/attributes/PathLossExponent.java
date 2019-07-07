package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;

public class PathLossExponent implements Attribute<DoubleSensorData> {
	private static final double SCALE = 0.01;

	public static double calculate(short value) {
		return Short.toUnsignedInt(value) * SCALE;
	}

	public static double calculate(byte[] value) {
		return calculate(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	@Override
	public int getId() {
		return RssiLocationClusterAttributes.PATH_LOSS_EXPONENT_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
