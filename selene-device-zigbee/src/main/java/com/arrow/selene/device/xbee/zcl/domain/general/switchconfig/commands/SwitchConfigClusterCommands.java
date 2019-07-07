package com.arrow.selene.device.xbee.zcl.domain.general.switchconfig.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SwitchConfigClusterCommands {
	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();
}
