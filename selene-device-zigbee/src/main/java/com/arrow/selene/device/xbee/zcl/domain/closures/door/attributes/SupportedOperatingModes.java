package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum SupportedOperatingModes implements Attribute<SetSensorData> {
	NORMAL_MODE_SUPPORTED,
	VACATION_MODE_SUPPORTED,
	PRIVACY_MODE_SUPPORTED,
	NO_RF_LOCK_OR_UNLOCK_MODE_SUPPORTED,
	PASSAGE_MODE_SUPPORTED;

	public static Set<SupportedOperatingModes> getByValue(byte... value) {
		Set<SupportedOperatingModes> result = EnumSet.noneOf(SupportedOperatingModes.class);
		for (SupportedOperatingModes item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.SUPPORTED_OPERATING_MODES_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
