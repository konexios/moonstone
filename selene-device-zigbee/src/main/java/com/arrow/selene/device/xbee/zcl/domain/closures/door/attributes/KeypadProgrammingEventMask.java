package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum KeypadProgrammingEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_KEYPAD_PROGRAMMING_EVENT,
	MASTER_CODE_CHANGED_SOURCE_KEYPAD,
	PIN_ADDED_SOURCE_KEYPAD,
	PIN_DELETED_SOURCE_KEYPAD,
	PIN_CHANGED_SOURCE_KEYPAD;

	public static Set<KeypadProgrammingEventMask> getByValue(byte... value) {
		Set<KeypadProgrammingEventMask> result = EnumSet.noneOf(KeypadProgrammingEventMask.class);
		for (KeypadProgrammingEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.KEYPAD_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
