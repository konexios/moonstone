package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum LockState implements Attribute<StringSensorData> {
	NOT_FULLY_LOCKED,
	LOCKED,
	UNLOCKED;

	public static LockState getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.LOCK_STATE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
