package com.arrow.selene.device.xbee.zcl.domain.measurement.levelsensing.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum LightSensorType implements Attribute<StringSensorData> {
	PHOTODIODE((byte) 0x00),
	CMOS((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0x3f),
	RESERVED_FOR_MANUFACTURER_SPECIFIC((byte) 0x40, (byte) 0xfe),
	UNKNOWN((byte) 0xff);

	private final byte min;
	private final byte max;

	LightSensorType(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	LightSensorType(byte value) {
		min = value;
		max = value;
	}

	public static LightSensorType getByValue(byte... value) {
		for (LightSensorType item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return LevelSensingMeasurementClusterAttributes.LIGHT_SENSOR_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
