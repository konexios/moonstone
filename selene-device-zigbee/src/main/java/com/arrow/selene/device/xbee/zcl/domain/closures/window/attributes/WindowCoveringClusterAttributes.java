package com.arrow.selene.device.xbee.zcl.domain.closures.window.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class WindowCoveringClusterAttributes {
	public static final int WINDOW_COVERING_TYPE_ATTRIBUTE_ID = 0x0000;
	public static final int LIMIT_LIFT_ATTRIBUTE_ID = 0x0001;
	public static final int LIMIT_TILT_ATTRIBUTE_ID = 0x0002;
	public static final int CURRENT_LIFT_ATTRIBUTE_ID = 0x0003;
	public static final int CURRENT_TILT_ATTRIBUTE_ID = 0x0004;
	public static final int NUMBER_LIFT_ATTRIBUTE_ID = 0x0005;
	public static final int NUMBER_TILT_ATTRIBUTE_ID = 0x0006;
	public static final int CONFIG_STATUS_ATTRIBUTE_ID = 0x0007;
	public static final int CURRENT_LIFT_PERCENTAGE_ATTRIBUTE_ID = 0x0008;
	public static final int CURRENT_TILT_PERCENTAGE_ATTRIBUTE_ID = 0x0009;
	public static final int OPEN_LIMIT_LIFT_ATTRIBUTE_ID = 0x0010;
	public static final int CLOSED_LIMIT_LIFT_ATTRIBUTE_ID = 0x0011;
	public static final int OPEN_LIMIT_TILT_ATTRIBUTE_ID = 0x0012;
	public static final int CLOSED_LIMIT_TILT_ATTRIBUTE_ID = 0x0013;
	public static final int VELOCITY_LIFT_ATTRIBUTE_ID = 0x0014;
	public static final int ACCELERATION_LIFT_ATTRIBUTE_ID = 0x0015;
	public static final int DECELERATION_LIFT_ATTRIBUTE_ID = 0x0016;
	public static final int MODE_ATTRIBUTE_ID = 0x0017;
	public static final int SETPOINTS_LIFT_ATTRIBUTE_ID = 0x0018;
	public static final int SETPOINTS_TILT_ATTRIBUTE_ID = 0x0019;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(WINDOW_COVERING_TYPE_ATTRIBUTE_ID,
				new ImmutablePair<>("Window Covering Type", WindowCoveringType.AWNING));
		ALL.put(LIMIT_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Limit Lift", null));
		ALL.put(LIMIT_TILT_ATTRIBUTE_ID, new ImmutablePair<>("Limit Tilt", null));
		ALL.put(CURRENT_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Current Lift", null));
		ALL.put(CURRENT_TILT_ATTRIBUTE_ID, new ImmutablePair<>("Current Tilt", null));
		ALL.put(NUMBER_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Number Lift", null));
		ALL.put(NUMBER_TILT_ATTRIBUTE_ID, new ImmutablePair<>("Number Tilt", null));
		ALL.put(CONFIG_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Config Status", ConfigStatus.DIRECTION_NORMAL));
		ALL.put(CURRENT_LIFT_PERCENTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Current Lift Percentage", null));
		ALL.put(CURRENT_TILT_PERCENTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Current Tilt Percentage", null));
		ALL.put(OPEN_LIMIT_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Open Limit Lift", null));
		ALL.put(CLOSED_LIMIT_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Closed Limit Lift", null));
		ALL.put(OPEN_LIMIT_TILT_ATTRIBUTE_ID, new ImmutablePair<>("Open Limit Tilt", null));
		ALL.put(CLOSED_LIMIT_TILT_ATTRIBUTE_ID, new ImmutablePair<>("Closed Limit Tilt", null));
		ALL.put(VELOCITY_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Velocity Lift", null));
		ALL.put(ACCELERATION_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Acceleration Lift", null));
		ALL.put(DECELERATION_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Deceleration Lift", null));
		ALL.put(MODE_ATTRIBUTE_ID, new ImmutablePair<>("Mode", Mode.LEDS_OFF));
		ALL.put(SETPOINTS_LIFT_ATTRIBUTE_ID, new ImmutablePair<>("Setpoints Lift", null));
		ALL.put(SETPOINTS_TILT_ATTRIBUTE_ID, new ImmutablePair<>("Setpoints Tilt", null));
	}
}
