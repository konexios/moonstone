package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum DoorLockAlarmMask implements Attribute<SetSensorData> {
	DEADBOLT_JAMMED,
	LOCK_RESET_TO_FACTORY_DEFAULTS,
	RESERVED,
	RF_MODULE_POWER_CYCLED,
	TAMPER_ALARM_WRONG_CODE_ENTRY_LIMIT,
	TAMPER_ALARM_WRONG_FRONT_ESCUTCHEON_REMOVED_FROM_MAIN,
	FORCED_DOOR_OPEN_UNDER_DOOR_LOCKED_CONDITION;

	public static Set<DoorLockAlarmMask> getByValue(byte... value) {
		Set<DoorLockAlarmMask> result = EnumSet.noneOf(DoorLockAlarmMask.class);
		for (DoorLockAlarmMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.DOOR_LOCK_ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
