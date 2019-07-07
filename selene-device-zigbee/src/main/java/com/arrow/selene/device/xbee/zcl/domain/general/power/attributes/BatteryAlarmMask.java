package com.arrow.selene.device.xbee.zcl.domain.general.power.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum BatteryAlarmMask implements Attribute<SetSensorData> {
	BATTERY_VOLTAGE_TOO_LOW,
	BATTERY_ALARM_1,
	BATTERY_ALARM_2,
	BATTERY_ALARM_3;

	public static Set<BatteryAlarmMask> getByValue(byte... value) {
		Set<BatteryAlarmMask> result = EnumSet.noneOf(BatteryAlarmMask.class);
		for (BatteryAlarmMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.BATTERY_ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
