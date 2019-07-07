package com.arrow.selene.device.xbee.zcl.domain.general.scenes.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class ScenesClusterAttributes {
	public static final int SCENE_COUNT_ATTRIBUTE_ID = 0x0000;
	public static final int CURRENT_SCENE_ATTRIBUTE_ID = 0x0001;
	public static final int CURRENT_GROUP_ATTRIBUTE_ID = 0x0002;
	public static final int SCENE_VALID_ATTRIBUTE_ID = 0x0003;
	public static final int NAME_SUPPORT_ATTRIBUTE_ID = 0x0004;
	public static final int LAST_CONFIGURED_BY_ATTRIBUTE_ID = 0x0005;

	public static final int TRANSITION_TIME_100_MS_ATTRIBUTE_ID = 0xffff;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(SCENE_COUNT_ATTRIBUTE_ID, new ImmutablePair<>("Scene Count", null));
		ALL.put(CURRENT_SCENE_ATTRIBUTE_ID, new ImmutablePair<>("Current Scene", null));
		ALL.put(CURRENT_GROUP_ATTRIBUTE_ID, new ImmutablePair<>("Current Group", null));
		ALL.put(SCENE_VALID_ATTRIBUTE_ID, new ImmutablePair<>("Scene Valid", null));
		ALL.put(NAME_SUPPORT_ATTRIBUTE_ID, new ImmutablePair<>("Name Support", new NameSupport()));
		ALL.put(LAST_CONFIGURED_BY_ATTRIBUTE_ID, new ImmutablePair<>("Last Configured By", null));

		ALL.put(TRANSITION_TIME_100_MS_ATTRIBUTE_ID, new ImmutablePair<>("Transition Time 100ms", null));
	}
}
