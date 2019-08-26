package moonstone.selene.device.xbee.zcl.domain.security.zone.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class SecurityZoneClusterCommands {
	public static final int ZONE_ENROLL_RESPONSE_COMMAND_ID = 0x00;
	public static final int INITIATE_NORMAL_OPERATION_MODE_COMMAND_ID = 0x01;
	public static final int INITIATE_TEST_MODE_COMMAND_ID = 0x02;

	public static final int ZONE_STATUS_CHANGE_NOTIFICATION_COMMAND_ID = 0x00;
	public static final int ZONE_ENROLL_REQUEST_COMMAND_ID = 0x01;
	public static final int INITIATE_NORMAL_OPERATION_MODE_RESPONSE_COMMAND_ID = 0x02;
	public static final int INITIATE_TEST_MODE_RESPONSE_COMMAND_ID = 0x03;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(ZONE_ENROLL_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Zone Enroll Response", ZoneEnrollResponse.class));
		ALL_RECEIVED.put(INITIATE_NORMAL_OPERATION_MODE_COMMAND_ID,
				new ImmutablePair<>("Initiate Normal Operation Mode", NoPayloadCommand.class));
		ALL_RECEIVED.put(INITIATE_TEST_MODE_COMMAND_ID,
				new ImmutablePair<>("Initiate Test Mode", NoPayloadCommand.class));

		ALL_GENERATED.put(ZONE_STATUS_CHANGE_NOTIFICATION_COMMAND_ID, "Zone Status Change Notification");
		ALL_GENERATED.put(ZONE_ENROLL_REQUEST_COMMAND_ID, "Zone Enroll Request");
		ALL_GENERATED.put(INITIATE_NORMAL_OPERATION_MODE_RESPONSE_COMMAND_ID,
				"Initiate Normal Operation Mode Response");
		ALL_GENERATED.put(INITIATE_TEST_MODE_RESPONSE_COMMAND_ID, "Initiate Test Mode Response");
	}
}
