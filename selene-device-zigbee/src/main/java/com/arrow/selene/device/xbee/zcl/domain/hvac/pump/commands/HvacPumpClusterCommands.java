package com.arrow.selene.device.xbee.zcl.domain.hvac.pump.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class HvacPumpClusterCommands {
	public final static Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public final static Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();
}
