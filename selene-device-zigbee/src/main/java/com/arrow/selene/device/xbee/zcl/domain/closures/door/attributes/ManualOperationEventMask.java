package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum ManualOperationEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_MANUAL_OPERATION_EVENT,
	THUMBTURN_LOCK,
	THUMBTURN_UNLOCK,
	ONE_TOUCH_LOCK,
	KEY_LOCK,
	KEY_UNLOCK,
	AUTO_LOCK,
	SCHEDULE_LOCK,
	SCHEDULE_UNLOCK,
	MANUAL_LOCK_KEY_OR_THUMBTURN,
	MANUAL_UNLOCK_KEY_OR_THUMBTURN;

	public static Set<ManualOperationEventMask> getByValue(byte... value) {
		Set<ManualOperationEventMask> result = EnumSet.noneOf(ManualOperationEventMask.class);
		for (ManualOperationEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.MANUAL_OPERATION_EVENT_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
