package com.arrow.widget;

public interface WidgetConstants {

	public static final int DEFAULT_REFRESH_RATE = 1;
	public static final int DEFAULT_MINIMUM_VALUE = 0;
	public static final int DEFAULT_MAXIMUM_VALUE = 100;
	public static final boolean DEFAULT_USE_TELEMETRY_NAME = true;
	public static final int DEFAULT_DISPLAY_INTERVAL = 60000;

	public interface Configuration {

		public interface Name {
			public static final String DEFAULT = "DEFAULT";
			public static final String SINGLE_DEVICE = "SINGLE_DEVICE";
			public static final String TEXT_WIDGET = "TEXT_WIDGET";
			public static final String DEVICE_LAST_TELEMETRY_WIDGET = "DEVICE_LAST_TELEMETRY_WIDGET";
			public static final String DEVICE_TELEMETRY_GAUGE_WIDGET = "DEVICE_TELEMETRY_GAUGE_WIDGET";
			public static final String DEVICE_ACCELERATION_CUBE_WIDGET = "DEVICE_ACCELERATION_CUBE_WIDGET";
			public static final String DEVICE_LAST_LOCATION_WIDGET = "DEVICE_LAST_LOCATION_WIDGET";
			public static final String DEVICE_TELEMETRY_LINE_CHART_WIDGET = "DEVICE_TELEMETRY_LINE_CHART_WIDGET";
			public static final String LOGO_WIDGET = "LOGO_WIDGET";
			public static final String SOCIAL_EVENT_REGISTRATION_WIDGET = "SOCIAL_EVENT_REGISTRATION_WIDGET";
		}

		public interface Label {
			public static final String DEFAULT = "Widget Configuration";
			public static final String SINGLE_DEVICE = "Single Device Configuration";
			public static final String TEXT_WIDGET = "Text Configuration";
			public static final String DEVICE_LAST_TELEMETRY_WIDGET = "Single Device Last Telemetry Configuration";
			public static final String DEVICE_TELEMETRY_GAUGE_WIDGET = "Device Telemetry Gauge Widget";
			public static final String DEVICE_ACCELERATION_CUBE_WIDGET = "Device Acceleration Cube Configuration";
			public static final String DEVICE_LAST_LOCATION_WIDGET = "Single Device Last Location Configuration";
			public static final String DEVICE_TELEMETRY_LINE_CHART_WIDGET = "Single Device Telemetry Line Chart";
			public static final String LOGO_WIDGET = "Logo Widget Configuration";
			public static final String SOCIAL_EVENT_REGISTRATION_WIDGET = "Social Event Registration Statictics Widget";
		}
	}

	public interface Page {

		public interface Name {
			public static final String DEFAULT = "DEFAULT";
			public static final String SETTINGS = "SETTINGS_PAGE";

			public static final String SINGLE_DEVICE_SELECTION = "SINGLE_DEVICE_SELECTION";
			public static final String SINGLE_TELEMETRY_SELECTION = "SINGLE_TELEMETRY_SELECTION";
			public static final String TEXT = "TEXT";
			public static final String LOGO_URL = "LOGO_URL";
			public static final String SINGLE_SOCIAL_EVENT_SELECTION = "SINGLE_SOCIAL_EVENT_SELECTION";
			public static final String TIME_ZONE_SELECTION = "TIME_ZONE_SELECTION";
		}

		public interface Label {
			public static final String DEFAULT = "General";
			public static final String SETTINGS = "Settings";

			public static final String SINGLE_DEVICE_SELECTION = "Device";
			public static final String SINGLE_TELEMETRY_SELECTION = "Telemetry";
			public static final String TEXT = "Content";
			public static final String LOGO_URL = "Logo Url";
			public static final String SINGLE_SOCIAL_EVENT_SELECTION = "Social Event";
			public static final String TIME_ZONE_SELECTION = "Time Zone";
		}
	}

	public interface Property {

		public interface Name {
			public static final String DEVICE_UID = "DEVICE_UID";
			public static final String GATEWAY_UID = "GATEWAY_UID";
			public static final String DEVICE_HID = "DEVICE_HID";
			public static final String GATEWAY_HID = "GATEWAY_HID";
			public static final String SINGLE_TELEMETRY_SELECTION = "TELEMETRY_NAME";

			public static final String USE_TELEMETRY_RAW_NAME = "USE_TELEMETRY_RAW_NAME";
			public static final String TEXT = "TEXT";

			public static final String LOGO_URL = "LOGO_URL";
			
			public static final String SOCIAL_EVENT_ID = "SOCIAL_EVENT_ID";
			public static final String TIME_ZONE_ID = "TIME_ZONE_ID";
			
			// settings
			public static final String REFRESH_RATE = "REFRESH_RATE";
			public static final String MIN_VALUE = "MINIMUM_VALUE";
			public static final String MAX_VALUE = "MAXIMUM_VALUE";
			public static final String DISPLAY_INTERVAL = "DISPLAY_INTERVAL";
		}

		public interface Label {
			public static final String UID = "UID";
			public static final String HID = "HID";
			public static final String DEVICE = "Device";
			public static final String GATEWAY = "Gateway";
			public static final String SINGLE_TELEMETRY_SELECTION = "Telemetry";

			public static final String USE_TELEMETRY_RAW_NAME = "Telemetry Name";
			public static final String CONTENT = "Content";

			public static final String LOGO_URL = "Url of picture";
			
			public static final String SOCIAL_EVENT = "Social Event";
			public static final String TIME_ZONE_ID = "Time Zone";
			
			// settings
			public static final String REFRESH_RATE = "Refresh Rate";
			public static final String MIN_VALUE = "Minimum Value";
			public static final String MAX_VALUE = "Maximum Value";
			public static final String DISPLAY_INTERVAL = "Display Last";
		}
	}
}
