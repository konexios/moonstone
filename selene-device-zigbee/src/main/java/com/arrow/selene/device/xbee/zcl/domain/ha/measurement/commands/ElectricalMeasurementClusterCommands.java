package com.arrow.selene.device.xbee.zcl.domain.ha.measurement.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ElectricalMeasurementClusterCommands {
	public static final int GET_PROFILE_INFO_RESPONSE_COMMAND_COMMAND_ID = 0x00;
	public static final int GET_MEASUREMENT_PROFILE_RESPONSE_COMMAND_COMMAND_ID = 0x01;

	public static final int GET_PROFILE_INFO_COMMAND_COMMAND_ID = 0x00;
	public static final int GET_MEASUREMENT_PROFILE_COMMAND_COMMAND_ID = 0x01;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(GET_PROFILE_INFO_RESPONSE_COMMAND_COMMAND_ID, "Get Profile Info Response Command");
		ALL_GENERATED.put(GET_MEASUREMENT_PROFILE_RESPONSE_COMMAND_COMMAND_ID,
				"Get Measurement Profile Response Command");

		ALL_RECEIVED.put(GET_PROFILE_INFO_COMMAND_COMMAND_ID,
				new ImmutablePair<>("Get Profile Info Command", GetProfileInfo.class));
		ALL_RECEIVED.put(GET_MEASUREMENT_PROFILE_COMMAND_COMMAND_ID,
				new ImmutablePair<>("Get Measurement Profile " + "Command", GetMeasurementProfile.class));
	}
}
