package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum StartupControl implements Attribute<StringSensorData> {
	CONSIDER_ITSELF_PART_OF_NETWORK,
	FORM_NETWORK,
	REJOIN_NETWORK,
	START_AND_JOIN_NETWORK;

	public static StartupControl getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return CommissioningClusterAttributes.STARTUP_CONTROL_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
