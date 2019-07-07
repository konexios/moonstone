package com.arrow.selene.device.xbee.zcl.domain.se.demand.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class DemandResponseAndLoadControlClusterCommands {
	public static final int LOAD_CONTROL_EVENT_COMMAND_ID = 0x00;
	public static final int CANCEL_LOAD_CONTROL_EVENT_COMMAND_ID = 0x01;
	public static final int CANCEL_ALL_LOAD_CONTROL_EVENTS_COMMAND_ID = 0x02;

	public static final int REPORT_EVENT_STATUS_COMMAND_ID = 0x00;
	public static final int GET_SCHEDULED_EVENTS_COMMAND_ID = 0x01;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(LOAD_CONTROL_EVENT_COMMAND_ID, "Load Control Event");
		ALL_GENERATED.put(CANCEL_LOAD_CONTROL_EVENT_COMMAND_ID, "Cancel Load Control Event");
		ALL_GENERATED.put(CANCEL_ALL_LOAD_CONTROL_EVENTS_COMMAND_ID, "Cancel All Load Control Events");

		ALL_RECEIVED.put(REPORT_EVENT_STATUS_COMMAND_ID,
				new ImmutablePair<>("Report Event Status", ReportEventStatus.class));
		ALL_RECEIVED.put(GET_SCHEDULED_EVENTS_COMMAND_ID,
				new ImmutablePair<>("Get Scheduled Events", GetScheduledEvents.class));
	}
}
