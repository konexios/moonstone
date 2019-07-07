package com.arrow.kronos;

public interface McKinleyConstants {

	public static final String[] APPLICATION_NAMES = new String[] {"McKinley Demo"};
	public static final String MCKINLEY_TRAVEL_TIME = "mckinley-travel-time";
	public static final String MCKINLEY_PREVIOUS_VALUE = "mckinley-previous-value";
	public static final String MCKINLEY_MAINT_COUNT = "mckinley-maint-count";

	public interface AuditLog {
		public static String ASSET_ACTIVITY_CHANGED = "McKinleyAssetActivityChanged";
		public static String ALERT = "McKinleyAlert";
		public static String CYCLE = "McKinleyCycle";
	}

	public interface Telemetry {
		public static String[] ASSET_ACTIVITY_TELEMETRY_NAME = new String[] { "MSG_AUTOQUIP_DOOR1",
		        "MSG_AUTOQUIP_DOOR2", "MSG_AUTOQUIP_VRC_SAFETY_BAR", "MSG_AUTOQUIP_VRC_UP_PRESSURE_SWITCH",
		        "MSG_AUTOQUIP_VRC_MOVING_DOWN", "MSG_AUTOQUIP_LEVEL", "MSG_AUTOQUIP_VRC_EBTN",
		        "MSG_AUTOQUIP_VRC_ANTI_CREEP_SWITCH", "MSG_AUTOQUIP_PRESSURE_UP", "MSG_AUTOQUIP_PRESSURE_DOWN",
		        "MSG_AUTOQUIP_PRESSURE_RESTING", "MSG_AUTOQUIP_PRESSURE_LEVEL1", "MSG_AUTOQUIP_PRESSURE_LEVEL2",
		        "PRESS_MOVING_UP", "PRESS_MOVING_DOWN", "PRESS_RESTING_BTW", "PRESS_LEVEL_1", "PRESS_LEVEL_2",
		        "PRESS_HEARTBEAT", "PRESS_DELTA_CHANGE", "MSG_EVENT_SENSOR_ALIVE" };

		public static String MSG_AWS_IMAGE_DATA = "MSG_AWS_IMAGE_DATA";

		public interface PLCData {
			public static String ADC_VOLTAGE_RSP_BOARDTYPE = "ADC_VOLTAGE_RSP_BOARDTYPE";
			public static String ADC_VOLTAGE_RSP_STATUS = "ADC_VOLTAGE_RSP_STATUS";
			public static String ADC_VOLTAGE_RSP_TIMESTAMP = "ADC_VOLTAGE_RSP_TIMESTAMP";
			public static String ADC_VOLTAGE_RSP_VOLTAGES = "ADC_VOLTAGE_RSP_VOLTAGES";
		}

		public interface AssetActivity {
			public static String MSG_AUTOQUIP_VRC_SAFETY_BAR = "MSG_AUTOQUIP_VRC_SAFETY_BAR";
			public static String MSG_AUTOQUIP_VRC_EBTN = "MSG_AUTOQUIP_VRC_EBTN";
			public static String MSG_AUTOQUIP_LEVEL = "MSG_AUTOQUIP_LEVEL";
			public static String MSG_AUTOQUIP_DOOR1 = "MSG_AUTOQUIP_DOOR1";
			public static String EBTN_GATE = "EBTN_GATE";
			public static String MSG_AUTOQUIP_VRC_UP_PRESSURE_SWITCH = "MSG_AUTOQUIP_VRC_UP_PRESSURE_SWITCH";
			public static String MSG_AUTOQUIP_VRC_ANTI_CREEP_SWITCH = "MSG_AUTOQUIP_VRC_ANTI_CREEP_SWITCH";
			public static String MSG_AUTOQUIP_DOOR2 = "MSG_AUTOQUIP_DOOR2";
			public static String MSG_AUTOQUIP_VRC_MOVING_DOWN = "MSG_AUTOQUIP_VRC_MOVING_DOWN";
			public static String MSG_AUTOQUIP_VRC_EBTN_DOOR = "MSG_AUTOQUIP_VRC_EBTN_DOOR";

			public static String UP = "UP";
			public static String PROGRAM_START = "PROGRAM_START";
			public static String SENSOR_ALIVE = "SENSOR_ALIVE";
			public static String DOWN = "DOWN";
			public static String MSG_AUTOQUIP_PRESSURE_UP = "MSG_AUTOQUIP_PRESSURE_UP";
			public static String MSG_AUTOQUIP_PRESSURE_DOWN = "MSG_AUTOQUIP_PRESSURE_DOWN";
			public static String MSG_AUTOQUIP_PRESSURE_RESTING = "MSG_AUTOQUIP_PRESSURE_RESTING";
			public static String MSG_AUTOQUIP_PRESSURE_LEVEL1 = "MSG_AUTOQUIP_PRESSURE_LEVEL1";
			public static String MSG_AUTOQUIP_PRESSURE_LEVEL2 = "MSG_AUTOQUIP_PRESSURE_LEVEL2";
			public static String MSG_AUTOQUIP_UP_BUTTON = "MSG_AUTOQUIP_UP_BUTTON";
			public static String MSG_AUTOQUIP_DOWN_BUTTON = "MSG_AUTOQUIP_DOWN_BUTTON";

			/**
			 * New requirement from Monday, July 17, 2017 1:55 PM From: Sean
			 * Nguyen
			 * 
			 * We're adding seven new pressure telemetries with data values
			 * that'll all be sent as floats; could you add handling for these
			 * messages on the demo app? Telemetries and Descriptions:
			 * 
			 * "PRESS_HEARTBEAT" -- Pressure measurement sent on a timer,
			 * regardless of state<br>
			 * "PRESS_DELTA_CHANGE" -- Pressure measurement sent after detecting
			 * change in pressure greater than delta<br>
			 * "PRESS_MOVING_UP" -- Pressure while moving up <br>
			 * "PRESS_MOVING_DOWN" -- Pressure while moving down <br>
			 * "PRESS_LEVEL_1" -- Pressure while on level 1 <br>
			 * "PRESS_LEVEL_2" -- Pressure while on level 2<br>
			 * "PRESS_RESTING_BTW" -- Pressure while resting between levels<br>
			 * 
			 * In the long term, we'll be using this new format for telemetry
			 * messages, but for now, we'd like to use these in conjunction with
			 * the old "MSG_AUTOQUIP______" pressure telemetries.
			 * 
			 */
			public static String PRESS_HEARTBEAT = "PRESS_HEARTBEAT";
			public static String PRESS_DELTA_CHANGE = "PRESS_DELTA_CHANGE";
			public static String PRESS_MOVING_UP = "PRESS_MOVING_UP";
			public static String PRESS_MOVING_DOWN = "PRESS_MOVING_DOWN";
			public static String PRESS_LEVEL_1 = "PRESS_LEVEL_1";
			public static String PRESS_LEVEL_2 = "PRESS_LEVEL_2";
			public static String PRESS_RESTING_BTW = "PRESS_RESTING_BTW";

			public static String MSG_EVENT_SENSOR_ALIVE = "MSG_EVENT_SENSOR_ALIVE";
		}
	}

}
