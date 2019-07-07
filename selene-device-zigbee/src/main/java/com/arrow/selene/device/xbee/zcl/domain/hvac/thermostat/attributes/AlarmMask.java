package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AlarmMask implements Attribute<SetSensorData> {
	INITIALIZATION_FAILURE,
	HARDWARE_FAILURE,
	SELF_CALIBRATION_FAILURE;

	public static Set<AlarmMask> getByValue(byte... value) {
		Set<AlarmMask> result = EnumSet.noneOf(AlarmMask.class);
		for (AlarmMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.THERMOSTAT_ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
