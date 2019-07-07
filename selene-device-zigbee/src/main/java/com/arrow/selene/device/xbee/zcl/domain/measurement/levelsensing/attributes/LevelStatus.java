package com.arrow.selene.device.xbee.zcl.domain.measurement.levelsensing.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum LevelStatus implements Attribute<StringSensorData> {
	ILLUMINANCE_ON_TARGET((byte) 0x00),
	ILLUMINANCE_BELOW_TARGET((byte) 0x01),
	ILLUMINANCE_ABOVE_TARGET((byte) 0x02),
	RESERVED((byte) 0x03, (byte) 0xff);

	private final byte min;
	private final byte max;

	LevelStatus(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	LevelStatus(byte value) {
		min = value;
		max = value;
	}

	public static LevelStatus getByValue(byte... value) {
		for (LevelStatus item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return LevelSensingMeasurementClusterAttributes.LEVEL_STATUS_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
