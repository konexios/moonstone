package com.arrow.selene.device.xbee.zcl.domain.measurement.levelsensing.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.SeleneException;
import com.arrow.selene.device.sensor.DoubleSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.engine.EngineConstants;

public enum IlluminanceTargetLevel implements Attribute<DoubleSensorData> {
	TO_LOW((short) 0x0000),
	MEASURED((short) 0x0001, (short) 0xfffe),
	INVALID((short) 0xffff);

	private final short min;
	private final short max;

	IlluminanceTargetLevel(short min, short max) {
		this.min = min;
		this.max = max;
	}

	IlluminanceTargetLevel(short value) {
		min = value;
		max = value;
	}

	public static IlluminanceTargetLevel getByValue(short value) {
		for (IlluminanceTargetLevel item : values()) {
			if (value >= item.min && value <= item.max) {
				return item;
			}
		}
		return null;
	}

	public static IlluminanceTargetLevel getByValue(byte[] value) {
		return getByValue(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	public static double calculate(short value) {
		return getByValue(value) == INVALID ? Double.NaN : Math.pow(10.0, (Short.toUnsignedInt(value) - 1.0) / 10000.0);
	}

	public static double calculate(byte... value) {
		return calculate(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	@Override
	public int getId() {
		return LevelSensingMeasurementClusterAttributes.ILLUMINANCE_TARGET_LEVEL_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		if (getByValue(value) == INVALID) {
			throw new SeleneException("value is invalid");
		}
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
