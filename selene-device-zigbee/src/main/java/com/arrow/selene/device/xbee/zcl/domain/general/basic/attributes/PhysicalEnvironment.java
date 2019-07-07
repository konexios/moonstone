package com.arrow.selene.device.xbee.zcl.domain.general.basic.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum PhysicalEnvironment implements Attribute<StringSensorData> {
	UNSPECIFIED_ENVIRONMENT((byte) 0x00),
	SPECIFIED_PER_PROFILE((byte) 0x01, (byte) 0x7f),
	RESERVED((byte) 0x80, (byte) 0xfe),
	UNKNOWN_ENVIRONMENT((byte) 0xff);

	private final byte min;
	private final byte max;

	PhysicalEnvironment(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	PhysicalEnvironment(byte value) {
		min = value;
		max = value;
	}

	public static PhysicalEnvironment getByValue(byte... value) {
		for (PhysicalEnvironment item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return BasicClusterAttributes.PHYSICAL_ENVIRONMENT_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
