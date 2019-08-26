package moonstone.selene.device.xbee.zcl.domain.general.alarms.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class AlarmsClusterCommands {
	public static final int RESET_ALARM_COMMAND_ID = 0x00;
	public static final int RESET_ALL_ALARMS_COMMAND_ID = 0x01;
	public static final int GET_ALARM_COMMAND_ID = 0x02;
	public static final int RESET_ALARM_LOG_COMMAND_ID = 0x03;

	public static final int ALARM_COMMAND_ID = 0x00;
	public static final int GET_ALARM_RESPONSE_COMMAND_ID = 0x01;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(RESET_ALARM_COMMAND_ID, new ImmutablePair<>("Reset Alarm", ResetAlarm.class));
		ALL_RECEIVED.put(RESET_ALL_ALARMS_COMMAND_ID, new ImmutablePair<>("Reset All Alarms", ResetAllAlarms.class));
		ALL_RECEIVED.put(GET_ALARM_COMMAND_ID, new ImmutablePair<>("Get Alarm", GetAlarm.class));
		ALL_RECEIVED.put(RESET_ALARM_LOG_COMMAND_ID, new ImmutablePair<>("Reset Alarm Log", ResetAlarmLog.class));

		ALL_GENERATED.put(ALARM_COMMAND_ID, "Alarm");
		ALL_GENERATED.put(GET_ALARM_RESPONSE_COMMAND_ID, "Get Alarm Response");
	}
}
