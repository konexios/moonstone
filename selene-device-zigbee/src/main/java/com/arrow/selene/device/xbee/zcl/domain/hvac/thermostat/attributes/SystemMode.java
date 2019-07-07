package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum SystemMode implements Attribute<StringSensorData> {
	OFF((byte) 0x00),
	AUTO((byte) 0x01),
	RESERVED_2((byte) 0x02),
	COOL((byte) 0x03),
	HEAT((byte) 0x04),
	EMERGENCY_HEATING((byte) 0x05),
	PRECOOLING((byte) 0x06),
	FAN_ONLY((byte) 0x07),
	DRY((byte) 0x08),
	SLEEP((byte) 0x09),
	RESERVED((byte) 0x0a, (byte) 0xfe);

	private final byte min;
	private final byte max;

	SystemMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	SystemMode(byte value) {
		min = value;
		max = value;
	}

	public static SystemMode getByValue(byte... value) {
		for (SystemMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.SYSTEM_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
