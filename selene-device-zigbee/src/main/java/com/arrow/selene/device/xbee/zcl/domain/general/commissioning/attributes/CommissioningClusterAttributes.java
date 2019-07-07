package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class CommissioningClusterAttributes {
	public static final int SHORT_ADDRESS_ATTRIBUTE_ID = 0x0000;
	public static final int EXTENDED_PAN_ID_ATTRIBUTE_ID = 0x0001;
	public static final int PAN_ID_ATTRIBUTE_ID = 0x0002;
	public static final int CHANNEL_MASK_ATTRIBUTE_ID = 0x0003;
	public static final int PROTOCOL_VERSION_ATTRIBUTE_ID = 0x0004;
	public static final int STACK_PROFILE_ATTRIBUTE_ID = 0x0005;
	public static final int STARTUP_CONTROL_ATTRIBUTE_ID = 0x0006;
	public static final int TRUST_CENTER_ADDRESS_ATTRIBUTE_ID = 0x0010;
	public static final int TRUST_CENTER_MASTER_KEY_ATTRIBUTE_ID = 0x0011;
	public static final int NETWORK_KEY_ATTRIBUTE_ID = 0x0012;
	public static final int USE_INSECURE_JOIN_ATTRIBUTE_ID = 0x0013;
	public static final int PRECONFIGURED_LINK_KEY_ATTRIBUTE_ID = 0x0014;
	public static final int NETWORK_KEY_SEQ_NUM_ATTRIBUTE_ID = 0x0015;
	public static final int NETWORK_KEY_TYPE_ATTRIBUTE_ID = 0x0016;
	public static final int NETWORK_MANAGER_ADDRESS_ATTRIBUTE_ID = 0x0017;

	public static final int SCAN_ATTEMPTS_ADDRESS_ATTRIBUTE_ID = 0x0020;
	public static final int TIME_BETWEEN_SCANS_ADDRESS_ATTRIBUTE_ID = 0x0021;
	public static final int REJOIN_INTERVAL_ATTRIBUTE_ID = 0x0022;
	public static final int MAX_REJOIN_INTERVAL_ATTRIBUTE_ID = 0x0023;

	public static final int INDIRECT_POLL_RATE_ATTRIBUTE_ID = 0x0030;
	public static final int PARENT_RETRY_THRESHOLD_ATTRIBUTE_ID = 0x0031;

	public static final int CONCENTRATOR_FLAG_ATTRIBUTE_ID = 0x0040;
	public static final int CONCENTRATOR_RADIUS_ATTRIBUTE_ID = 0x0041;
	public static final int CONCENTRATOR_DISCOVERY_TIME_ATTRIBUTE_ID = 0x0042;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(SHORT_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("Short Address", null));
		ALL.put(EXTENDED_PAN_ID_ATTRIBUTE_ID, new ImmutablePair<>("Extended PAN ID", null));
		ALL.put(PAN_ID_ATTRIBUTE_ID, new ImmutablePair<>("PAN ID", null));
		ALL.put(CHANNEL_MASK_ATTRIBUTE_ID, new ImmutablePair<>("Channel Mask", null));
		ALL.put(PROTOCOL_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Protocol Version", null));
		ALL.put(STACK_PROFILE_ATTRIBUTE_ID, new ImmutablePair<>("Stack Profile", StackProfile.ZIGBEE_STACK_PROFILE));
		ALL.put(STARTUP_CONTROL_ATTRIBUTE_ID, new ImmutablePair<>("Startup Control", StartupControl.FORM_NETWORK));
		ALL.put(TRUST_CENTER_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("Trust Center Address", null));
		ALL.put(TRUST_CENTER_MASTER_KEY_ATTRIBUTE_ID, new ImmutablePair<>("Trust Center Master Key", null));
		ALL.put(NETWORK_KEY_ATTRIBUTE_ID, new ImmutablePair<>("Network Key", null));
		ALL.put(USE_INSECURE_JOIN_ATTRIBUTE_ID, new ImmutablePair<>("Use Insecure Join", null));
		ALL.put(PRECONFIGURED_LINK_KEY_ATTRIBUTE_ID, new ImmutablePair<>("Preconfigured Link Key", null));
		ALL.put(NETWORK_KEY_SEQ_NUM_ATTRIBUTE_ID, new ImmutablePair<>("Network Key Sequence Number", null));
		ALL.put(NETWORK_KEY_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Network Key Type", null));
		ALL.put(NETWORK_MANAGER_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("Network Manager Address", null));
		ALL.put(SCAN_ATTEMPTS_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("Scan Attempts Address", null));
		ALL.put(TIME_BETWEEN_SCANS_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("Time Between Scan's Address", null));
		ALL.put(REJOIN_INTERVAL_ATTRIBUTE_ID, new ImmutablePair<>("Rejoin Interval", null));
		ALL.put(MAX_REJOIN_INTERVAL_ATTRIBUTE_ID, new ImmutablePair<>("Max Rejoin Interval", null));
		ALL.put(INDIRECT_POLL_RATE_ATTRIBUTE_ID, new ImmutablePair<>("Indirect Poll Rate", null));
		ALL.put(PARENT_RETRY_THRESHOLD_ATTRIBUTE_ID, new ImmutablePair<>("Parent Retry Threshold", null));
		ALL.put(CONCENTRATOR_FLAG_ATTRIBUTE_ID, new ImmutablePair<>("Concentrator Flag", null));
		ALL.put(CONCENTRATOR_RADIUS_ATTRIBUTE_ID, new ImmutablePair<>("Concentrator Radius", null));
		ALL.put(CONCENTRATOR_DISCOVERY_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Concentrator Discovery Time", null));
	}
}
