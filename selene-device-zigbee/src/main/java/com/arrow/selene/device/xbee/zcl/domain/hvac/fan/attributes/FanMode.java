package com.arrow.selene.device.xbee.zcl.domain.hvac.fan.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum FanMode implements Attribute<StringSensorData> {
	OFF((byte) 0x00),
	LOW((byte) 0x01),
	MEDIUM((byte) 0x02),
	HIGH((byte) 0x03),
	ON((byte) 0x04),
	AUTO((byte) 0x05),
	SMART((byte) 0x06),
	RESERVED((byte) 0x07, (byte) 0xfe);

	private final byte min;
	private final byte max;

	FanMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	FanMode(byte value) {
		min = value;
		max = value;
	}

	public static FanMode getByValue(byte... value) {
		for (FanMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacFanClusterAttributes.FAN_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
