package com.arrow.selene.device.xbee.zcl.domain.general.switchconfig.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum SwitchActions implements Attribute<StringSensorData> {
	ON_OFF((byte) 0x00),
	OFF_ON((byte) 0x01),
	TOGGLE((byte) 0x02),
	RESERVED((byte) 0x03, (byte) 0xff);

	private final byte min;
	private final byte max;

	SwitchActions(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	SwitchActions(byte value) {
		min = value;
		max = value;
	}

	public static SwitchActions getByValue(byte... value) {
		for (SwitchActions item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return SwitchConfigClusterAttributes.SWITCH_ACTIONS_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
