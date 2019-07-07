package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class HvacThermostatClusterAttributes {
	public static final int LOCAL_TEMPERATURE_ATTRIBUTE_ID = 0x0000;
	public static final int OUTDOOR_TEMPERATURE_ATTRIBUTE_ID = 0x0001;
	public static final int OCCUPANCY_ATTRIBUTE_ID = 0x0002;
	public static final int ABS_MIN_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0003;
	public static final int ABS_MAX_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0004;
	public static final int ABS_MIN_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0005;
	public static final int ABS_MAX_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0006;
	public static final int PI_COOLING_DEMAND_ATTRIBUTE_ID = 0x0007;
	public static final int PI_HEATING_DEMAND_ATTRIBUTE_ID = 0x0008;
	public static final int HVAC_SYSTEM_TYPE_CONFIGURATION_ATTRIBUTE_ID = 0x0009;

	public static final int LOCAL_TEMPERATURE_CALIBRATION_ATTRIBUTE_ID = 0x0010;
	public static final int OCCUPIED_COOLING_SETPOINT_ATTRIBUTE_ID = 0x0011;
	public static final int OCCUPIED_HEATING_SETPOINT_ATTRIBUTE_ID = 0x0012;
	public static final int UNOCCUPIED_COOLING_SETPOINT_ATTRIBUTE_ID = 0x0013;
	public static final int UNOCCUPIED_HEATING_SETPOINT_ATTRIBUTE_ID = 0x0014;
	public static final int MIN_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0015;
	public static final int MAX_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0016;
	public static final int MIN_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0017;
	public static final int MAX_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID = 0x0018;
	public static final int MIN_SETPOINT_DEAD_BAND_ATTRIBUTE_ID = 0x0019;
	public static final int REMOTE_SENSING_ATTRIBUTE_ID = 0x001a;
	public static final int CONTROL_SEQUENCE_OF_OPERATION_ATTRIBUTE_ID = 0x001b;
	public static final int SYSTEM_MODE_ATTRIBUTE_ID = 0x001c;
	public static final int THERMOSTAT_ALARM_MASK_ATTRIBUTE_ID = 0x001d;
	public static final int THERMOSTAT_RUNNING_MODE_ATTRIBUTE_ID = 0x001e;

	public static final int START_OF_WEEK_ATTRIBUTE_ID = 0x0020;
	public static final int NUMBER_OF_WEEKLY_TRANSITIONS_ATTRIBUTE_ID = 0x0021;
	public static final int NUMBER_OF_DAILY_TRANSITIONS_ATTRIBUTE_ID = 0x0022;
	public static final int TEMPERATURE_SETPOINT_HOLD_ATTRIBUTE_ID = 0x0023;
	public static final int TEMPERATURE_SETPOINT_HOLD_DURATION_ATTRIBUTE_ID = 0x0024;
	public static final int THERMOSTAT_PROGRAMMING_OPERATION_MODE_ATTRIBUTE_ID = 0x0025;
	public static final int THERMOSTAT_RUNNING_STATE_ATTRIBUTE_ID = 0x0029;
	public static final int SETPOINT_CHANGE_SOURCE_ATTRIBUTE_ID = 0x0030;
	public static final int SETPOINT_CHANGE_AMOUNT_ATTRIBUTE_ID = 0x0031;
	public static final int SETPOINT_CHANGE_SOURCE_TIMESTAMP_ATTRIBUTE_ID = 0x0032;
	public static final int AC_TYPE_ATTRIBUTE_ID = 0x0040;
	public static final int AC_CAPACITY_ATTRIBUTE_ID = 0x0041;
	public static final int AC_REFRIGERANT_TYPE_ATTRIBUTE_ID = 0x0042;
	public static final int AC_COMPRESSOR_ATTRIBUTE_ID = 0x0043;
	public static final int AC_ERROR_CODE_ATTRIBUTE_ID = 0x0044;
	public static final int AC_LOUVER_POSITION_ATTRIBUTE_ID = 0x0045;
	public static final int AC_COIL_TEMPERATURE_ATTRIBUTE_ID = 0x0046;
	public static final int AC_CAPACITY_FORMAT_ATTRIBUTE_ID = 0x0047;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(LOCAL_TEMPERATURE_ATTRIBUTE_ID, new ImmutablePair<>("Local Temperature", new Temperature()));
		ALL.put(OUTDOOR_TEMPERATURE_ATTRIBUTE_ID, new ImmutablePair<>("Outdoor Temperature", null));
		ALL.put(OCCUPANCY_ATTRIBUTE_ID, new ImmutablePair<>("Occupancy", new Occupancy()));
		ALL.put(ABS_MIN_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID,
				new ImmutablePair<>("Absolute Minimum Heat Set Point Limit", null));
		ALL.put(ABS_MAX_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID,
				new ImmutablePair<>("Absolute Maximum Heat Set Point Limit", null));
		ALL.put(ABS_MIN_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID,
				new ImmutablePair<>("Absolute Minimum Cool Set Point Limit", null));
		ALL.put(ABS_MAX_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID,
				new ImmutablePair<>("Absolute Maximum Cool Set Point Limit", null));
		ALL.put(PI_COOLING_DEMAND_ATTRIBUTE_ID, new ImmutablePair<>("PI Cooling Demand", null));
		ALL.put(PI_HEATING_DEMAND_ATTRIBUTE_ID, new ImmutablePair<>("PI Heating Demand", null));
		ALL.put(HVAC_SYSTEM_TYPE_CONFIGURATION_ATTRIBUTE_ID,
				new ImmutablePair<>("HVAC System Type Configuration", HvacSystemTypeConfiguration.COOL_STAGE1));
		ALL.put(LOCAL_TEMPERATURE_CALIBRATION_ATTRIBUTE_ID,
				new ImmutablePair<>("Local Temperature Calibration", new LocalTemperatureCalibration()));
		ALL.put(OCCUPIED_COOLING_SETPOINT_ATTRIBUTE_ID, new ImmutablePair<>("Occupied Cooling Set Point", null));
		ALL.put(OCCUPIED_HEATING_SETPOINT_ATTRIBUTE_ID, new ImmutablePair<>("Occupied Heating Set Point", null));
		ALL.put(UNOCCUPIED_COOLING_SETPOINT_ATTRIBUTE_ID, new ImmutablePair<>("Unoccupied Cooling Set Point", null));
		ALL.put(UNOCCUPIED_HEATING_SETPOINT_ATTRIBUTE_ID, new ImmutablePair<>("Unoccupied Heating Set Point", null));
		ALL.put(MIN_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Minimum Heat Set Point Limit", null));
		ALL.put(MAX_HEAT_SETPOINT_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Maximum Heat Set Point Limit", null));
		ALL.put(MIN_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Minimum Cool Set Point Limit", null));
		ALL.put(MAX_COOL_SETPOINT_LIMIT_ATTRIBUTE_ID, new ImmutablePair<>("Maximum Cool Set Point Limit", null));
		ALL.put(MIN_SETPOINT_DEAD_BAND_ATTRIBUTE_ID,
				new ImmutablePair<>("Minimum Set Point Dead Band", new MinSetpointDeadBand()));
		ALL.put(REMOTE_SENSING_ATTRIBUTE_ID,
				new ImmutablePair<>("Remote Sensing", RemoteSensing.OCCUPANCY_SENSED_REMOTELY));
		ALL.put(CONTROL_SEQUENCE_OF_OPERATION_ATTRIBUTE_ID,
				new ImmutablePair<>("Control Sequence of Operation", ControlSequenceOfOperation.RESERVED));
		ALL.put(SYSTEM_MODE_ATTRIBUTE_ID, new ImmutablePair<>("System Mode", SystemMode.RESERVED));
		ALL.put(THERMOSTAT_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Thermostat Alarm Mask", AlarmMask.HARDWARE_FAILURE));
		ALL.put(THERMOSTAT_RUNNING_MODE_ATTRIBUTE_ID,
				new ImmutablePair<>("Thermostat Running Mode", ThermostatRunningMode.RESERVED));
		ALL.put(START_OF_WEEK_ATTRIBUTE_ID, new ImmutablePair<>("Start Of Week", StartOfWeek.RESERVED));
		ALL.put(NUMBER_OF_WEEKLY_TRANSITIONS_ATTRIBUTE_ID, new ImmutablePair<>("Number Of Weekly Transitions", null));
		ALL.put(NUMBER_OF_DAILY_TRANSITIONS_ATTRIBUTE_ID, new ImmutablePair<>("Number Of Daily Transitions", null));
		ALL.put(TEMPERATURE_SETPOINT_HOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Temperature Setpoint Hold", TemperatureSetpointHold.RESERVED));
		ALL.put(TEMPERATURE_SETPOINT_HOLD_DURATION_ATTRIBUTE_ID,
				new ImmutablePair<>("Temperature Setpoint Hold Duration", null));
		ALL.put(THERMOSTAT_PROGRAMMING_OPERATION_MODE_ATTRIBUTE_ID,
				new ImmutablePair<>("Thermostat Programming Operation Mode",
						ThermostatProgrammimngOperationMode.AUTO_RECOVERY_MODE_ON));
		ALL.put(THERMOSTAT_RUNNING_STATE_ATTRIBUTE_ID,
				new ImmutablePair<>("Thermostat Running State", ThermostatRunningState.COOL_STATE_ON));
		ALL.put(SETPOINT_CHANGE_SOURCE_ATTRIBUTE_ID,
				new ImmutablePair<>("Setpoint Change Source", SetpointChangeSource.RESERVED));
		ALL.put(SETPOINT_CHANGE_AMOUNT_ATTRIBUTE_ID, new ImmutablePair<>("Setpoint Change Amount", null));
		ALL.put(SETPOINT_CHANGE_SOURCE_TIMESTAMP_ATTRIBUTE_ID,
				new ImmutablePair<>("Setpoint Change Source Timestamp", null));
		ALL.put(AC_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("AC Type", AcType.RESERVED));
		ALL.put(AC_CAPACITY_ATTRIBUTE_ID, new ImmutablePair<>("AC Capacity", null));
		ALL.put(AC_REFRIGERANT_TYPE_ATTRIBUTE_ID,
				new ImmutablePair<>("AC Refrigerant Type", AcRefrigerantType.RESERVED));
		ALL.put(AC_COMPRESSOR_ATTRIBUTE_ID, new ImmutablePair<>("AC Compressor", AcCompressorType.RESERVED));
		ALL.put(AC_ERROR_CODE_ATTRIBUTE_ID, new ImmutablePair<>("AC Error Code", AcErrorCode.FAN_FAILURE));
		ALL.put(AC_LOUVER_POSITION_ATTRIBUTE_ID, new ImmutablePair<>("AC Louver Position", AcLouverPosition.RESERVED));
		ALL.put(AC_COIL_TEMPERATURE_ATTRIBUTE_ID, new ImmutablePair<>("AC Coil Temperature", new AcCoilTemperature()));
		ALL.put(AC_CAPACITY_FORMAT_ATTRIBUTE_ID, new ImmutablePair<>("AC Capacity Format", AcCapacityFormat.BTUH));
	}
}
