package com.arrow.selene.device.ble.sensortag;

public class SensorTagGattConstants {
	public static final String UUID_TEMPERATURE_SENSOR_DATA = "f000aa01-0451-4000-b000-000000000000";
	public static final String UUID_TEMPERATURE_SENSOR_CONFIG = "f000aa02-0451-4000-b000-000000000000";
	public static final String UUID_TEMPERATURE_SENSOR_PERIOD = "f000aa03-0451-4000-b000-000000000000";

	// public static final String UUID_ACCELEROMETER_SENSOR_DATA =
	// "f000aa11-0451-4000-b000-000000000000";
	// public static final String UUID_ACCELEROMETER_SENSOR_CONFIG =
	// "f000aa12-0451-4000-b000-000000000000";
	// public static final String UUID_ACCELEROMETER_SENSOR_PERIOD =
	// "f000aa13-0451-4000-b000-000000000000";

	public static final String UUID_HUMIDITY_SENSOR_DATA = "f000aa21-0451-4000-b000-000000000000";
	public static final String UUID_HUMIDITY_SENSOR_CONFIG = "f000aa22-0451-4000-b000-000000000000";
	public static final String UUID_HUMIDITY_SENSOR_PERIOD = "f000aa23-0451-4000-b000-000000000000";

	// public static final String UUID_MAGNETOMETER_SENSOR_DATA =
	// "f000aa31-0451-4000-b000-000000000000";
	// public static final String UUID_MAGNETOMETER_SENSOR_CONFIG =
	// "f000aa32-0451-4000-b000-000000000000";
	// public static final String UUID_MAGNETOMETER_SENSOR_PERIOD =
	// "f000aa33-0451-4000-b000-000000000000";

	public static final String UUID_BAROMETER_PRESSURE_SENSOR_DATA = "f000aa41-0451-4000-b000-000000000000";
	public static final String UUID_BAROMETER_PRESSURE_SENSOR_CONFIG = "f000aa42-0451-4000-b000-000000000000";
	public static final String UUID_BAROMETER_PRESSURE_SENSOR_PERIOD = "f000aa44-0451-4000-b000-000000000000";

	// public static final String UUID_GYROSCOPE_SENSOR_DATA =
	// "f000aa51-0451-4000-b000-000000000000";
	// public static final String UUID_GYROSCOPE_SENSOR_CONFIG =
	// "f000aa52-0451-4000-b000-000000000000";
	// public static final String UUID_GYROSCOPE_SENSOR_PERIOD =
	// "f000aa53-0451-4000-b000-000000000000";

	public static final String UUID_SIMPLE_KEYS_SENSOR_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";

	public static final String UUID_OPTICAL_SENSOR_DATA = "f000aa71-0451-4000-b000-000000000000";
	public static final String UUID_OPTICAL_SENSOR_CONFIG = "f000aa72-0451-4000-b000-000000000000";
	public static final String UUID_OPTICAL_SENSOR_PERIOD = "f000aa73-0451-4000-b000-000000000000";

	public static final String UUID_MOVEMENT_SENSOR_DATA = "f000aa81-0451-4000-b000-000000000000";
	public static final String UUID_MOVEMENT_SENSOR_CONFIG = "f000aa82-0451-4000-b000-000000000000";
	public static final String UUID_MOVEMENT_SENSOR_PERIOD = "f000aa83-0451-4000-b000-000000000000";

	public static final String ENABLE_NOTIFICATION = "01:00";
	public static final String DISABLE_NOTIFICATION = "00:00";

	// // Temperature Sensor
	// public static final String HANDLE_TEMP_SENSOR_VALUE = "0x0021";
	// public static final String HANDLE_TEMP_SENSOR_NOTIFICATION = "0x0022";
	// public static final String HANDLE_TEMP_SENSOR_ENABLE = "0x0024";
	// public static final String HANDLE_TEMP_SENSOR_PERIOD = "0x0026";
	//
	// // Humidity sensor
	// public static final String HANDLE_HUM_SENSOR_VALUE = "0x0029";
	// public static final String HANDLE_HUM_SENSOR_NOTIFICATION = "0x002a";
	// public static final String HANDLE_HUM_SENSOR_ENABLE = "0x002c";
	// public static final String HANDLE_HUM_SENSOR_PERIOD = "0x002E";
	//
	// // Pressure sensor
	// public static final String HANDLE_PRE_SENSOR_VALUE = "0x0031";
	// public static final String HANDLE_PRE_SENSOR_NOTIFICATION = "0x0032";
	// public static final String HANDLE_PRE_SENSOR_ENABLE = "0x0034";
	// public static final String HANDLE_PRE_SENSOR_PERIOD = "0x0036";
	//
	// // Keys
	// public static final String HANDLE_KEYS_STATUS = "0x0049";
	// public static final String HANDLE_KEYS_NOTIFICATION = "0x004A";
	//
	// // Ambient Light sensor
	// public static final String HANDLE_OPTO_SENSOR_VALUE = "0x0041";
	// public static final String HANDLE_OPTO_SENSOR_NOTIFICATION = "0x0042";
	// public static final String HANDLE_OPTO_SENSOR_ENABLE = "0x0044";
	// public static final String HANDLE_OPTO_SENSOR_PERIOD = "0x0046";
	//
	// // Movement sensor (accelerometer, gyroscope and magnetometer)
	// public static final String HANDLE_MOV_SENSOR_VALUE = "0x0039";
	// public static final String HANDLE_MOV_SENSOR_NOTIFICATION = "0x003A";
	// public static final String HANDLE_MOV_SENSOR_ENABLE = "0x003C";
	// public static final String HANDLE_MOV_SENSOR_PERIOD = "0x003E";

	// // Accelerometer sensor
	// public static final UUID UUID_ACC_SENSOR_VALUE =
	// UUID.fromString("f000aa11-0451-4000-b000-000000000000");
	// public static final UUID UUID_ACC_SENSOR_ENABLE =
	// UUID.fromString("f000aa12-0451-4000-b000-000000000000");
	// public static final UUID UUID_ACC_SENSOR_PERIOD =
	// UUID.fromString("f000aa13-0451-4000-b000-000000000000");
	//
	// // Gyroscope sensor
	// public static final UUID UUID_GYR_SENSOR_VALUE =
	// UUID.fromString("f000aa51-0451-4000-b000-000000000000");
	// public static final UUID UUID_GYR_SENSOR_ENABLE =
	// UUID.fromString("f000aa52-0451-4000-b000-000000000000");
	// public static final UUID UUID_GYR_SENSOR_PERIOD =
	// UUID.fromString("f000aa53-0451-4000-b000-000000000000");
	//
	// // Magnetometer sensor
	// public static final UUID UUID_MAG_SENSOR_VALUE =
	// UUID.fromString("f000aa31-0451-4000-b000-000000000000");
	// public static final UUID UUID_MAG_SENSOR_ENABLE =
	// UUID.fromString("f000aa32-0451-4000-b000-000000000000");
	// public static final UUID UUID_MAG_SENSOR_PERIOD =
	// UUID.fromString("f000aa33-0451-4000-b000-000000000000");
	//
	// public static final String TEMP_SERVICE = "/service001f/";
	// public static final String HUM_SERVICE = "/service0027/";
	// public static final String PRE_SERVICE = "/service002f/";
	// public static final String MOV_SERVICE = "/service0037/";
	// public static final String OPTO_SERVICE = "/service003f/";
	//
	// public static final String CHAR_TEMP_SENSOR_VALUE = "char0020";
	// public static final String CHAR_TEMP_SENSOR_NOTIFICATION = TEMP_SERVICE +
	// CHAR_TEMP_SENSOR_VALUE;
	// public static final String CHAR_HUM_SENSOR_VALUE = "char0028";
	// public static final String CHAR_HUM_SENSOR_NOTIFICATION = HUM_SERVICE +
	// CHAR_HUM_SENSOR_VALUE;
	// public static final String CHAR_PRE_SENSOR_VALUE = "char0030";
	// public static final String CHAR_PRE_SENSOR_NOTIFICATION = PRE_SERVICE +
	// CHAR_PRE_SENSOR_VALUE;
	// public static final String CHAR_MOV_SENSOR_VALUE = "char0038";
	// public static final String CHAR_MOV_SENSOR_NOTIFICATION = MOV_SERVICE +
	// CHAR_MOV_SENSOR_VALUE;
	// public static final String CHAR_OPTO_SENSOR_VALUE = "char0040";
	// public static final String CHAR_OPTO_SENSOR_NOTIFICATION = OPTO_SERVICE +
	// CHAR_OPTO_SENSOR_VALUE;
	// public static final String CHAR_KEYS_STATUS = "char0048";
	// public static final String CHAR_KEYS_NOTIFICATION = "/service0047/" +
	// CHAR_KEYS_STATUS;
	// public static final String CHAR_TEMP_SENSOR_ENABLE = TEMP_SERVICE +
	// "char0023";
	// public static final String CHAR_HUM_SENSOR_ENABLE = HUM_SERVICE +
	// "char002b";
	// public static final String CHAR_PRE_SENSOR_ENABLE = PRE_SERVICE +
	// "char0033";
	// public static final String CHAR_MOV_SENSOR_ENABLE = MOV_SERVICE +
	// "char003b";
	// public static final String CHAR_OPTO_SENSOR_ENABLE = OPTO_SERVICE +
	// "char0043";
	// public static final String CHAR_TEMP_SENSOR_PERIOD = TEMP_SERVICE +
	// "char0025";
	// public static final String CHAR_HUM_SENSOR_PERIOD = HUM_SERVICE +
	// "char002d";
	// public static final String CHAR_PRE_SENSOR_PERIOD = PRE_SERVICE +
	// "char0035";
	// public static final String CHAR_MOV_SENSOR_PERIOD = MOV_SERVICE +
	// "char003d";
	// public static final String CHAR_OPTO_SENSOR_PERIOD = OPTO_SERVICE +
	// "char0045";
}
