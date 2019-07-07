package com.arrow.selene.device.xbee.zcl.domain.general.temperature.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum DeviceTempAlarmMask implements Attribute<SetSensorData> {
	DEVICE_TEMP_TOO_LOW,
	DEVICE_TEMP_TOO_HIGH;

	public static Set<DeviceTempAlarmMask> getByValue(byte... value) {
		Set<DeviceTempAlarmMask> result = EnumSet.noneOf(DeviceTempAlarmMask.class);
		for (DeviceTempAlarmMask alarmMask : values()) {
			if ((value[0] >> alarmMask.ordinal() & 0x01) == 1) {
				result.add(alarmMask);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return TemperatureClusterAttributes.DEVICE_TEMP_ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
