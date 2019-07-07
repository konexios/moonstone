package com.arrow.selene.device.xbee.zcl.domain.general.appliance.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class ApplianceControlClusterAttributes {
	public static final int START_TIME_ATTRIBUTE_ID = 0x0000;
	public static final int FINISH_TIME_ATTRIBUTE_ID = 0x0001;
	public static final int REMAINING_TIME_ATTRIBUTE_ID = 0x0002;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(START_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Start Time", new StartTime()));
		ALL.put(FINISH_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Finish Time", new FinishTime()));
		ALL.put(REMAINING_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Remaining Time", new RemainingTime()));
	}
}
