package com.arrow.selene.device.xbee.zcl.domain.general.pollcontrol.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class PollControlClusterAttributes {
	public static final int CHECK_IN_INTERVAL_ATTRIBUTE_ID = 0x0000;
	public static final int LONG_POLL_INTERVAL_ATTRIBUTE_ID = 0x0001;
	public static final int SHORT_POLL_INTERVAL_ATTRIBUTE_ID = 0x0002;
	public static final int FAST_POLL_TIMEOUT_ATTRIBUTE_ID = 0x0003;
	public static final int CHECK_IN_INTERVAL_MIN_ATTRIBUTE_ID = 0x0004;
	public static final int LONG_POLL_INTERVAL_MIN_ATTRIBUTE_ID = 0x0005;
	public static final int FAST_POLL_TIMEOUT_MAX_ATTRIBUTE_ID = 0x0006;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(CHECK_IN_INTERVAL_ATTRIBUTE_ID, new ImmutablePair<>("Check In Interval", null));
		ALL.put(LONG_POLL_INTERVAL_ATTRIBUTE_ID, new ImmutablePair<>("Long Poll Interval", null));
		ALL.put(SHORT_POLL_INTERVAL_ATTRIBUTE_ID, new ImmutablePair<>("Short Poll Interval", null));
		ALL.put(FAST_POLL_TIMEOUT_ATTRIBUTE_ID, new ImmutablePair<>("Fast Poll Timeout", null));
		ALL.put(CHECK_IN_INTERVAL_MIN_ATTRIBUTE_ID, new ImmutablePair<>("Check In Interval Min", null));
		ALL.put(LONG_POLL_INTERVAL_MIN_ATTRIBUTE_ID, new ImmutablePair<>("Long Poll Interval Min", null));
		ALL.put(FAST_POLL_TIMEOUT_MAX_ATTRIBUTE_ID, new ImmutablePair<>("Fast Poll Timeout Max", null));
	}
}
