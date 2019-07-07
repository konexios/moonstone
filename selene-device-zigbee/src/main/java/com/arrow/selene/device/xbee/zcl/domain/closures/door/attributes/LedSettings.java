package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum LedSettings implements Attribute<StringSensorData> {
	NEVER_USE_LED_FOR_SIGNALIZATION,
	USE_LED_SIGNALIZATION_EXCEPT_FOR_ACCESS_ALLOWED_EVENTS,
	USE_LED_SIGNALIZATION_FOR_ALL_EVENTS;

	public static LedSettings getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.LED_SETTINGS_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
