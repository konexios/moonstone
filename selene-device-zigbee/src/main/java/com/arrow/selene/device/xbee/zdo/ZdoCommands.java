package com.arrow.selene.device.xbee.zdo;

import java.util.HashMap;
import java.util.Map;

public class ZdoCommands {
	public static final int NETWORK_ADDRESS_REQ = 0x0000;
	public static final int NETWORK_ADDRESS_RSP = 0x8000;
	public static final int NODE_DESCRIPTOR_REQ = 0x0002;
	public static final int NODE_DESCRIPTOR_RSP = 0x8002;
	public static final int SIMPLE_DESCRIPTOR_REQ = 0x0004;
	public static final int SIMPLE_DESCRIPTOR_RSP = 0x8004;
	public static final int ACTIVE_ENDPOINTS_REQ = 0x0005;
	public static final int ACTIVE_ENDPOINTS_RSP = 0x8005;
	public static final int MATCH_DESCRIPTOR_REQ = 0x0006;
	public static final int MATCH_DESCRIPTOR_RSP = 0x8006;
	public static final int END_DEVICE_ANNOUNCE_REQ = 0x0013;
	public static final int END_DEVICE_ANNOUNCE_RSP = 0x8013;
	public static final int BIND_REQ = 0x0021;
	public static final int BIND_RSP = 0x8021;
	public static final int UNBIND_REQ = 0x0022;
	public static final int UNBIND_RSP = 0x8022;
	public static final int MANAGEMENT_LQI_REQ = 0x0031;
	public static final int MANAGEMENT_LQI_RSP = 0x8031;
	public static final int MANAGEMENT_BIND_REQ = 0x0033;
	public static final int MANAGEMENT_BIND_RSP = 0x8033;
	public static final int PERMIT_JOIN_REQ = 0x0036;
	public static final int PERMIT_JOIN_RSP = 0x8036;

	private static final Map<Integer, String> ALL = new HashMap<>();

	static {
		ALL.put(NETWORK_ADDRESS_REQ, "Network address request");
		ALL.put(NETWORK_ADDRESS_RSP, "Network address response");
		ALL.put(NODE_DESCRIPTOR_REQ, "Node descriptor request");
		ALL.put(NODE_DESCRIPTOR_RSP, "Node descriptor response");
		ALL.put(SIMPLE_DESCRIPTOR_REQ, "Simple descriptor request");
		ALL.put(SIMPLE_DESCRIPTOR_RSP, "Simple descriptor response");
		ALL.put(ACTIVE_ENDPOINTS_REQ, "Active endpoint request");
		ALL.put(ACTIVE_ENDPOINTS_RSP, "Active endpoint response");
		ALL.put(MATCH_DESCRIPTOR_REQ, "Match descriptor request");
		ALL.put(END_DEVICE_ANNOUNCE_REQ, "End device announce request");
		ALL.put(END_DEVICE_ANNOUNCE_RSP, "End device announce response");
		ALL.put(BIND_REQ, "Bind request");
		ALL.put(BIND_RSP, "Bind response");
		ALL.put(UNBIND_REQ, "Unbind request");
		ALL.put(UNBIND_RSP, "Unbind response");
		ALL.put(MANAGEMENT_LQI_REQ, "Management LQI request");
		ALL.put(MANAGEMENT_LQI_RSP, "Management LQI response");
		ALL.put(MANAGEMENT_BIND_REQ, "Management bind request");
		ALL.put(MANAGEMENT_BIND_RSP, "Management bind response");
		ALL.put(PERMIT_JOIN_REQ, "Permit join request");
		ALL.put(PERMIT_JOIN_RSP, "Permit join response");
	}

	public static String getName(Integer id) {
		return ALL.getOrDefault(id, "Unknown ZDO command");
	}
}
