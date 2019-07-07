package com.arrow.widget.model;

import java.io.Serializable;

public class DeviceLastTelemetryItemModel implements Serializable {
	private static final long serialVersionUID = -1020755507363063596L;

	private String name;
	private String value;
	private long timestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public DeviceLastTelemetryItemModel withName(String name) {
		setName(name);

		return this;
	}

	public DeviceLastTelemetryItemModel withValue(String value) {
		setValue(value);

		return this;
	}

	public DeviceLastTelemetryItemModel withTimestamp(long timestamp) {
		setTimestamp(timestamp);

		return this;
	}
}
