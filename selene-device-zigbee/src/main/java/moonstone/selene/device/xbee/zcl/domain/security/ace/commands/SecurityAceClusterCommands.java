package moonstone.selene.device.xbee.zcl.domain.security.ace.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class SecurityAceClusterCommands {
	public static final int ARM_RESPONSE_COMMAND_ID = 0x00;
	public static final int GET_ZONE_ID_MAP_RESPONSE_COMMAND_ID = 0x01;
	public static final int GET_ZONE_INFORMATION_RESPONSE_COMMAND_ID = 0x02;
	public static final int ZONE_STATUS_CHANGED_COMMAND_ID = 0x03;
	public static final int PANEL_STATUS_CHANGED_COMMAND_ID = 0x04;
	public static final int GET_PANEL_STATUS_RESPONSE_COMMAND_ID = 0x05;
	public static final int SET_BYPASSED_ZONE_LIST_COMMAND_ID = 0x06;
	public static final int BYPASS_RESPONSE_COMMAND_ID = 0x07;
	public static final int GET_ZONE_STATUS_RESPONSE_COMMAND_ID = 0x08;

	public static final int ARM_COMMAND_ID = 0x00;
	public static final int BYPASS_COMMAND_ID = 0x01;
	public static final int EMERGENCY_COMMAND_ID = 0x02;
	public static final int FIRE_COMMAND_ID = 0x03;
	public static final int PANIC_COMMAND_ID = 0x04;
	public static final int GET_ZONE_ID_MAP_COMMAND_ID = 0x05;
	public static final int GET_ZONE_INFORMATION_COMMAND_ID = 0x06;
	public static final int GET_PANEL_STATUS_COMMAND_ID = 0x07;
	public static final int GET_BYPASSED_ZONE_LIST_COMMAND_ID = 0x08;
	public static final int GET_ZONE_STATUS_COMMAND_ID = 0x09;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(ARM_RESPONSE_COMMAND_ID, "Arm Response");
		ALL_GENERATED.put(GET_ZONE_ID_MAP_RESPONSE_COMMAND_ID, "Get Zone Id Map Response");
		ALL_GENERATED.put(GET_ZONE_INFORMATION_RESPONSE_COMMAND_ID, "Get Zone Information Response");
		ALL_GENERATED.put(ZONE_STATUS_CHANGED_COMMAND_ID, "Zone Status Changed");
		ALL_GENERATED.put(PANEL_STATUS_CHANGED_COMMAND_ID, "Panel Status Changed");
		ALL_GENERATED.put(GET_PANEL_STATUS_RESPONSE_COMMAND_ID, "Get Panel Status Response");
		ALL_GENERATED.put(SET_BYPASSED_ZONE_LIST_COMMAND_ID, "Set Bypassed Zone List");
		ALL_GENERATED.put(BYPASS_RESPONSE_COMMAND_ID, "Bypass Response");
		ALL_GENERATED.put(GET_ZONE_STATUS_RESPONSE_COMMAND_ID, "Get Zone Status Response");

		ALL_RECEIVED.put(ARM_COMMAND_ID, new ImmutablePair<>("Arm", Arm.class));
		ALL_RECEIVED.put(BYPASS_COMMAND_ID, new ImmutablePair<>("Bypass", Bypass.class));
		ALL_RECEIVED.put(EMERGENCY_COMMAND_ID, new ImmutablePair<>("Emergency", Emergency.class));
		ALL_RECEIVED.put(FIRE_COMMAND_ID, new ImmutablePair<>("Fire", Fire.class));
		ALL_RECEIVED.put(PANIC_COMMAND_ID, new ImmutablePair<>("Panic", Panic.class));
		ALL_RECEIVED.put(GET_ZONE_ID_MAP_COMMAND_ID, new ImmutablePair<>("Get Zone Id Map", GetZoneIdMap.class));
		ALL_RECEIVED.put(GET_ZONE_INFORMATION_COMMAND_ID,
				new ImmutablePair<>("Get Zone Information", GetZoneInformation.class));
		ALL_RECEIVED.put(GET_PANEL_STATUS_COMMAND_ID, new ImmutablePair<>("Get Panel Status", NoPayloadCommand.class));
		ALL_RECEIVED.put(GET_BYPASSED_ZONE_LIST_COMMAND_ID,
				new ImmutablePair<>("Get Bypassed Zone List", NoPayloadCommand.class));
		ALL_RECEIVED.put(GET_ZONE_STATUS_COMMAND_ID, new ImmutablePair<>("Get Zone Status", NoPayloadCommand.class));
	}
}
