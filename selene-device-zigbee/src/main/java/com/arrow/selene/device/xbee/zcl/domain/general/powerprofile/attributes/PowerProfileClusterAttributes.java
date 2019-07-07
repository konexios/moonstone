package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class PowerProfileClusterAttributes {
	public static final int TOTAL_PROFILE_NUM_ATTRIBUTE_ID = 0x0000;
	public static final int MULTIPLE_SCHEDULING_ATTRIBUTE_ID = 0x0001;
	public static final int ENERGY_FORMATTING_ATTRIBUTE_ID = 0x0002;
	public static final int ENERGY_REMOTE_ATTRIBUTE_ID = 0x0003;
	public static final int SCHEDULE_MODE_ATTRIBUTE_ID = 0x0004;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(TOTAL_PROFILE_NUM_ATTRIBUTE_ID, new ImmutablePair<>("Total Profile Num", null));
		ALL.put(MULTIPLE_SCHEDULING_ATTRIBUTE_ID, new ImmutablePair<>("Multiple Scheduling", null));
		ALL.put(ENERGY_FORMATTING_ATTRIBUTE_ID, new ImmutablePair<>("Energy Formatting", null));
		ALL.put(ENERGY_REMOTE_ATTRIBUTE_ID, new ImmutablePair<>("Energy Remote", new EnergyFormatting()));
		ALL.put(SCHEDULE_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Schedule Mode", ScheduleMode.SCHEDULE_MODE_CHEAPEST));
	}
}
