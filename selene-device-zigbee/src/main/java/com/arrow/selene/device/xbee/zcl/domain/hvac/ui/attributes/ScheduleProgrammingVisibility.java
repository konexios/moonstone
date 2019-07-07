package com.arrow.selene.device.xbee.zcl.domain.hvac.ui.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum ScheduleProgrammingVisibility implements Attribute<StringSensorData> {
	LOCAL_SCHEDULE_PROGRAMMING_FUNCTIONALITY_ENABLED((byte) 0x00),
	LOCAL_SCHEDULE_PROGRAMMING_FUNCTIONALITY_DISABLED((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xff);

	private final byte min;
	private final byte max;

	ScheduleProgrammingVisibility(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	ScheduleProgrammingVisibility(byte value) {
		min = value;
		max = value;
	}

	public static ScheduleProgrammingVisibility getByValue(byte... value) {
		for (ScheduleProgrammingVisibility item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return HvacUiClusterAttributes.SCHEDULE_PROGRAMMING_VISIBILITY_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
