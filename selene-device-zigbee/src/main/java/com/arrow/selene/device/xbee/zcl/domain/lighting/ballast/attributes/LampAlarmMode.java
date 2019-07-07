package com.arrow.selene.device.xbee.zcl.domain.lighting.ballast.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum LampAlarmMode implements Attribute<SetSensorData> {
	LAMP_BURN_HOURS;

	public static Set<LampAlarmMode> getByValue(byte... value) {
		Set<LampAlarmMode> result = EnumSet.noneOf(LampAlarmMode.class);
		for (LampAlarmMode alarmMask : values()) {
			if ((value[0] >> alarmMask.ordinal() & 0x01) == 1) {
				result.add(alarmMask);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return LightingBallastClusterAttributes.LAMP_ALARM_MODE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
