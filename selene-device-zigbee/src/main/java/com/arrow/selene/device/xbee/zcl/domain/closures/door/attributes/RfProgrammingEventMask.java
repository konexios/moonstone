package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum RfProgrammingEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_RF_PROGRAMMING_EVENT,
	RESERVED,
	PIN_ADDED_SOURCE_RF,
	PIN_DELETED_SOURCE_RF,
	PIN_CHANGED_SOURCE_RF;

	public static Set<RfProgrammingEventMask> getByValue(byte... value) {
		Set<RfProgrammingEventMask> result = EnumSet.noneOf(RfProgrammingEventMask.class);
		for (RfProgrammingEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.RF_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID;
	}

	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
