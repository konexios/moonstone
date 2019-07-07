package com.arrow.selene;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.codec.binary.Base64;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.KeyValuePair;
import com.arrow.selene.data.Telemetry;

public class TelemetryUtils {
	public static Telemetry withString(TelemetryItemType type, String name, String value, long timestamp) {
		Telemetry result = new Telemetry(type, name, timestamp);
		result.setStrValue(value);
		return result;
	}

	public static Telemetry withInt(String name, Long value, long timestamp) {
		Telemetry result = new Telemetry(TelemetryItemType.Integer, name, timestamp);
		result.setIntValue(value);
		return result;
	}

	public static Telemetry withFloat(String name, Double value, long timestamp) {
		Telemetry result = new Telemetry(TelemetryItemType.Float, name, timestamp);
		result.setFloatValue(value);
		return result;
	}

	public static Telemetry withBoolean(String name, Boolean value, long timestamp) {
		Telemetry result = new Telemetry(TelemetryItemType.Boolean, name, timestamp);
		result.setBoolValue(value);
		return result;
	}

	public static Telemetry withDate(TelemetryItemType type, String name, LocalDate value, long timestamp) {
		Telemetry result = new Telemetry(type, name, timestamp);
		result.setDateValue(value);
		return result;
	}

	public static Telemetry withDateTime(TelemetryItemType type, String name, LocalDateTime value, long timestamp) {
		Telemetry result = new Telemetry(type, name, timestamp);
		result.setDatetimeValue(value);
		return result;
	}

	public static Telemetry withBinary(String name, byte[] value, long timestamp) {
		Telemetry result = new Telemetry(TelemetryItemType.Binary, name, timestamp);
		if (value == null) {
			value = new byte[0];
		}
		result.setStrValue(Base64.encodeBase64String(value));
		return result;
	}

	public static Telemetry withRaw(String name, String value, long timestamp) {
		KeyValuePair<TelemetryItemType, String> pair = TelemetryItemType.parse(name);
		return withString(pair.getKey(), pair.getValue(), value, timestamp);
	}
}
