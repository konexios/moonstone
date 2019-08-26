package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RssiLocationClusterCommands {
	public static final int SET_ABSOLUTE_LOCATION_COMMAND_ID = 0x00;
	public static final int SET_DEVICE_CONFIGURATION_COMMAND_ID = 0x01;
	public static final int GET_DEVICE_CONFIGURATION_COMMAND_ID = 0x02;
	public static final int GET_LOCATION_DATA_COMMAND_ID = 0x03;
	public static final int RSSI_RESPONSE_COMMAND_ID = 0x04;
	public static final int SEND_PINGS_COMMAND_ID = 0x05;
	public static final int ANCHOR_NODE_ANNOUNCE_COMMAND_ID = 0x06;

	public static final int DEVICE_CONFIGURATION_RESPONSE_COMMAND_ID = 0x00;
	public static final int LOCATION_DATA_RESPONSE_COMMAND_ID = 0x01;
	public static final int LOCATION_DATA_NOTIFICATION_COMMAND_ID = 0x02;
	public static final int COMPACT_LOCATION_DATA_NOTIFICATION_COMMAND_ID = 0x03;
	public static final int RSSI_PING_COMMAND_ID = 0x04;
	public static final int RSSI_REQUEST_COMMAND_ID = 0x05;
	public static final int REPORT_RSSI_MEASUREMENTS_COMMAND_ID = 0x06;
	public static final int REQUEST_OWN_LOCATION_COMMAND_ID = 0x07;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(SET_ABSOLUTE_LOCATION_COMMAND_ID,
				new ImmutablePair<>("Set Absolute Location", SetAbsoluteLocation.class));
		ALL_RECEIVED.put(SET_DEVICE_CONFIGURATION_COMMAND_ID,
				new ImmutablePair<>("Set Device Configuration", SetDeviceConfiguration.class));
		ALL_RECEIVED.put(GET_DEVICE_CONFIGURATION_COMMAND_ID,
				new ImmutablePair<>("Get Device Configuration", GetDeviceConfiguration.class));
		ALL_RECEIVED.put(GET_LOCATION_DATA_COMMAND_ID, new ImmutablePair<>("Get Location Data", GetLocationData
				.class));
		ALL_RECEIVED.put(RSSI_RESPONSE_COMMAND_ID, new ImmutablePair<>("RSSI Response", RssiResponse.class));
		ALL_RECEIVED.put(SEND_PINGS_COMMAND_ID, new ImmutablePair<>("Send Pings", SendPings.class));
		ALL_RECEIVED.put(ANCHOR_NODE_ANNOUNCE_COMMAND_ID,
				new ImmutablePair<>("Anchor Node Announce", AnchorNodeAnnounce.class));

		ALL_GENERATED.put(DEVICE_CONFIGURATION_RESPONSE_COMMAND_ID, "Device Configuration Response");
		ALL_GENERATED.put(LOCATION_DATA_RESPONSE_COMMAND_ID, "Location Data Response");
		ALL_GENERATED.put(LOCATION_DATA_NOTIFICATION_COMMAND_ID, "Location Data Notification");
		ALL_GENERATED.put(COMPACT_LOCATION_DATA_NOTIFICATION_COMMAND_ID, "Compact Location Data Notification");
		ALL_GENERATED.put(RSSI_PING_COMMAND_ID, "RSSI Ping");
		ALL_GENERATED.put(RSSI_REQUEST_COMMAND_ID, "RSSI Request");
		ALL_GENERATED.put(REPORT_RSSI_MEASUREMENTS_COMMAND_ID, "Report RSSI Measurements");
		ALL_GENERATED.put(REQUEST_OWN_LOCATION_COMMAND_ID, "Request Own Location");
	}
}
