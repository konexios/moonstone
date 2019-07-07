package com.arrow.selene.device.xbee.zcl.domain.hvac.dehumidification.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum RelativeHumidityDisplay implements Attribute<StringSensorData> {
	RELATIVE_HUMIDITY_NOT_DISPLAYED((byte) 0x00),
	RELATIVE_HUMIDITY_DISPLAYED((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xff);

	private final byte min;
	private final byte max;

	RelativeHumidityDisplay(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	RelativeHumidityDisplay(byte value) {
		min = value;
		max = value;
	}

	public static RelativeHumidityDisplay getByValue(byte... value) {
		for (RelativeHumidityDisplay item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return DehumidificationClusterAttributes.RELATIVE_HUMIDITY_DISPLAY_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
