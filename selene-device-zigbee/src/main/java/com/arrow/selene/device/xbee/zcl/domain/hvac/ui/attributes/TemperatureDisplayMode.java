package com.arrow.selene.device.xbee.zcl.domain.hvac.ui.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum TemperatureDisplayMode implements Attribute<StringSensorData> {
	TEMPERATURE_IN_CELSIUS((byte) 0x00),
	TEMPERATURE_IN_FAHRENHEITS((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xff);

	private final byte min;
	private final byte max;

	TemperatureDisplayMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	TemperatureDisplayMode(byte value) {
		min = value;
		max = value;
	}

	public static TemperatureDisplayMode getByValue(byte... value) {
		for (TemperatureDisplayMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacUiClusterAttributes.KEYPAD_LOCKOUT_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
