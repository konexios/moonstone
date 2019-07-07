package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum StackProfile implements Attribute<StringSensorData> {
	ZIGBEE_STACK_PROFILE((byte) 0x01),
	ZIGBEE_PRO_STACK_PROFILE((byte) 0x02);

	private final byte value;

	StackProfile(byte value) {
		this.value = value;
	}

	public static StackProfile getByValue(byte... value) {
		for (StackProfile item : values()) {
			if (item.value == value[0]) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return CommissioningClusterAttributes.STACK_PROFILE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
