package com.arrow.kronos.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.SearchParamsAbstract;

public class EsTelemetryItemSearchParams extends SearchParamsAbstract {

	private static final long serialVersionUID = -6353836504070448555L;

	private String applicationId;
	private String deviceId;
	private Set<String> telemetryNames;
	private Long fromTimestamp;
	private Long toTimestamp;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public EsTelemetryItemSearchParams addApplicationId(String applicationId) {
		setApplicationId(applicationId);
		return this;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public EsTelemetryItemSearchParams addDeviceId(String deviceId) {
		setDeviceId(deviceId);
		return this;
	}

	public Set<String> getTelemetryNames() {
		return telemetryNames;
	}

	public EsTelemetryItemSearchParams addTelemetryNames(String... telemetryNames) {
		this.telemetryNames = super.addValues(this.telemetryNames, telemetryNames);
		return this;
	}

	public Long getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Long fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public EsTelemetryItemSearchParams addFromTimestamp(Long fromTimestamp) {
		setFromTimestamp(fromTimestamp);
		return this;
	}

	public Long getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	public EsTelemetryItemSearchParams addToTimestamp(Long toTimestamp) {
		setToTimestamp(toTimestamp);
		return this;
	}
}
