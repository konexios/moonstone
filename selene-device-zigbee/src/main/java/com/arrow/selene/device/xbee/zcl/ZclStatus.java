package com.arrow.selene.device.xbee.zcl;

import java.util.HashMap;
import java.util.Map;

public class ZclStatus {
	public static final int SUCCESS = 0x00;
	public static final int FAILURE = 0x01;
	public static final int NOT_AUTHORIZED = 0x7e;
	public static final int RESERVED_FIELD_NOT_ZERO = 0x7f;
	public static final int MALFORMED_COMMAND = 0x80;
	public static final int UNSUP_CLUSTER_COMMAND = 0x81;
	public static final int UNSUP_GENERAL_COMMAND = 0x82;
	public static final int UNSUP_MANUF_CLUSTER_COMMAND = 0x83;
	public static final int UNSUP_MANUF_GENERAL_COMMAND = 0x84;
	public static final int INVALID_FIELD = 0x85;
	public static final int UNSUPPORTED_ATTRIBUTE = 0x86;
	public static final int INVALID_VALUE = 0x87;
	public static final int READ_ONLY = 0x88;
	public static final int INSUFFICIENT_SPACE = 0x89;
	public static final int DUPLICATE_EXISTS = 0x8a;
	public static final int NOT_FOUND = 0x8b;
	public static final int UNREPORTABLE_ATTRIBUTE = 0x8c;
	public static final int INVALID_DATA_TYPE = 0x8d;
	public static final int INVALID_SELECTOR = 0x8e;
	public static final int WRITE_ONLY = 0x8f;
	public static final int INCONSISTENT_STARTUP_STATE = 0x90;
	public static final int DEFINED_OUT_OF_BAND = 0x91;
	public static final int INCONSISTENT = 0x92;
	public static final int ACTION_DENIED = 0x93;
	public static final int TIMEOUT = 0x94;
	public static final int ABORT = 0x95;
	public static final int INVALID_IMAGE = 0x96;
	public static final int WAIT_FOR_DATA = 0x97;
	public static final int NO_IMAGE_AVAILABLE = 0x98;
	public static final int REQUIRE_MORE_IMAGE = 0x99;
	public static final int HARDWARE_FAILURE = 0xc0;
	public static final int SOFTWARE_FAILURE = 0xc1;
	public static final int CALIBRATION_ERROR = 0xc2;

	private static final Map<Integer, String> ALL = new HashMap<>();

	static {
		ALL.put(SUCCESS, "Success");
		ALL.put(FAILURE, "Failure");
		ALL.put(NOT_AUTHORIZED, "Not authorized");
		ALL.put(RESERVED_FIELD_NOT_ZERO, "Reserved field not zero");
		ALL.put(MALFORMED_COMMAND, "Malformed command");
		ALL.put(UNSUP_CLUSTER_COMMAND, "Unsupported cluster command");
		ALL.put(UNSUP_GENERAL_COMMAND, "Unsupported general command");
		ALL.put(UNSUP_MANUF_CLUSTER_COMMAND, "Unsupported manufacturer cluster command");
		ALL.put(UNSUP_MANUF_GENERAL_COMMAND, "Unsupported manufacturer general command");
		ALL.put(INVALID_FIELD, "Invalid field");
		ALL.put(UNSUPPORTED_ATTRIBUTE, "Unsupported attribute");
		ALL.put(INVALID_VALUE, "Invalid value");
		ALL.put(READ_ONLY, "Read only");
		ALL.put(INSUFFICIENT_SPACE, "Insufficient space");
		ALL.put(DUPLICATE_EXISTS, "Duplicate exits");
		ALL.put(NOT_FOUND, "Not found");
		ALL.put(UNREPORTABLE_ATTRIBUTE, "Unreportable attribute");
		ALL.put(INVALID_DATA_TYPE, "Invalid data type");
		ALL.put(INVALID_SELECTOR, "Invalid selector");
		ALL.put(WRITE_ONLY, "Write only");
		ALL.put(INCONSISTENT_STARTUP_STATE, "Inconsistent startup state");
		ALL.put(DEFINED_OUT_OF_BAND, "Defined out of band");
		ALL.put(INCONSISTENT, "Inconsistent");
		ALL.put(ACTION_DENIED, "Action denied");
		ALL.put(TIMEOUT, "Timeout");
		ALL.put(ABORT, "Abort");
		ALL.put(INVALID_IMAGE, "Invalid image");
		ALL.put(WAIT_FOR_DATA, "Wait for data");
		ALL.put(NO_IMAGE_AVAILABLE, "No image available");
		ALL.put(REQUIRE_MORE_IMAGE, "Require more image");
		ALL.put(HARDWARE_FAILURE, "Hardware failure");
		ALL.put(SOFTWARE_FAILURE, "Software failure");
		ALL.put(CALIBRATION_ERROR, "Calibration error");
	}

	public static String getName(Integer id) {
		return ALL.getOrDefault(id, "Unknown ZCL status");
	}
}
