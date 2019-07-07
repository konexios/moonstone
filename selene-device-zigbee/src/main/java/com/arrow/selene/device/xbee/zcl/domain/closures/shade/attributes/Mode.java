package com.arrow.selene.device.xbee.zcl.domain.closures.shade.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum Mode implements Attribute<StringSensorData> {
	NORMAL((byte) 0x00),
	CONFIGURE((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xfe),
	INVALID_OR_UNKNOWN((byte) 0xff);

	private final byte min;
	private final byte max;

	Mode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	Mode(byte value) {
		min = value;
		max = value;
	}

	public static Mode getByValue(byte... value) {
		for (Mode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return ShadeClusterAttributes.MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
