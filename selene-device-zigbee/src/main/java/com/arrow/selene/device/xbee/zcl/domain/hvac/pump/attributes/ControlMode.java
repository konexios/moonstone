package com.arrow.selene.device.xbee.zcl.domain.hvac.pump.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum ControlMode implements Attribute<StringSensorData> {
	CONSTANT_SPEED((byte) 0x00),
	CONSTANT_PRESSURE((byte) 0x01),
	PROPORTIONAL_PRESSURE((byte) 0x02),
	CONSTANT_FLOW((byte) 0x03),
	RESERVED_4((byte) 0x04),
	CONSTANT_TEMPERATURE((byte) 0x05),
	RESERVED_6((byte) 0x06),
	AUTOMATIC((byte) 0x07),
	RESERVED((byte) 0x08, (byte) 0xfe);

	private final byte min;
	private final byte max;

	ControlMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	ControlMode(byte value) {
		min = value;
		max = value;
	}

	public static ControlMode getByValue(byte... value) {
		for (ControlMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacPumpClusterAttributes.CONTROL_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
