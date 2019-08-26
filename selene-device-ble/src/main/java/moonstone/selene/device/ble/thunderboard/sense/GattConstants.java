package moonstone.selene.device.ble.thunderboard.sense;

public class GattConstants {
	public static final String ENABLE_NOTIFICATION = "01:00";
	public static final String DISABLE_NOTIFICATION = "00:00";

	public static final String UUID_UV_SENSOR = "00002a76-0000-1000-8000-00805f9b34fb";
	public static final String UUID_PRESSURE_SENSOR = "00002a6d-0000-1000-8000-00805f9b34fb";
	public static final String UUID_TEMPERATURE_SENSOR = "00002a6e-0000-1000-8000-00805f9b34fb";
	public static final String UUID_HUMIDITY_SENSOR = "00002a6f-0000-1000-8000-00805f9b34fb";
	public static final String UUID_AMBIENT_LIGHT_SENSOR = "c8546913-bfd9-45eb-8dde-9f8754f4a32e";
	public static final String UUID_SOUND_LEVEL_SENSOR = "c8546913-bf02-45eb-8dde-9f8754f4a32e";
	public static final String UUID_CO2_READING_SENSOR = "efd658ae-c401-ef33-76e7-91b00019103b";
	public static final String UUID_TVOC_READING_SENSOR = "efd658ae-c402-ef33-76e7-91b00019103b";
	public static final String UUID_ACCELERATION_SENSOR = "c4c1f6e2-4be5-11e5-885d-feff819cdc9f";
	public static final String UUID_ORIENTATION_SENSOR = "b7c4b694-bee3-45dd-ba9f-f3b5e994f49a";
	public static final String UUID_KEYS_SENSOR = "fcb89c40-c601-59f3-7dc3-5ece444a401b";
	public static final String UUID_RGB_LEDS = "fcb89c40-c603-59f3-7dc3-5ece444a401b";

	// // UV sensor
	// public static final String HANDLE_UV_SENSOR_VALUE = "0x001d";
	//
	// // Pressure sensor
	// public static final String HANDLE_PRESSURE_SENSOR_VALUE = "0x001f";
	//
	// // Temperature sensor
	// public static final String HANDLE_TEMP_SENSOR_VALUE = "0x0021";
	//
	// // Humidity sensor
	// public static final String HANDLE_HUM_SENSOR_VALUE = "0x0023";
	//
	// // Ambient Light sensor
	// public static final String HANDLE_AMBIENT_LIGHT_SENSOR_VALUE = "0x0025";
	//
	// // Sound sensor
	// public static final String HANDLE_SOUND_SENSOR_VALUE = "0x0027";
	//
	// // CO2 sensor
	// public static final String HANDLE_CO2_SENSOR_VALUE = "0x0030";
	//
	// // TVOC sensor
	// public static final String HANDLE_TVOC_SENSOR_VALUE = "0x0032";
	//
	// // Keys
	// public static final String HANDLE_KEYS_STATUS = "0x0038";
	//
	// // RGB LEDs
	// public static final String HANDLE_RGB_LEDS_VALUE = "0x003d";
	//
	// // Movement sensor (accelerometer and orientation)
	// public static final String HANDLE_ACC_SENSOR_VALUE = "0x004e";
	// public static final String HANDLE_ACC_SENSOR_NOTIFICATION = "0x004f";
	// public static final String HANDLE_POS_SENSOR_VALUE = "0x0051";
	// public static final String HANDLE_POS_SENSOR_NOTIFICATION = "0x0052";
	//
	// public static final String ENV_SERVICE = "/service001b/";
	// public static final String AIR_QUALITY_SERVICE = "/service002e/";
	// public static final String HARDWARE_SERVICE = "/service0036/";
	// public static final String MOV_SERVICE = "/service004c/";
	//
	// public static final String CHAR_UV_SENSOR_VALUE = "char001c";
	// public static final String CHAR_UV_VALUE = ENV_SERVICE +
	// CHAR_UV_SENSOR_VALUE;
	// public static final String CHAR_PRESSURE_SENSOR_VALUE = "char001e";
	// public static final String CHAR_PRESSURE_VALUE = ENV_SERVICE +
	// CHAR_PRESSURE_SENSOR_VALUE;
	// public static final String CHAR_TEMP_SENSOR_VALUE = "char0020";
	// public static final String CHAR_TEMP_VALUE = ENV_SERVICE +
	// CHAR_TEMP_SENSOR_VALUE;
	// public static final String CHAR_HUM_SENSOR_VALUE = "char0022";
	// public static final String CHAR_HUM_VALUE = ENV_SERVICE +
	// CHAR_HUM_SENSOR_VALUE;
	// public static final String CHAR_AMBIENT_LIGHT_SENSOR_VALUE = "char0024";
	// public static final String CHAR_AMBIENT_LIGHT_VALUE = ENV_SERVICE +
	// CHAR_AMBIENT_LIGHT_SENSOR_VALUE;
	// public static final String CHAR_SOUND_SENSOR_VALUE = "char0026";
	// public static final String CHAR_SOUND_VALUE = ENV_SERVICE +
	// CHAR_SOUND_SENSOR_VALUE;
	//
	// public static final String CHAR_CO2_SENSOR_VALUE = "char002f";
	// public static final String CHAR_CO2_VALUE = AIR_QUALITY_SERVICE +
	// CHAR_CO2_SENSOR_VALUE;
	// public static final String CHAR_TVOC_SENSOR_VALUE = "char0031";
	// public static final String CHAR_TVOC_VALUE = AIR_QUALITY_SERVICE +
	// CHAR_TVOC_SENSOR_VALUE;
	//
	// public static final String CHAR_KEYS_STATUS_VALUE = "char0037";
	// public static final String CHAR_KEYS_VALUE = HARDWARE_SERVICE +
	// CHAR_KEYS_STATUS_VALUE;
	// public static final String CHAR_RGB_LEDS_CONTROL_VALUE = "char003c";
	// public static final String CHAR_RGB_LEDS_VALUE = HARDWARE_SERVICE +
	// CHAR_RGB_LEDS_CONTROL_VALUE;
	//
	// public static final String CHAR_ACC_SENSOR_VALUE = "char004d";
	// public static final String CHAR_ACC_SENSOR_NOTIFICATION = MOV_SERVICE +
	// CHAR_ACC_SENSOR_VALUE;
	// public static final String CHAR_POS_SENSOR_VALUE = "char0050";
	// public static final String CHAR_POS_SENSOR_NOTIFICATION = MOV_SERVICE +
	// CHAR_POS_SENSOR_VALUE;
}
