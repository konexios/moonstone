package com.arrow.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class LevelControlClusterCommands {
	public static final int MOVE_TO_LEVEL_COMMAND_ID = 0x00;
	public static final int MOVE_COMMAND_ID = 0x01;
	public static final int STEP_COMMAND_ID = 0x02;
	public static final int STOP_COMMAND_ID = 0x03;
	public static final int MOVE_TO_LEVEL_WITH_ON_OFF_COMMAND_ID = 0x04;
	public static final int MOVE_WITH_ON_OFF_COMMAND_ID = 0x05;
	public static final int STEP_WITH_ON_OFF_COMMAND_ID = 0x06;
	public static final int STOP_WITH_ON_OFF_COMMAND_ID = 0x07;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(MOVE_TO_LEVEL_COMMAND_ID, new ImmutablePair<>("Move To Level", MoveToLevel.class));
		ALL_RECEIVED.put(MOVE_COMMAND_ID, new ImmutablePair<>("Move", Move.class));
		ALL_RECEIVED.put(STEP_COMMAND_ID, new ImmutablePair<>("Step", Step.class));
		ALL_RECEIVED.put(STOP_COMMAND_ID, new ImmutablePair<>("Stop", Stop.class));
		ALL_RECEIVED.put(MOVE_TO_LEVEL_WITH_ON_OFF_COMMAND_ID,
				new ImmutablePair<>("Move To Level With On/Off", MoveToLevelWithOnOff.class));
		ALL_RECEIVED.put(MOVE_WITH_ON_OFF_COMMAND_ID, new ImmutablePair<>("Move With On/Off", MoveWithOnOff.class));
		ALL_RECEIVED.put(STEP_WITH_ON_OFF_COMMAND_ID, new ImmutablePair<>("Step with On/Off", StepWithOnOff.class));
		ALL_RECEIVED.put(STOP_WITH_ON_OFF_COMMAND_ID, new ImmutablePair<>("Stop With On/Off", Stop.class));
	}
}
