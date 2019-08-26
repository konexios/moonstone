package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class PowerProfileClusterCommands {
	public static final int POWER_PROFILE_NOTIFICATION_COMMAND_ID = 0x00;
	public static final int POWER_PROFILE_RESPONSE_COMMAND_ID = 0x01;
	public static final int POWER_PROFILE_STATE_RESPONSE_COMMAND_ID = 0x02;
	public static final int GET_POWER_PROFILE_PRICE_COMMAND_ID = 0x03;
	public static final int POWER_PROFILES_STATE_NOTIFICATION_COMMAND_ID = 0x04;
	public static final int GET_OVERALL_SCHEDULE_PRICE_COMMAND_ID = 0x05;
	public static final int ENERGY_PHASES_SCHEDULE_REQUEST_COMMAND_ID = 0x06;
	public static final int ENERGY_PHASES_SCHEDULE_STATE_RESPONSE_COMMAND_ID = 0x07;
	public static final int ENERGY_PHASES_SCHEDULE_STATE_NOTIFICATION_COMMAND_ID = 0x08;
	public static final int POWER_PROFILE_SCHEDULE_CONSTRAINTS_NOTIFICATION_COMMAND_ID = 0x09;
	public static final int POWER_PROFILE_SCHEDULE_CONSTRAINTS_RESPONSE_COMMAND_ID = 0x0A;
	public static final int GET_POWER_PROFILE_PRICE_EXTENDED_COMMAND_ID = 0x0B;

	public static final int POWER_PROFILE_REQUEST_COMMAND_ID = 0x00;
	public static final int POWER_PROFILE_STATE_REQUEST_COMMAND_ID = 0x01;
	public static final int GET_POWER_PROFILE_PRICE_RESPONSE_COMMAND_ID = 0x02;
	public static final int GET_OVERALL_SCHEDULE_PRICE_RESPONSE_COMMAND_ID = 0x03;
	public static final int ENERGY_PHASES_SCHEDULE_NOTIFICATION_COMMAND_ID = 0x04;
	public static final int ENERGY_PHASES_SCHEDULE_RESPONSE_COMMAND_ID = 0x05;
	public static final int POWER_PROFILE_SCHEDULE_CONSTRAINTS_REQUEST_COMMAND_ID = 0x06;
	public static final int ENERGY_PHASES_SCHEDULE_STATE_REQUEST_COMMAND_ID = 0x07;
	public static final int GET_POWER_PROFILE_PRICE_EXTENDED_RESPONSE_COMMAND_ID = 0x08;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(POWER_PROFILE_NOTIFICATION_COMMAND_ID, "Power Profile Notification");
		ALL_GENERATED.put(POWER_PROFILE_RESPONSE_COMMAND_ID, "Power Profile Response");
		ALL_GENERATED.put(POWER_PROFILE_STATE_RESPONSE_COMMAND_ID, "Power Profile State Response");
		ALL_GENERATED.put(GET_POWER_PROFILE_PRICE_COMMAND_ID, "Get Power Profile Price");
		ALL_GENERATED.put(POWER_PROFILES_STATE_NOTIFICATION_COMMAND_ID, "Power Profiles State Notification");
		ALL_GENERATED.put(GET_OVERALL_SCHEDULE_PRICE_COMMAND_ID, "Get Overall Schedule Price");
		ALL_GENERATED.put(ENERGY_PHASES_SCHEDULE_REQUEST_COMMAND_ID, "Energy Phases Schedule Request");
		ALL_GENERATED.put(ENERGY_PHASES_SCHEDULE_STATE_RESPONSE_COMMAND_ID, "Energy Phases Schedule State Response");
		ALL_GENERATED.put(ENERGY_PHASES_SCHEDULE_STATE_NOTIFICATION_COMMAND_ID,
				"Energy Phases Schedule State Notification");
		ALL_GENERATED.put(POWER_PROFILE_SCHEDULE_CONSTRAINTS_NOTIFICATION_COMMAND_ID,
				"Power Profile Schedule Constraints Notification");
		ALL_GENERATED.put(POWER_PROFILE_SCHEDULE_CONSTRAINTS_RESPONSE_COMMAND_ID,
				"Power Profile Schedule Constraints Response");
		ALL_GENERATED.put(GET_POWER_PROFILE_PRICE_EXTENDED_COMMAND_ID, "Get Power Profile Price Extended");

		ALL_RECEIVED.put(POWER_PROFILE_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Power Profile Request", PowerProfileRequest.class));
		ALL_RECEIVED.put(POWER_PROFILE_STATE_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Power Profile State Request", PowerProfileStateRequest.class));
		ALL_RECEIVED.put(GET_POWER_PROFILE_PRICE_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Get Power Profile Price Response", GetPowerProfilePrice.class));
		ALL_RECEIVED.put(GET_OVERALL_SCHEDULE_PRICE_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Get Overall Schedule Price Response", GetOverallSchedulePrice.class));
		ALL_RECEIVED.put(ENERGY_PHASES_SCHEDULE_NOTIFICATION_COMMAND_ID,
				new ImmutablePair<>("Energy Phases Schedule Notification", EnergyPhasesScheduleNotification.class));
		ALL_RECEIVED.put(ENERGY_PHASES_SCHEDULE_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Energy Phases Schedule Response", EnergyPhasesScheduleResponse.class));
		ALL_RECEIVED.put(POWER_PROFILE_SCHEDULE_CONSTRAINTS_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Power Profile Schedule Constraints Request",
						PowerProfileScheduleConstraintsRequest.class));
		ALL_RECEIVED.put(ENERGY_PHASES_SCHEDULE_STATE_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Energy Phases Schedule State Request", EnergyPhasesScheduleStateRequest.class));
		ALL_RECEIVED.put(GET_POWER_PROFILE_PRICE_EXTENDED_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Get Power Profile Price Extended Response",
						GetPowerProfilePriceExtendedResponse.class));
	}
}
