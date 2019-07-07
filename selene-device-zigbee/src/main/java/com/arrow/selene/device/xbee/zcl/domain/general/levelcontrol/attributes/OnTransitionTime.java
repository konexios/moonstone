package com.arrow.selene.device.xbee.zcl.domain.general.levelcontrol.attributes;

import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;
import com.digi.xbee.api.utils.ByteUtils;

public class OnTransitionTime implements Attribute<DoubleSensorData> {
	private static final double TO_SEC_SCALE = 0.1;

	public static double calculate(int value) {
		return value * TO_SEC_SCALE;
	}

	public static double calculate(byte... value) {
		return calculate(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)));
	}

	@Override
	public int getId() {
		return LevelControlClusterAttributes.ON_TRANSITION_TIME_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
