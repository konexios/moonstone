package com.arrow.selene.device.xbee.zcl.domain.security.wd.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SecurityWdClusterCommands {
	public static final int START_WARNING_COMMAND_ID = 0x00;
	public static final int SQUAWK_COMMAND_ID = 0x01;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(START_WARNING_COMMAND_ID, new ImmutablePair<>("Start Warning", StartWarning.class));
		ALL_RECEIVED.put(SQUAWK_COMMAND_ID, new ImmutablePair<>("Squawk Command", Squawk.class));
	}
}
