package com.arrow.selene.device.xbee.zcl.domain.ha.measurement.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AcAlarmsMask implements Attribute<SetSensorData> {
	VOLTAGE_OVERLOAD,
	CURRENT_OVERLOAD,
	ACTIVE_POWER_OVERLOAD,
	REACTIVE_POWER_OVERLOAD,
	AVERAGE_RMS_OVER_VOLTAGE,
	AVERAGE_RMS_UNDER_VOLTAGE,
	RMS_EXTREME_OVER_VOLTAGE,
	RMS_EXTREME_UNDER_VOLTAGE,
	RMS_VOLTAGE_SAG,
	RMS_VOLTAGE_SWELL;

	public static Set<AcAlarmsMask> getByValue(byte... value) {
		Set<AcAlarmsMask> result = EnumSet.noneOf(AcAlarmsMask.class);
		for (AcAlarmsMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return ElectricalMeasurementClusterAttributes.AC_ALARMS_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
