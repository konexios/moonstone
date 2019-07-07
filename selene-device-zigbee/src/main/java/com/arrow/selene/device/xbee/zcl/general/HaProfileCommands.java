package com.arrow.selene.device.xbee.zcl.general;

import java.util.HashMap;
import java.util.Map;

public class HaProfileCommands {
	public static final int READ_ATTRIBUTES = 0x00;
	public static final int READ_ATTRIBUTES_RSP = 0x01;
	public static final int WRITE_ATTRIBUTES = 0x02;
	public static final int WRITE_ATTRIBUTES_UNDIVIDED = 0x03;
	public static final int WRITE_ATTRIBUTES_RSP = 0x04;
	public static final int WRITE_ATTRIBUTES_NO_RESPONSE = 0x05;
	public static final int CONFIGURE_REPORTING = 0x06;
	public static final int CONFIGURE_REPORTING_RSP = 0x07;
	public static final int READ_REPORTING_CONFIGURATION = 0x08;
	public static final int READ_REPORTING_CONFIGURATION_RSP = 0x09;
	public static final int REPORT_ATTRIBUTES = 0x0A;
	public static final int DEFAULT_RSP = 0x0B;
	public static final int DISCOVER_ATTRIBUTES = 0x0C;
	public static final int DISCOVER_ATTRIBUTES_RSP = 0x0D;
	public static final int READ_ATTRIBUTES_STRUCTURED = 0x0E;
	public static final int WRITE_ATTRIBUTES_STRUCTURED = 0x0F;
	public static final int WRITE_ATTRIBUTES_STRUCTURED_RSP = 0x10;
	public static final int DISCOVER_COMMANDS_RECEIVED = 0x11;
	public static final int DISCOVER_COMMANDS_RECEIVED_RSP = 0x12;
	public static final int DISCOVER_COMMANDS_GENERATED = 0x13;
	public static final int DISCOVER_COMMANDS_GENERATED_RSP = 0x14;
	public static final int DISCOVER_ATTRIBUTES_EXTENDED = 0x15;
	public static final int DISCOVER_ATTRIBUTES_EXTENDED_RSP = 0x16;

	private static final Map<Integer, String> ALL = new HashMap<>();

	static {
		ALL.put(READ_ATTRIBUTES, "Read attributes");
		ALL.put(READ_ATTRIBUTES_RSP, "Read attributes response");
		ALL.put(WRITE_ATTRIBUTES, "Write attributes");
		ALL.put(WRITE_ATTRIBUTES_UNDIVIDED, "Write attributes undivided");
		ALL.put(WRITE_ATTRIBUTES_RSP, "Write attributes response");
		ALL.put(WRITE_ATTRIBUTES_NO_RESPONSE, "Write attributes no response");
		ALL.put(CONFIGURE_REPORTING, "Configure reporting");
		ALL.put(CONFIGURE_REPORTING_RSP, "Configure reporting response");
		ALL.put(READ_REPORTING_CONFIGURATION, "Read reporting configuration");
		ALL.put(READ_REPORTING_CONFIGURATION_RSP, "Read reporting configuration response");
		ALL.put(REPORT_ATTRIBUTES, "Report attributes");
		ALL.put(DEFAULT_RSP, "Default response");
		ALL.put(DISCOVER_ATTRIBUTES, "Discover attributes");
		ALL.put(DISCOVER_ATTRIBUTES_RSP, "Discover attributes response");
		ALL.put(READ_ATTRIBUTES_STRUCTURED, "Read attributes structured");
		ALL.put(WRITE_ATTRIBUTES_STRUCTURED, "Write attributes structured");
		ALL.put(WRITE_ATTRIBUTES_STRUCTURED_RSP, "Write attributes structured response");
		ALL.put(DISCOVER_COMMANDS_RECEIVED, "Discover commands received");
		ALL.put(DISCOVER_COMMANDS_RECEIVED_RSP, "Discover commands received response");
		ALL.put(DISCOVER_COMMANDS_GENERATED, "Discover commands generated");
		ALL.put(DISCOVER_COMMANDS_GENERATED_RSP, "Discover commands generated response");
		ALL.put(DISCOVER_ATTRIBUTES_EXTENDED, "Discover attributes extended");
		ALL.put(DISCOVER_ATTRIBUTES_EXTENDED_RSP, "Discover attributes extended response");
	}

	public static String getName(Integer id) {
		return ALL.getOrDefault(id, "Unknown HA profile command");
	}
}
