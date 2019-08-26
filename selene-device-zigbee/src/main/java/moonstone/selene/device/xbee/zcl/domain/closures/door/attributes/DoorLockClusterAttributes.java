package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class DoorLockClusterAttributes {
	public static final int LOCK_STATE_ATTRIBUTE_ID = 0x0000;
	public static final int LOCK_TYPE_ATTRIBUTE_ID = 0x0001;
	public static final int ACTUATOR_ENABLED_ATTRIBUTE_ID = 0x0002;
	public static final int DOOR_STATE_ATTRIBUTE_ID = 0x0003;
	public static final int DOOR_OPEN_EVENTS_ATTRIBUTE_ID = 0x0004;
	public static final int DOOR_CLOSED_EVENTS_ATTRIBUTE_ID = 0x0005;
	public static final int OPEN_PERIOD_ATTRIBUTE_ID = 0x0006;
	public static final int NUM_LOCK_RECORDS_SUPPORTED_ATTRIBUTE_ID = 0x0010;
	public static final int NUM_TOTAL_USERS_SUPPORTED_ATTRIBUTE_ID = 0x0011;
	public static final int NUM_PIN_USERS_SUPPORTED_ATTRIBUTE_ID = 0x0012;
	public static final int NUM_RFID_USERS_SUPPORTED_ATTRIBUTE_ID = 0x0013;
	public static final int NUM_WEEKDAY_SCHEDULES_SUPPORTED_PER_USER_ATTRIBUTE_ID = 0x0014;
	public static final int NUM_YEARDAY_SCHEDULES_SUPPORTED_PER_USER_ATTRIBUTE_ID = 0x0015;
	public static final int NUM_HOLIDAY_SCHEDULES_SUPPORTED_PER_USER_ATTRIBUTE_ID = 0x0016;
	public static final int MAX_PIN_LENGTH_ATTRIBUTE_ID = 0x0017;
	public static final int MIN_PIN_LENGTH_ATTRIBUTE_ID = 0x0018;
	public static final int MAX_RFID_CODE_LENGTH_ATTRIBUTE_ID = 0x0019;
	public static final int MIN_RFID_CODE_LENGTH_ATTRIBUTE_ID = 0x001A;
	public static final int ENABLE_LOGGING_ATTRIBUTE_ID = 0x0020;
	public static final int LANGUAGE_ATTRIBUTE_ID = 0x0021;
	public static final int LED_SETTINGS_ATTRIBUTE_ID = 0x0022;
	public static final int AUTO_RELOCK_TIME_ATTRIBUTE_ID = 0x0023;
	public static final int SOUND_VOLUME_ATTRIBUTE_ID = 0x0024;
	public static final int OPERATING_MODE_ATTRIBUTE_ID = 0x0025;
	public static final int SUPPORTED_OPERATING_MODES_ATTRIBUTE_ID = 0x0026;
	public static final int DEFAULT_CONFIGURATION_REGISTER_ATTRIBUTE_ID = 0x0027;
	public static final int ENABLE_LOCAL_PROGRAMMING_ATTRIBUTE_ID = 0x0028;
	public static final int ENABLE_ONE_TOUCH_LOCKING_ATTRIBUTE_ID = 0x0029;
	public static final int ENABLE_INSIDE_STATUS_LED_ATTRIBUTE_ID = 0x002A;
	public static final int ENABLE_PRIVACY_MODE_BUTTON_ATTRIBUTE_ID = 0x002B;
	public static final int WRONG_CODE_ENTRY_LIMIT_ATTRIBUTE_ID = 0x0030;
	public static final int USER_CODE_TEMPORARY_DISABLE_TIME_ATTRIBUTE_ID = 0x0031;
	public static final int SEND_PIN_OVER_THE_AIR_ATTRIBUTE_ID = 0x0032;
	public static final int REQUIRE_PIN_FOR_RF_OPERATION_ATTRIBUTE_ID = 0x0033;
	public static final int ZIGBEE_SECURITY_LEVEL_ATTRIBUTE_ID = 0x0034;
	public static final int DOOR_LOCK_ALARM_MASK_ATTRIBUTE_ID = 0x0040;
	public static final int KEYPAD_OPERATION_EVENT_MASK_ATTRIBUTE_ID = 0x0041;
	public static final int RF_OPERATION_EVENT_MASK_ATTRIBUTE_ID = 0x0042;
	public static final int MANUAL_OPERATION_EVENT_MASK_ATTRIBUTE_ID = 0x0043;
	public static final int RFID_OPERATION_EVENT_MASK_ATTRIBUTE_ID = 0x0044;
	public static final int KEYPAD_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID = 0x0045;
	public static final int RF_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID = 0x0046;
	public static final int RFID_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID = 0x0047;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(LOCK_STATE_ATTRIBUTE_ID, new ImmutablePair<>("Lock State", LockState.LOCKED));
		ALL.put(LOCK_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Lock Type", LockType.OTHER));
		ALL.put(ACTUATOR_ENABLED_ATTRIBUTE_ID, new ImmutablePair<>("Actuator Enabled", null));
		ALL.put(DOOR_STATE_ATTRIBUTE_ID, new ImmutablePair<>("Door State", DoorState.RESERVED));
		ALL.put(DOOR_OPEN_EVENTS_ATTRIBUTE_ID, new ImmutablePair<>("Door Open", null));
		ALL.put(DOOR_CLOSED_EVENTS_ATTRIBUTE_ID, new ImmutablePair<>("Door Closed Events", null));
		ALL.put(OPEN_PERIOD_ATTRIBUTE_ID, new ImmutablePair<>("Open Period", null));
		ALL.put(NUM_LOCK_RECORDS_SUPPORTED_ATTRIBUTE_ID, new ImmutablePair<>("Number of Lock Records Supported",
				null));
		ALL.put(NUM_TOTAL_USERS_SUPPORTED_ATTRIBUTE_ID, new ImmutablePair<>("Number of Total Users Supported", null));
		ALL.put(NUM_PIN_USERS_SUPPORTED_ATTRIBUTE_ID, new ImmutablePair<>("Number of Pin Users Supported", null));
		ALL.put(NUM_RFID_USERS_SUPPORTED_ATTRIBUTE_ID, new ImmutablePair<>("Number of Rfid Users Supported", null));
		ALL.put(NUM_WEEKDAY_SCHEDULES_SUPPORTED_PER_USER_ATTRIBUTE_ID,
				new ImmutablePair<>("Number of Weekday Schedules Supported Per User", null));
		ALL.put(NUM_YEARDAY_SCHEDULES_SUPPORTED_PER_USER_ATTRIBUTE_ID,
				new ImmutablePair<>("Number of Yearday Schedules Supported Per User", null));
		ALL.put(NUM_HOLIDAY_SCHEDULES_SUPPORTED_PER_USER_ATTRIBUTE_ID,
				new ImmutablePair<>("Number of Holiday Schedules Supported Per User", null));
		ALL.put(MAX_PIN_LENGTH_ATTRIBUTE_ID, new ImmutablePair<>("Maximum Pin Length", null));
		ALL.put(MIN_PIN_LENGTH_ATTRIBUTE_ID, new ImmutablePair<>("Minimum Pin Length", null));
		ALL.put(MAX_RFID_CODE_LENGTH_ATTRIBUTE_ID, new ImmutablePair<>("Maximum RFID Code Length", null));
		ALL.put(MIN_RFID_CODE_LENGTH_ATTRIBUTE_ID, new ImmutablePair<>("Minimum RFID Code Length", null));
		ALL.put(ENABLE_LOGGING_ATTRIBUTE_ID, new ImmutablePair<>("Enable Logging", null));
		ALL.put(LANGUAGE_ATTRIBUTE_ID, new ImmutablePair<>("Language", null));
		ALL.put(LED_SETTINGS_ATTRIBUTE_ID,
				new ImmutablePair<>("LED Settings", LedSettings.NEVER_USE_LED_FOR_SIGNALIZATION));
		ALL.put(AUTO_RELOCK_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Auto Relock Time", null));
		ALL.put(SOUND_VOLUME_ATTRIBUTE_ID, new ImmutablePair<>("Sound Volume", SoundVolume.LOW_VOLUME));
		ALL.put(OPERATING_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Operating Mode", OperatingMode.NORMAL));
		ALL.put(SUPPORTED_OPERATING_MODES_ATTRIBUTE_ID,
				new ImmutablePair<>("Supported Operating Modes", SupportedOperatingModes.NORMAL_MODE_SUPPORTED));
		ALL.put(DEFAULT_CONFIGURATION_REGISTER_ATTRIBUTE_ID, new ImmutablePair<>("Default Configuration Register",
				DefaultConfigurationRegister.RF_INTERFACE_DEFAULT_ACCESS_ENABLED));
		ALL.put(ENABLE_LOCAL_PROGRAMMING_ATTRIBUTE_ID, new ImmutablePair<>("Enable Local Programming", null));
		ALL.put(ENABLE_ONE_TOUCH_LOCKING_ATTRIBUTE_ID, new ImmutablePair<>("Enable One Touch Locking", null));
		ALL.put(ENABLE_INSIDE_STATUS_LED_ATTRIBUTE_ID, new ImmutablePair<>("Enable Inside Status Led", null));
		ALL.put(ENABLE_PRIVACY_MODE_BUTTON_ATTRIBUTE_ID, new ImmutablePair<>("Enable Privacy Mode Button", null));
		ALL.put(WRONG_CODE_ENTRY_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Wrong Code Entry Limit", null));
		ALL.put(USER_CODE_TEMPORARY_DISABLE_TIME_ATTRIBUTE_ID,
				new ImmutablePair<>("User Code Temporary Disable Time", null));
		ALL.put(SEND_PIN_OVER_THE_AIR_ATTRIBUTE_ID, new ImmutablePair<>("Send Pin Over The Air", null));
		ALL.put(REQUIRE_PIN_FOR_RF_OPERATION_ATTRIBUTE_ID, new ImmutablePair<>("Require Pin For RF Operation", null));
		ALL.put(ZIGBEE_SECURITY_LEVEL_ATTRIBUTE_ID,
				new ImmutablePair<>("ZigBee Security Level", ZigBeeSecurityLevel.APS_SECURITY));
		ALL.put(DOOR_LOCK_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Door Lock Alarm Mask", DoorLockAlarmMask.RESERVED));
		ALL.put(KEYPAD_OPERATION_EVENT_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Keypad Operation Event Mask", KeypadOperationEventMask.LOCK_SOURCE_KEYPAD));
		ALL.put(RF_OPERATION_EVENT_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("RF Operation Event Mask", RfOperationEventMask.LOCK_SOURCE_RF));
		ALL.put(MANUAL_OPERATION_EVENT_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Manual Operation Event Mask", ManualOperationEventMask.KEY_LOCK));
		ALL.put(RFID_OPERATION_EVENT_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("RFID Operation Event Mask", RfidOperationEventMask.LOCK_SOURCE_RFID));
		ALL.put(KEYPAD_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID, new ImmutablePair<>("Keypad Programming Event Mask",
				KeypadProgrammingEventMask.PIN_ADDED_SOURCE_KEYPAD));
		ALL.put(RF_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("RF Programming Event Mask", RfProgrammingEventMask.PIN_ADDED_SOURCE_RF));
		ALL.put(RFID_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("RFID Programming Event Mask", RfidProgrammingEventMask.RESERVED1));
	}
}
