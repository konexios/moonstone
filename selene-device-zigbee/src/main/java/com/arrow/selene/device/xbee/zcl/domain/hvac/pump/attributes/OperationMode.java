package com.arrow.selene.device.xbee.zcl.domain.hvac.pump.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum OperationMode implements Attribute<StringSensorData> {
	NORMAL((byte) 0x00),
	MINIMUM((byte) 0x01),
	MAXIMUM((byte) 0x02),
	LOCAL((byte) 0x03),
	RESERVED((byte) 0x04, (byte) 0xfe);

	private final byte min;
	private final byte max;

	OperationMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	OperationMode(byte value) {
		min = value;
		max = value;
	}

	public static OperationMode getByValue(byte... value) {
		for (OperationMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacPumpClusterAttributes.OPERATION_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
