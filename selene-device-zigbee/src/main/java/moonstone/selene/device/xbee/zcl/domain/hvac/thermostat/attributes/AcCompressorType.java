package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum AcCompressorType implements Attribute<StringSensorData> {
	RESERVED,
	T1_MAX_WORKING_AMBIENT_43C,
	T2_MAX_WORKING_AMBIENT_35C,
	T3_MAX_WORKING_AMBIENT_52C;

	public static AcCompressorType getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.AC_REFRIGERANT_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
