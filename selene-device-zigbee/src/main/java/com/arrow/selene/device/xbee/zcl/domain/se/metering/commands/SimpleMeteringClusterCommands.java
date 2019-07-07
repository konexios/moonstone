package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SimpleMeteringClusterCommands {
	public static final int GET_PROFILE_RESPONSE_COMMAND_ID = 0x00;
	public static final int REQUEST_MIRROR_COMMAND_ID = 0x01;
	public static final int REMOVE_MIRROR_COMMAND_ID = 0x02;
	public static final int REQUEST_FAST_POLL_MODE_RESPONSE_COMMAND_ID = 0x03;
	public static final int SCHEDULE_SNAPSHOT_RESPONSE_COMMAND_ID = 0x04;
	public static final int TAKE_SNAPSHOT_RESPONSE_COMMAND_ID = 0x05;
	public static final int PUBLISH_SNAPSHOT_COMMAND_ID = 0x06;
	public static final int GET_SAMPLED_DATA_RESPONSE_COMMAND_ID = 0x07;
	public static final int CONFIGURE_MIRROR_COMMAND_ID = 0x08;
	public static final int CONFIGURE_NOTIFICATION_SCHEME_COMMAND_ID = 0x09;
	public static final int CONFIGURE_NOTIFICATION_FLAGS_COMMAND_ID = 0x0A;
	public static final int GET_NOTIFICATION_FLAG_COMMAND_ID = 0x0B;
	public static final int SUPPLY_STATUS_RESPONSE_COMMAND_ID = 0x0C;
	public static final int START_SAMPLING_RESPONSE_COMMAND_ID = 0x0D;

	public static final int GET_PROFILE_COMMAND_ID = 0x00;
	public static final int REQUEST_MIRROR_RESPONSE_COMMAND_ID = 0x01;
	public static final int MIRROR_REMOVED_COMMAND_ID = 0x02;
	public static final int REQUEST_FAST_POLL_MODE_COMMAND_ID = 0x03;
	public static final int SCHEDULE_SNAPSHOT_COMMAND_ID = 0x04;
	public static final int TAKE_SNAPSHOT_COMMAND_ID = 0x05;
	public static final int GET_SNAPSHOT_COMMAND_ID = 0x06;
	public static final int START_SAMPLING_COMMAND_ID = 0x07;
	public static final int GET_SAMPLED_DATA_COMMAND_ID = 0x08;
	public static final int MIRROR_REPORT_ATTRIBUTE_RESPONSE_COMMAND_ID = 0x09;
	public static final int RESET_LOAD_LIMIT_COUNTER_COMMAND_ID = 0x0A;
	public static final int CHANGE_SUPPLY_COMMAND_ID = 0x0B;
	public static final int LOCAL_CHANGE_SUPPLY_COMMAND_ID = 0x0C;
	public static final int SET_SUPPLY_STATUS_COMMAND_ID = 0x0D;
	public static final int SET_UNCONTROLLED_FLOW_THRESHOLD_COMMAND_ID = 0x0E;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(GET_PROFILE_RESPONSE_COMMAND_ID, "Get Profile Response");
		ALL_GENERATED.put(REQUEST_MIRROR_COMMAND_ID, "Request Mirror");
		ALL_GENERATED.put(REMOVE_MIRROR_COMMAND_ID, "Remove Mirror");
		ALL_GENERATED.put(REQUEST_FAST_POLL_MODE_RESPONSE_COMMAND_ID, "Request Fast Poll Mode Response");
		ALL_GENERATED.put(SCHEDULE_SNAPSHOT_RESPONSE_COMMAND_ID, "Schedule Snapshot Response");
		ALL_GENERATED.put(TAKE_SNAPSHOT_RESPONSE_COMMAND_ID, "Take Snapshot Response");
		ALL_GENERATED.put(PUBLISH_SNAPSHOT_COMMAND_ID, "Publish Snapshot");
		ALL_GENERATED.put(GET_SAMPLED_DATA_RESPONSE_COMMAND_ID, "Get Sampled Data Response");
		ALL_GENERATED.put(CONFIGURE_MIRROR_COMMAND_ID, "Configure Mirror");
		ALL_GENERATED.put(CONFIGURE_NOTIFICATION_SCHEME_COMMAND_ID, "Configure Notification Scheme");
		ALL_GENERATED.put(CONFIGURE_NOTIFICATION_FLAGS_COMMAND_ID, "Configure Notification Flags");
		ALL_GENERATED.put(GET_NOTIFICATION_FLAG_COMMAND_ID, "Get Notification Flag");
		ALL_GENERATED.put(SUPPLY_STATUS_RESPONSE_COMMAND_ID, "Supply Status Response");
		ALL_GENERATED.put(START_SAMPLING_RESPONSE_COMMAND_ID, "Start Sampling Response");

		ALL_RECEIVED.put(GET_PROFILE_COMMAND_ID, new ImmutablePair<>("Get Profile", GetProfile.class));
		ALL_RECEIVED.put(REQUEST_MIRROR_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Request Mirror Response", RequestMirrorResponse.class));
		ALL_RECEIVED.put(MIRROR_REMOVED_COMMAND_ID, new ImmutablePair<>("Mirror Removed", MirrorRemoved.class));
		ALL_RECEIVED.put(REQUEST_FAST_POLL_MODE_COMMAND_ID,
				new ImmutablePair<>("Request Fast Poll Mode", RequestFastPollMode.class));
		ALL_RECEIVED.put(SCHEDULE_SNAPSHOT_COMMAND_ID,
				new ImmutablePair<>("Schedule Snapshot", ScheduleSnapshot.class));
		ALL_RECEIVED.put(TAKE_SNAPSHOT_COMMAND_ID, new ImmutablePair<>("Take Snapshot", TakeSnapshot.class));
		ALL_RECEIVED.put(GET_SNAPSHOT_COMMAND_ID, new ImmutablePair<>("Get Snapshot", GetSnapshot.class));
		ALL_RECEIVED.put(START_SAMPLING_COMMAND_ID, new ImmutablePair<>("Start Sampling", StartSampling.class));
		ALL_RECEIVED.put(GET_SAMPLED_DATA_COMMAND_ID, new ImmutablePair<>("Get Sampled Data", GetSampledData.class));
		ALL_RECEIVED.put(MIRROR_REPORT_ATTRIBUTE_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Mirror Report Attribute Response", MirrorReportAttributeResponse.class));
		ALL_RECEIVED.put(RESET_LOAD_LIMIT_COUNTER_COMMAND_ID,
				new ImmutablePair<>("Reset Load Limit Counter", ResetLoadLimitCounter.class));
		ALL_RECEIVED.put(CHANGE_SUPPLY_COMMAND_ID, new ImmutablePair<>("Change Supply", ChangeSupply.class));
		ALL_RECEIVED.put(LOCAL_CHANGE_SUPPLY_COMMAND_ID,
				new ImmutablePair<>("Local Change Supply", LocalChangeSupply.class));
		ALL_RECEIVED.put(SET_SUPPLY_STATUS_COMMAND_ID, new ImmutablePair<>("Set Supply Status", SetSupplyStatus
				.class));
		ALL_RECEIVED.put(SET_UNCONTROLLED_FLOW_THRESHOLD_COMMAND_ID,
				new ImmutablePair<>("Set Uncontrolled Flow Threshold", SetUncontrolledFlowThreshold.class));
	}
}
