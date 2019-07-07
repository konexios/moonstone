package com.arrow.kronos;

public interface KronosApiConstants {
	public final static int DEFAULT_MQTT_LISTENER_NUM_THREADS = 10;
	public final static int DEFAULT_PROCESSOR_NUM_THREADS = 10;
	public final static String DEFAULT_USER_ROLE = "Default User";

	public static final String DEVICE_TELEMETRY_EXCHANGE = "kronos.api.telemetry.topic";
	public static final String DEVICE_TELEMETRY_QUEUE = "kronos.api.telemetry";

	public static String deviceTelemetryRouting(String hid, String telemetryName) {
		return String.format("kronos.api.telemetry.%s.%s", hid, telemetryName);
	}

	public static String deviceTelemetryQueueName(String sessionId) {
		return String.format("kronos.api.telemetry.%s", sessionId);
	}

	public static String kronosTelemetryRouting(String gatewayHid) {
		return String.format("krs.tel.#.%s", gatewayHid);
	}
}
