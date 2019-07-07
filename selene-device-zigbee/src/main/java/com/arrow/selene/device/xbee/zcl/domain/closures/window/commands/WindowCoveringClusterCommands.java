package com.arrow.selene.device.xbee.zcl.domain.closures.window.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class WindowCoveringClusterCommands {
	public static final int WINDOW_COVERING_UP_OPEN_COMMAND_ID = 0x00;
	public static final int WINDOW_COVERING_DOWN_CLOSE_COMMAND_ID = 0x01;
	public static final int WINDOW_COVERING_STOP_COMMAND_ID = 0x02;
	public static final int WINDOW_COVERING_GO_TO_LIFT_VALUE_COMMAND_ID = 0x04;
	public static final int WINDOW_COVERING_GO_TO_LIFT_PERCENTAGE_COMMAND_ID = 0x05;
	public static final int WINDOW_COVERING_GO_TO_TILT_VALUE_COMMAND_ID = 0x07;
	public static final int WINDOW_COVERING_GO_TO_TILT_PERCENTAGE_COMMAND_ID = 0x08;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(WINDOW_COVERING_UP_OPEN_COMMAND_ID,
				new ImmutablePair<>("Window Covering Up Open", UpOpen.class));
		ALL_RECEIVED.put(WINDOW_COVERING_DOWN_CLOSE_COMMAND_ID,
				new ImmutablePair<>("Window Covering Down Close", DownClose.class));
		ALL_RECEIVED.put(WINDOW_COVERING_STOP_COMMAND_ID, new ImmutablePair<>("Window Covering Stop", Stop.class));
		ALL_RECEIVED.put(WINDOW_COVERING_GO_TO_LIFT_VALUE_COMMAND_ID,
				new ImmutablePair<>("Lift ValueWindow Covering Go To ", GoToLiftValue.class));
		ALL_RECEIVED.put(WINDOW_COVERING_GO_TO_LIFT_PERCENTAGE_COMMAND_ID,
				new ImmutablePair<>("Window Covering Go To Lift Percentage", GoToLiftPercentage.class));
		ALL_RECEIVED.put(WINDOW_COVERING_GO_TO_TILT_VALUE_COMMAND_ID,
				new ImmutablePair<>("Window Covering Go To Tilt Value", GoToTiltValue.class));
		ALL_RECEIVED.put(WINDOW_COVERING_GO_TO_TILT_PERCENTAGE_COMMAND_ID,
				new ImmutablePair<>("Window Covering Go To Tilt Percentage", GoToTiltPercentage.class));
	}
}
