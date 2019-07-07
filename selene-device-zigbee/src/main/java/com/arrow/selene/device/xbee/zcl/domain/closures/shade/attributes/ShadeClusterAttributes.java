package com.arrow.selene.device.xbee.zcl.domain.closures.shade.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class ShadeClusterAttributes {
	public static final int PHYSICAL_CLOSED_LIMIT_ATTRIBUTE_ID = 0x0000;
	public static final int MOTOR_STEP_SIZE_ATTRIBUTE_ID = 0x0001;
	public static final int STATUS_ATTRIBUTE_ID = 0x0002;

	public static final int CLOSED_LIMIT_ATTRIBUTE_ID = 0x0010;
	public static final int MODE_ATTRIBUTE_ID = 0x0011;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(PHYSICAL_CLOSED_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Physical Closed Limit", null));
		ALL.put(MOTOR_STEP_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Motor Step Size", new MotorStepSize()));
		ALL.put(STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Status", Status.SHADE_CLOSING));
		ALL.put(CLOSED_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Closed Limit", null));
		ALL.put(MODE_ATTRIBUTE_ID, new ImmutablePair<>("Mode", Mode.RESERVED));
	}
}
