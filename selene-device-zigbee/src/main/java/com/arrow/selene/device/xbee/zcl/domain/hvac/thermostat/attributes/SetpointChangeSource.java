package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum SetpointChangeSource implements Attribute<StringSensorData> {
	MANUAL((byte) 0x00),
	SCHEDULE_INTERNAL((byte) 0x01),
	EXTERNALLY_INITIATED((byte) 0x02),
	RESERVED((byte) 0x03, (byte) 0xff);

	private final byte min;
	private final byte max;

	SetpointChangeSource(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	SetpointChangeSource(byte value) {
		min = value;
		max = value;
	}

	public static SetpointChangeSource getByValue(byte... value) {
		for (SetpointChangeSource item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.SETPOINT_CHANGE_SOURCE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
