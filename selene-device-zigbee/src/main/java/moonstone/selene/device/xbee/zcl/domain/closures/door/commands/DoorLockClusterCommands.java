package moonstone.selene.device.xbee.zcl.domain.closures.door.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.commands.ClearWeeklySchedule;
import moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.commands.GetWeeklySchedule;
import moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.commands.SetWeeklySchedule;

public class DoorLockClusterCommands {
	public static final int LOCK_DOOR_COMMAND_ID = 0x00;
	public static final int UNLOCK_DOOR_COMMAND_ID = 0x01;
	public static final int TOGGLE_COMMAND_ID = 0x02;
	public static final int UNLOCK_WITH_TIMEOUT_COMMAND_ID = 0x03;
	public static final int GET_LOG_RECORD_COMMAND_ID = 0x04;
	public static final int SET_PIN_COMMAND_ID = 0x05;
	public static final int GET_PIN_COMMAND_ID = 0x06;
	public static final int CLEAR_PIN_COMMAND_ID = 0x07;
	public static final int CLEAR_ALL_PINS_COMMAND_ID = 0x08;
	public static final int SET_USER_STATUS_COMMAND_ID = 0x09;
	public static final int GET_USER_STATUS_COMMAND_ID = 0x0A;
	public static final int SET_WEEKDAY_SCHEDULE_COMMAND_ID = 0x0B;
	public static final int GET_WEEKDAY_SCHEDULE_COMMAND_ID = 0x0C;
	public static final int CLEAR_WEEKDAY_SCHEDULE_COMMAND_ID = 0x0D;
	public static final int SET_YEARDAY_SCHEDULE_COMMAND_ID = 0x0E;
	public static final int GET_YEARDAY_SCHEDULE_COMMAND_ID = 0x0F;
	public static final int CLEAR_YEARDAY_SCHEDULE_COMMAND_ID = 0x10;
	public static final int SET_HOLIDAY_SCHEDULE_COMMAND_ID = 0x11;
	public static final int GET_HOLIDAY_SCHEDULE_COMMAND_ID = 0x12;
	public static final int CLEAR_HOLIDAY_SCHEDULE_COMMAND_ID = 0x13;
	public static final int SET_USER_TYPE_COMMAND_ID = 0x14;
	public static final int GET_USER_TYPE_COMMAND_ID = 0x15;
	public static final int SET_RFID_COMMAND_ID = 0x16;
	public static final int GET_RFID_COMMAND_ID = 0x17;
	public static final int CLEAR_RFID_COMMAND_ID = 0x18;
	public static final int CLEAR_ALL_RFIDS_COMMAND_ID = 0x19;

	public static final int LOCK_DOOR_RESPONSE_COMMAND_ID = 0x00;
	public static final int UNLOCK_DOOR_RESPONSE_COMMAND_ID = 0x01;
	public static final int TOGGLE_RESPONSE_COMMAND_ID = 0x02;
	public static final int UNLOCK_WITH_TIMEOUT_RESPONSE_COMMAND_ID = 0x03;
	public static final int GET_LOG_RECORD_RESPONSE_COMMAND_ID = 0x04;
	public static final int SET_PIN_RESPONSE_COMMAND_ID = 0x05;
	public static final int GET_PIN_RESPONSE_COMMAND_ID = 0x06;
	public static final int CLEAR_PIN_RESPONSE_COMMAND_ID = 0x07;
	public static final int CLEAR_ALL_PINS_RESPONSE_COMMAND_ID = 0x08;
	public static final int SET_USER_STATUS_RESPONSE_COMMAND_ID = 0x09;
	public static final int GET_USER_STATUS_RESPONSE_COMMAND_ID = 0x0A;
	public static final int SET_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x0B;
	public static final int GET_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x0C;
	public static final int CLEAR_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x0D;
	public static final int SET_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x0E;
	public static final int GET_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x0F;
	public static final int CLEAR_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x10;
	public static final int SET_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x11;
	public static final int GET_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x12;
	public static final int CLEAR_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID = 0x13;
	public static final int SET_USER_TYPE_RESPONSE_COMMAND_ID = 0x14;
	public static final int GET_USER_TYPE_RESPONSE_COMMAND_ID = 0x15;
	public static final int SET_RFID_RESPONSE_COMMAND_ID = 0x16;
	public static final int GET_RFID_RESPONSE_COMMAND_ID = 0x17;
	public static final int CLEAR_RFID_RESPONSE_COMMAND_ID = 0x18;
	public static final int CLEAR_ALL_RFIDS_RESPONSE_COMMAND_ID = 0x19;
	public static final int OPERATION_EVENT_NOTIFICATION_COMMAND_ID = 0x20;
	public static final int PROGRAMMING_EVENT_NOTIFICATION_COMMAND_ID = 0x21;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(LOCK_DOOR_COMMAND_ID, new ImmutablePair<>("Lock Door", LockDoor.class));
		ALL_RECEIVED.put(UNLOCK_DOOR_COMMAND_ID, new ImmutablePair<>("Unlock Door", UnlockDoor.class));
		ALL_RECEIVED.put(TOGGLE_COMMAND_ID, new ImmutablePair<>("Toggle", Toggle.class));
		ALL_RECEIVED.put(UNLOCK_WITH_TIMEOUT_COMMAND_ID, new ImmutablePair<>("Unlock With Timeout", UnlockDoor.class));
		ALL_RECEIVED.put(GET_LOG_RECORD_COMMAND_ID, new ImmutablePair<>("Get Log Record", GetLogRecord.class));
		ALL_RECEIVED.put(SET_PIN_COMMAND_ID, new ImmutablePair<>("Set Pin", SetPinCode.class));
		ALL_RECEIVED.put(GET_PIN_COMMAND_ID, new ImmutablePair<>("Get Pin", GetPinCode.class));
		ALL_RECEIVED.put(CLEAR_PIN_COMMAND_ID, new ImmutablePair<>("Clear Pin", ClearPinCode.class));
		ALL_RECEIVED.put(CLEAR_ALL_PINS_COMMAND_ID, new ImmutablePair<>("Clear All Pins", ClearAllPinCodes.class));
		ALL_RECEIVED.put(SET_USER_STATUS_COMMAND_ID, new ImmutablePair<>("Set User Status", SetUserStatus.class));
		ALL_RECEIVED.put(GET_USER_STATUS_COMMAND_ID, new ImmutablePair<>("Get User Status", GetUserStatus.class));
		ALL_RECEIVED.put(SET_WEEKDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Set Weekday Schedule", SetWeeklySchedule.class));
		ALL_RECEIVED.put(GET_WEEKDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Get Weekday Schedule", GetWeeklySchedule.class));
		ALL_RECEIVED.put(CLEAR_WEEKDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Clear Weekday Schedule", ClearWeeklySchedule.class));
		ALL_RECEIVED.put(SET_YEARDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Set Yearday Schedule", SetYearDaySchedule.class));
		ALL_RECEIVED.put(GET_YEARDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Get Yearday Schedule", GetYearDaySchedule.class));
		ALL_RECEIVED.put(CLEAR_YEARDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Clear Yearday Schedule", ClearYearDaySchedule.class));
		ALL_RECEIVED.put(SET_HOLIDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Set Holiday Schedule", SetHolidaySchedule.class));
		ALL_RECEIVED.put(GET_HOLIDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Get Holiday Schedule", GetHolidaySchedule.class));
		ALL_RECEIVED.put(CLEAR_HOLIDAY_SCHEDULE_COMMAND_ID,
				new ImmutablePair<>("Clear Holiday Schedule", ClearHolidaySchedule.class));
		ALL_RECEIVED.put(SET_USER_TYPE_COMMAND_ID, new ImmutablePair<>("Set User Type", SetUserType.class));
		ALL_RECEIVED.put(GET_USER_TYPE_COMMAND_ID, new ImmutablePair<>("Get User Type", GetUserType.class));
		ALL_RECEIVED.put(SET_RFID_COMMAND_ID, new ImmutablePair<>("Set RFID", SetRfidCode.class));
		ALL_RECEIVED.put(GET_RFID_COMMAND_ID, new ImmutablePair<>("Get RFID", GetRfidCode.class));
		ALL_RECEIVED.put(CLEAR_RFID_COMMAND_ID, new ImmutablePair<>("Clear RFID", ClearRfidCode.class));
		ALL_RECEIVED.put(CLEAR_ALL_RFIDS_COMMAND_ID, new ImmutablePair<>("Clear All RFIDs", ClearAllRfidCodes.class));

		ALL_GENERATED.put(LOCK_DOOR_RESPONSE_COMMAND_ID, "Lock Door Response");
		ALL_GENERATED.put(UNLOCK_DOOR_RESPONSE_COMMAND_ID, "Unlock Door Response");
		ALL_GENERATED.put(TOGGLE_RESPONSE_COMMAND_ID, "Toggle Response");
		ALL_GENERATED.put(UNLOCK_WITH_TIMEOUT_RESPONSE_COMMAND_ID, "Unlock With Timeout Response");
		ALL_GENERATED.put(GET_LOG_RECORD_RESPONSE_COMMAND_ID, "Get Log Record Response");
		ALL_GENERATED.put(SET_PIN_RESPONSE_COMMAND_ID, "Set Pin Response");
		ALL_GENERATED.put(GET_PIN_RESPONSE_COMMAND_ID, "Get Pin Response");
		ALL_GENERATED.put(CLEAR_PIN_RESPONSE_COMMAND_ID, "Clear Pin Response");
		ALL_GENERATED.put(CLEAR_ALL_PINS_RESPONSE_COMMAND_ID, "Clear All Pins Response");
		ALL_GENERATED.put(SET_USER_STATUS_RESPONSE_COMMAND_ID, "Set User Status Response");
		ALL_GENERATED.put(GET_USER_STATUS_RESPONSE_COMMAND_ID, "Get User Status Response");
		ALL_GENERATED.put(SET_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Set Weekday Schedule Response");
		ALL_GENERATED.put(GET_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Get Weekday Schedule Response");
		ALL_GENERATED.put(CLEAR_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Clear Weekday Schedule Response");
		ALL_GENERATED.put(SET_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Set Yearday Schedule Response");
		ALL_GENERATED.put(GET_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Get Yearday Schedule Response");
		ALL_GENERATED.put(CLEAR_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Clear Yearday Schedule Response");
		ALL_GENERATED.put(SET_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Set Holiday Schedule Response");
		ALL_GENERATED.put(GET_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Get Holiday Schedule Response");
		ALL_GENERATED.put(CLEAR_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID, "Clear Holiday Schedule Response");
		ALL_GENERATED.put(SET_USER_TYPE_RESPONSE_COMMAND_ID, "Set User Type Response");
		ALL_GENERATED.put(GET_USER_TYPE_RESPONSE_COMMAND_ID, "Get User Type Response");
		ALL_GENERATED.put(SET_RFID_RESPONSE_COMMAND_ID, "Set RFID Response");
		ALL_GENERATED.put(GET_RFID_RESPONSE_COMMAND_ID, "Get RFID Response");
		ALL_GENERATED.put(CLEAR_RFID_RESPONSE_COMMAND_ID, "Clear RFID Response");
		ALL_GENERATED.put(CLEAR_ALL_RFIDS_RESPONSE_COMMAND_ID, "Clear All RFIDs Response");
		ALL_GENERATED.put(OPERATION_EVENT_NOTIFICATION_COMMAND_ID, "Operation Event Notification");
		ALL_GENERATED.put(PROGRAMMING_EVENT_NOTIFICATION_COMMAND_ID, "Programming Event Notification");
	}
}
