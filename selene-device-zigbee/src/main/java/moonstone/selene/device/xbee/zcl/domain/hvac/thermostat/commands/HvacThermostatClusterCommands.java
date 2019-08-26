package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class HvacThermostatClusterCommands {
	public static final int SETPOINT_RAISE_LOWER_COMMAND_ID = 0x00;
	public static final int SET_WEEKLY_SCHEDULE_COMMAND_ID = 0x01;
	public static final int GET_WEEKLY_SCHEDULE_COMMAND_ID = 0x02;
	public static final int CLEAR_WEEKLY_SCHEDULE_COMMAND_ID = 0x03;
	public static final int GET_RELAY_STATUS_LOG_COMMAND_ID = 0x04;

	public static final int CURRENT_WEEKLY_SCHEDULE_COMMAND_ID = 0x00;
	public static final int RELAY_STATUS_LOG_COMMAND_ID = 0x01;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(SETPOINT_RAISE_LOWER_COMMAND_ID,
				new ImmutablePair<>("Set Point Raise Lower", SetpointRaiseLower.class));
		ALL_RECEIVED.put(SET_WEEKLY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Set Weekly Schedule", SetWeeklySchedule.class));
		ALL_RECEIVED.put(GET_WEEKLY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Get Weekly Schedule", GetWeeklySchedule.class));
		ALL_RECEIVED.put(CLEAR_WEEKLY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Clear Weekly Schedule", ClearWeeklySchedule.class));
		ALL_RECEIVED.put(GET_RELAY_STATUS_LOG_COMMAND_ID,
				new ImmutablePair<>("Get Relay Status Log", GetRelayStatusLog.class));

		ALL_GENERATED.put(CURRENT_WEEKLY_SCHEDULE_COMMAND_ID, "Current Weekly Schedule");
		ALL_GENERATED.put(RELAY_STATUS_LOG_COMMAND_ID, "Relay Status Log");
	}
}
