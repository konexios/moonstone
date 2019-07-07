package com.arrow.selene.device.xbee.zcl.domain.ha.events.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ApplianceEventsAndAlertClusterCommands {
	public static final int GET_ALERTS_RESPONSE_COMMAND_ID = 0x00;
	public static final int ALERTS_NOTIFICATION_COMMAND_ID = 0x01;
	public static final int EVENTS_NOTIFICATION_COMMAND_ID = 0x02;

	public static final int GET_ALERTS_COMMAND_ID = 0x00;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(GET_ALERTS_RESPONSE_COMMAND_ID, "Get Alerts Response");
		ALL_GENERATED.put(ALERTS_NOTIFICATION_COMMAND_ID, "Alerts Notification");
		ALL_GENERATED.put(EVENTS_NOTIFICATION_COMMAND_ID, "Events Notification");

		ALL_RECEIVED.put(GET_ALERTS_COMMAND_ID, new ImmutablePair<>("Get Alerts", GetAlerts.class));
	}
}
