package com.arrow.kronos.data;

import java.io.Serializable;
import java.time.Instant;

public class DeviceStateValue implements Serializable {
	private static final long serialVersionUID = -5320873767830234543L;

	private String value;
	private Instant timestamp;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}
