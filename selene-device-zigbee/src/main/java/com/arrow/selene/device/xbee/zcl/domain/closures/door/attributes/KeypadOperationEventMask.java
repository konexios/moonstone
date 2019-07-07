package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum KeypadOperationEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_KEYPAD_OPERATION_EVENT,
	LOCK_SOURCE_KEYPAD,
	UNLOCK_SOURCE_KEYPAD,
	LOCK_SOURCE_KEYPAD_ERROR_INVALID_PIN,
	LOCK_SOURCE_KEYPAD_ERROR_INVALID_SCHEDULE,
	UNLOCK_SOURCE_KEYPAD_ERROR_INVALID_CODE,
	UNLOCK_SOURCE_KEYPAD_ERROR_INVALID_SCHEDULE,
	NON_ACCESS_USER_OPERATION_EVENT_SOURCE_KEYPAD;

	public static Set<KeypadOperationEventMask> getByValue(byte... value) {
		Set<KeypadOperationEventMask> result = EnumSet.noneOf(KeypadOperationEventMask.class);
		for (KeypadOperationEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.KEYPAD_OPERATION_EVENT_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
