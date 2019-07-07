package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import com.arrow.selene.device.sensor.BooleanSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class Occupancy implements Attribute<BooleanSensorData> {
	public static boolean isOccupied(byte... value) {
		return isOccupied(Byte.toUnsignedInt(value[0]));
	}

	public static boolean isOccupied(int value) {
		return (value & 0b0000000_1) != 0;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.OCCUPANCY_ATTRIBUTE_ID;
	}

	@Override
	public BooleanSensorData toData(String name, byte... value) {
		return new BooleanSensorData(name, isOccupied());
	}
}
