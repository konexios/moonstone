package com.arrow.selene.device.xbee.zcl.domain.hvac.ui.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class HvacUiClusterAttributes {
	public static final int TEMPERATURE_DISPLAY_MODE_ATTRIBUTE_ID = 0x0000;
	public static final int KEYPAD_LOCKOUT_ATTRIBUTE_ID = 0x0001;
	public static final int SCHEDULE_PROGRAMMING_VISIBILITY_ATTRIBUTE_ID = 0x0002;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(TEMPERATURE_DISPLAY_MODE_ATTRIBUTE_ID,
				new ImmutablePair<>("Temperature Display Mode", TemperatureDisplayMode.RESERVED));
		ALL.put(KEYPAD_LOCKOUT_ATTRIBUTE_ID, new ImmutablePair<>("Keypad Lockout", KeypadLockout.RESERVED));
		ALL.put(SCHEDULE_PROGRAMMING_VISIBILITY_ATTRIBUTE_ID,
				new ImmutablePair<>("Schedule Programming Visibility", ScheduleProgrammingVisibility.RESERVED));
	}
}
