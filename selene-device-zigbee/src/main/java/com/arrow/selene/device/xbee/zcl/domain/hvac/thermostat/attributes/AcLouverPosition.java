package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AcLouverPosition implements Attribute<StringSensorData> {
	RESERVED_0((byte) 0x00),
	FULLY_CLOSED((byte) 0x01),
	FULLY_OPEN((byte) 0x02),
	QUARTER_OPEN((byte) 0x03),
	HALF_OPEN((byte) 0x04),
	THREE_QUARTERS_OPEN((byte) 0x05),
	RESERVED((byte) 0x06, (byte) 0xff);

	private final byte min;
	private final byte max;

	AcLouverPosition(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	AcLouverPosition(byte value) {
		min = value;
		max = value;
	}

	public static AcLouverPosition getByValue(byte... value) {
		for (AcLouverPosition item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.AC_LOUVER_POSITION_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
