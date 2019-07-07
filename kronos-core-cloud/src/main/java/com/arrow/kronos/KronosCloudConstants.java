package com.arrow.kronos;

public interface KronosCloudConstants {
	public static interface Aws {
		public final static String DEFAULT_POLICY_NAME = "iotconnect-default-policy";
		public final static String DEFAULT_POLICY_FILE = "/aws/iotconnect-default-policy.json";
	}

	public static interface Ibm {
		public final static String DEFAULT_GATEWAY_DEVICE_TYPE = "iotconnect-gateway-device-type";
		public final static String HEADER_ORGANIZATION_ID = "Organization-ID";
		public final static String HEADER_CLIENT_ID = "id";
		public final static String HEADER_AUTHENTICATION_MODE = "Authentication-Method";
		public final static String HEADER_AUTHENTICATION_MODE_API_KEY = "apikey";
		public final static String HEADER_AUTHENTICATION_MODE_TOKEN = "token";
		public final static String HEADER_API_KEY = "API-Key";
		public final static String HEADER_AUTHENTICATION_TOKEN = "Authentication-Token";
		public final static String HEADER_GATEWAY_TYPE = "Gateway-Type";
		public final static String HEADER_GATEWAY_ID = "Gateway-ID";
	}

	public static interface Azure {
		public final static int DEFAULT_NUM_PARTITIONS = 4;
	}

	public static interface LiveTelemetryStreaming {
		public final static boolean DEFAULT_ENABLED = false;
		public final static long DEFAULT_RETENTION_SECS = 24 * 60 * 60; // 1 day
	}

	public static interface DeviceType {
		public final static String DEFAULT_GATEWAY_TYPE = "default-gateway";
	}
}
