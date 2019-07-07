package com.arrow.selene.device.xbee.zcl.domain.general.power.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum BatteryAlarmState implements Attribute<SetSensorData> {
	BATTERY_1_MIN_THRESHOLD_REACHED,
	BATTERY_1_THRESHOLD_1_REACHED,
	BATTERY_1_THRESHOLD_2_REACHED,
	BATTERY_1_THRESHOLD_3_REACHED,
	RESERVED_4,
	RESERVED_5,
	RESERVED_6,
	RESERVED_7,
	RESERVED_8,
	RESERVED_9,
	BATTERY_2_MIN_THRESHOLD_REACHED,
	BATTERY_2_THRESHOLD_1_REACHED,
	BATTERY_2_THRESHOLD_2_REACHED,
	BATTERY_2_THRESHOLD_3_REACHED,
	RESERVED_14,
	RESERVED_15,
	RESERVED_16,
	RESERVED_17,
	RESERVED_18,
	RESERVED_19,
	BATTERY_3_MIN_THRESHOLD_REACHED,
	BATTERY_3_THRESHOLD_1_REACHED,
	BATTERY_3_THRESHOLD_2_REACHED,
	BATTERY_3_THRESHOLD_3_REACHED,
	RESERVED_24,
	RESERVED_25,
	RESERVED_26,
	RESERVED_27,
	RESERVED_28,
	RESERVED_29,
	RESERVED_30,
	RESERVED_31;

	public static Set<BatteryAlarmState> getByValue(byte... value) {
		Set<BatteryAlarmState> result = EnumSet.noneOf(BatteryAlarmState.class);
		for (BatteryAlarmState item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.BATTERY_ALARM_STATE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
