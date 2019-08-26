package moonstone.selene.device.xbee.zcl.domain.zll.commissioning.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ZllCommissioningClusterCommands {
	public static final int SCAN_REQUEST_COMMAND_ID = 0x00;
	public static final int DEVICE_INFORMATION_REQUEST_COMMAND_ID = 0x02;
	public static final int IDENTIFY_REQUEST_COMMAND_ID = 0x06;
	public static final int RESET_TO_FACTORY_NEW_REQUEST_COMMAND_ID = 0x07;
	public static final int NETWORK_START_REQUEST_COMMAND_ID = 0x10;
	public static final int NETWORK_JOIN_ROUTER_REQUEST_COMMAND_ID = 0x12;
	public static final int NETWORK_JOIN_END_DEVICE_REQUEST_COMMAND_ID = 0x14;
	public static final int NETWORK_UPDATE_REQUEST_COMMAND_ID = 0x16;

	public static final int SCAN_RESPONSE_COMMAND_ID = 0x00;
	public static final int DEVICE_INFORMATION_RESPONSE_COMMAND_ID = 0x02;
	public static final int NETWORK_START_RESPONSE_COMMAND_ID = 0x10;
	public static final int NETWORK_JOIN_ROUTER_RESPONSE_COMMAND_ID = 0x12;
	public static final int NETWORK_JOIN_END_DEVICE_RESPONSE_COMMAND_ID = 0x14;
	public static final int NETWORK_UPDATE_RESPONSE_COMMAND_ID = 0x16;

	public static final int ENDPOINT_INFORMATION_COMMAND_ID = 0x40;
	public static final int GET_GROUP_IDENTIFIERS_REQUEST_COMMAND_ID = 0x41;
	public static final int GET_ENDPOINT_LIST_REQUEST_COMMAND_ID = 0x42;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(SCAN_REQUEST_COMMAND_ID, "Scan Request");
		ALL_GENERATED.put(DEVICE_INFORMATION_REQUEST_COMMAND_ID, "Device Information Request");
		ALL_GENERATED.put(IDENTIFY_REQUEST_COMMAND_ID, "Identify Request");
		ALL_GENERATED.put(RESET_TO_FACTORY_NEW_REQUEST_COMMAND_ID, "Reset To Factory New Request");
		ALL_GENERATED.put(NETWORK_START_REQUEST_COMMAND_ID, "Network Start Request");
		ALL_GENERATED.put(NETWORK_JOIN_ROUTER_REQUEST_COMMAND_ID, "Network Join Router Request");
		ALL_GENERATED.put(NETWORK_JOIN_END_DEVICE_REQUEST_COMMAND_ID, "Network Join End Device Request");
		ALL_GENERATED.put(NETWORK_UPDATE_REQUEST_COMMAND_ID, "Network Update Request");

		ALL_RECEIVED.put(SCAN_RESPONSE_COMMAND_ID, new ImmutablePair<>("Scan Response", null));
		ALL_RECEIVED.put(DEVICE_INFORMATION_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Device Information Response", null));
		ALL_RECEIVED.put(NETWORK_START_RESPONSE_COMMAND_ID, new ImmutablePair<>("Network Start Response", null));
		ALL_RECEIVED.put(NETWORK_JOIN_ROUTER_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Network Join Router Response", null));
		ALL_RECEIVED.put(NETWORK_JOIN_END_DEVICE_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Network Join End Device Response", null));
		ALL_RECEIVED.put(NETWORK_UPDATE_RESPONSE_COMMAND_ID, new ImmutablePair<>("Network Update Response", null));

		ALL_GENERATED.put(ENDPOINT_INFORMATION_COMMAND_ID, "Endpoint Information");

		ALL_RECEIVED.put(GET_GROUP_IDENTIFIERS_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Get Group Identifiers Request", null));
		ALL_RECEIVED.put(GET_ENDPOINT_LIST_REQUEST_COMMAND_ID, new ImmutablePair<>("Get Endpoint List Request", null));
	}
}
