package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum ThermostatRunningState implements Attribute<SetSensorData> {
	HEAT_STATE_OFF((byte) 0, false),
	HEAT_STATE_ON((byte) 0, true),
	COOL_STATE_OFF((byte) 1, false),
	COOL_STATE_ON((byte) 1, true),
	FAN_STATE_OFF((byte) 2, false),
	FAN_STATE_ON((byte) 2, true),
	HEAT_2ND_STAGE_STATE_OFF((byte) 3, false),
	HEAT_2ND_STAGE_STATE_ON((byte) 3, true),
	COOL_2ND_STAGE_STATE_OFF((byte) 4, false),
	COOL_2ND_STAGE_STATE_ON((byte) 4, true),
	FAN_2ND_STAGE_STATE_OFF((byte) 5, false),
	FAN_2ND_STAGE_STATE_ON((byte) 5, true),
	FAN_3RD_STAGE_STATE_OFF((byte) 6, false),
	FAN_3RD_STAGE_STATE_ON((byte) 6, true);

	private final byte bit;
	private final boolean value;

	ThermostatRunningState(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<ThermostatRunningState> getByValue(byte... value) {
		Set<ThermostatRunningState> result = EnumSet.noneOf(ThermostatRunningState.class);
		for (ThermostatRunningState item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.THERMOSTAT_PROGRAMMING_OPERATION_MODE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
