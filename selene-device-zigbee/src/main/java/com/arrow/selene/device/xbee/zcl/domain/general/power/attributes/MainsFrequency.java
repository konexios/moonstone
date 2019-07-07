package com.arrow.selene.device.xbee.zcl.domain.general.power.attributes;

import com.arrow.selene.SeleneException;
import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;

public enum MainsFrequency implements Attribute<DoubleSensorData> {
	TO_LOW((byte) 0x00),
	MEASURED((byte) 0x01, (byte) 0xfd),
	TO_HIGH((byte) 0xfe),
	COULD_NOT_BE_MEASURED((byte) 0xff);

	private static final double TO_HZ_SCALE = 2.0;
	private final byte min;
	private final byte max;

	MainsFrequency(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	MainsFrequency(byte value) {
		min = value;
		max = value;
	}

	public static MainsFrequency getByValue(byte... value) {
		for (MainsFrequency item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	public static double calculate(byte... value) {
		return getByValue(value[0]) == COULD_NOT_BE_MEASURED ? Double.NaN : Byte.toUnsignedInt(value[0]) * TO_HZ_SCALE;
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.MAINS_FREQUENCY_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		if (getByValue(value) == COULD_NOT_BE_MEASURED) {
			throw new SeleneException("value cannot be measured");
		}
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
