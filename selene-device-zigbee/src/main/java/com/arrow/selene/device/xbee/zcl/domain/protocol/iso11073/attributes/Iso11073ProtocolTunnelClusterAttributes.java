package com.arrow.selene.device.xbee.zcl.domain.protocol.iso11073.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class Iso11073ProtocolTunnelClusterAttributes {
	public static final int DEVICE_ID_LIST_ATTRIBUTE_ID = 0x0000;
	public static final int MANAGER_TARGET_ATTRIBUTE_ID = 0x0001;
	public static final int MANAGER_ENDPOINT_ATTRIBUTE_ID = 0x0002;
	public static final int CONNECTED_ATTRIBUTE_ID = 0x0003;
	public static final int PREEMPTIBLE_ATTRIBUTE_ID = 0x0004;
	public static final int IDLE_TIMEOUT_ATTRIBUTE_ID = 0x0005;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(DEVICE_ID_LIST_ATTRIBUTE_ID, new ImmutablePair<>("Device Id List", null));
		ALL.put(MANAGER_TARGET_ATTRIBUTE_ID, new ImmutablePair<>("Manager Target", null));
		ALL.put(MANAGER_ENDPOINT_ATTRIBUTE_ID, new ImmutablePair<>("Manager Endpoint", null));
		ALL.put(CONNECTED_ATTRIBUTE_ID, new ImmutablePair<>("Connected", null));
		ALL.put(PREEMPTIBLE_ATTRIBUTE_ID, new ImmutablePair<>("Preemptible", null));
		ALL.put(IDLE_TIMEOUT_ATTRIBUTE_ID, new ImmutablePair<>("Idle Timeout", null));
	}
}
