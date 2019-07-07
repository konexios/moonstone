package com.arrow.selene.device.xbee.zcl.domain.measurement.levelsensing.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class LevelSensingMeasurementClusterAttributes {
	public static final int LEVEL_STATUS_ATTRIBUTE_ID = 0x0000;
	public static final int LIGHT_SENSOR_TYPE_ATTRIBUTE_ID = 0x0001;

	public static final int ILLUMINANCE_TARGET_LEVEL_ATTRIBUTE_ID = 0x0010;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(LEVEL_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Level Status", LevelStatus.RESERVED));
		ALL.put(LIGHT_SENSOR_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Light Sensor Type", LightSensorType.RESERVED));
		ALL.put(ILLUMINANCE_TARGET_LEVEL_ATTRIBUTE_ID,
				new ImmutablePair<>("Illuminance Target Level", IlluminanceTargetLevel.INVALID));
	}
}
