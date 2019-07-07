package com.arrow.selene.device.xbee.zcl.domain.measurement.humidity.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class HumidityMeasurementClusterAttributes {
	public static final int MEASURED_VALUE_ATTRIBUTE_ID = 0x0000;
	public static final int MIN_MEASURED_VALUE_ATTRIBUTE_ID = 0x0001;
	public static final int MAX_MEASURED_VALUE_ATTRIBUTE_ID = 0x0002;
	public static final int TOLERANCE_ATTRIBUTE_ID = 0x0003;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Measured Value", new MeasuredValue()));
		ALL.put(MIN_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Min Measured Value", new MeasuredValue()));
		ALL.put(MAX_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Max Measured Value", new MeasuredValue()));
		ALL.put(TOLERANCE_ATTRIBUTE_ID, new ImmutablePair<>("Tolerance", null));
	}
}
