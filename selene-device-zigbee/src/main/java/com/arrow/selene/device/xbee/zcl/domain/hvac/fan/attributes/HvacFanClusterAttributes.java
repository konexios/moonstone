package com.arrow.selene.device.xbee.zcl.domain.hvac.fan.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class HvacFanClusterAttributes {
	public static final int FAN_MODE_ATTRIBUTE_ID = 0x0000;
	public static final int FAN_MODE_SEQUENCE_ATTRIBUTE_ID = 0x0001;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(FAN_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Fan Mode", FanMode.RESERVED));
		ALL.put(FAN_MODE_SEQUENCE_ATTRIBUTE_ID, new ImmutablePair<>("Fan Mode Sequence", FanModeSequence.RESERVED));
	}
}
