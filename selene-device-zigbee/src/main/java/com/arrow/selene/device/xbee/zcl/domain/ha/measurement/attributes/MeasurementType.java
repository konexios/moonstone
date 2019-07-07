package com.arrow.selene.device.xbee.zcl.domain.ha.measurement.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum MeasurementType implements Attribute<SetSensorData> {
	ACTIVE_MEASUREMENT_AC,
	REACTIVE_MEASUREMENT_AC,
	APPARENT_MEASUREMENT_AC,
	PHASE_A_MEASUREMENT,
	PHASE_B_MEASUREMENT,
	PHASE_C_MEASUREMENT,
	DC_MEASUREMENT,
	HARMONICS_MEASUREMENT,
	POWER_QUALITY_MEASUREMENT;

	public static Set<MeasurementType> getByValue(byte... value) {
		Set<MeasurementType> result = EnumSet.noneOf(MeasurementType.class);
		for (MeasurementType item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return ElectricalMeasurementClusterAttributes.MEASUREMENT_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
