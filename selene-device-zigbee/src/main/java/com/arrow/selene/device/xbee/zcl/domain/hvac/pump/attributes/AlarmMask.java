package com.arrow.selene.device.xbee.zcl.domain.hvac.pump.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum AlarmMask implements Attribute<SetSensorData> {
	SUPPLY_VOLTAGE_TOO_LOW,
	SUPPLY_VOLTAGE_TOO_HIGH,
	POWER_MISSING_PHASE,
	SYSTEM_PRESSURE_TOO_LOW,
	SYSTEM_PRESSURE_TOO_HIGH,
	DRY_RUNNING,
	MOTOR_TEMPERATURE_TOO_HIGH,
	PUMP_MOTOR_HAS_FATAL_FAILURE,
	ELECTRONIC_TEMPERATURE_TOO_HIGH,
	PUMP_BLOCKED,
	SENSOR_FAILURE,
	ELECTRONIC_NON_FATAL_FAILURE,
	ELECTRONIC_FATAL_FAILURE,
	GENERAL_FAILURE;

	public static Set<AlarmMask> getByValue(byte... value) {
		Set<AlarmMask> result = EnumSet.noneOf(AlarmMask.class);
		for (AlarmMask pumpStatus : values()) {
			if ((value[0] >> pumpStatus.ordinal() & 0x01) == 1) {
				result.add(pumpStatus);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacPumpClusterAttributes.ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
