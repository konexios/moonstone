package com.arrow.selene.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.SeleneException;

public class Telemetry extends EntityAbstract {
	private static final long serialVersionUID = 6392472594451306061L;

	private TelemetryItemType type;
	private Long deviceId;
	private long timestamp;
	private String name;

	private String strValue;
	private Long intValue;
	private Double floatValue;
	private Boolean boolValue;
	private LocalDate dateValue;
	private LocalDateTime datetimeValue;

	public Telemetry() {
	}

	public Telemetry(TelemetryItemType type, String name, long timestamp) {
		this.type = type;
		this.name = name;
		this.timestamp = timestamp;
	}

	public Object value() {
		switch (type) {
		case Boolean:
			return getBoolValue();
		case Date:
			return getDateValue();
		case DateTime:
			return getDatetimeValue();
		case Float:
			return getFloatValue();
		case FloatCube:
			return getStrValue();
		case FloatSquare:
			return getStrValue();
		case Integer:
			return getIntValue();
		case IntegerCube:
			return getStrValue();
		case IntegerSquare:
			return getStrValue();
		case String:
			return getStrValue();
		case System:
			return getStrValue();
		case Binary:
			return getStrValue();
		default:
			throw new SeleneException("UNSUPPORTED TYPE: " + type);
		}
	}

	public TelemetryItemType getType() {
		return type;
	}

	public void setType(TelemetryItemType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public Long getIntValue() {
		return intValue;
	}

	public void setIntValue(Long intValue) {
		this.intValue = intValue;
	}

	public Double getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Double floatValue) {
		this.floatValue = floatValue;
	}

	public Boolean getBoolValue() {
		return boolValue;
	}

	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}

	public LocalDate getDateValue() {
		return dateValue;
	}

	public void setDateValue(LocalDate dateValue) {
		this.dateValue = dateValue;
	}

	public LocalDateTime getDatetimeValue() {
		return datetimeValue;
	}

	public void setDatetimeValue(LocalDateTime datetimeValue) {
		this.datetimeValue = datetimeValue;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
}
