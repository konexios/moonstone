package com.arrow.selene.device.xbee.zcl.domain.measurement.occupancy.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum OccupancySensorType implements Attribute<StringSensorData> {
	PIR((byte) 0x00),
	ULTRASONIC((byte) 0x01),
	PIR_AND_ULTRASONIC((byte) 0x02),
	RESERVED((byte) 0x03, (byte) 0xff);

	private final byte min;
	private final byte max;

	OccupancySensorType(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	OccupancySensorType(byte value) {
		min = value;
		max = value;
	}

	public static OccupancySensorType getByValue(byte... value) {
		for (OccupancySensorType item : values()) {
			if (value[0] >= (int) item.min && value[0] <= (int) item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return OccupancyMeasurementClusterAttributes.OCCUPANCY_SENSOR_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
