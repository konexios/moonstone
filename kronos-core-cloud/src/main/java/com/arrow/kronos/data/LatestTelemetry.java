package com.arrow.kronos.data;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class LatestTelemetry implements Serializable {
	private static final long serialVersionUID = -4943762903496562706L;

	@Id
	private String deviceId;
	@Field
	private long timestamp;

	public LatestTelemetry withDeviceId(String deviceId) {
		setDeviceId(deviceId);
		return this;
	}

	public LatestTelemetry withTimestamp(long timestamp) {
		setTimestamp(timestamp);
		return this;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
