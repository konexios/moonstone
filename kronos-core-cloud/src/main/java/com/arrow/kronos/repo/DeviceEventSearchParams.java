package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class DeviceEventSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -7445722093328061132L;

	private Set<String> applicationIds;
	private Set<String> deviceIds;
	private Set<String> telemetryIds;
    private Set<String> deviceActionTypeIds;
	private Set<String> statuses;
	private Instant createdDateFrom;
	private Instant createdDateTo;

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public DeviceEventSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = super.addValues(this.applicationIds, applicationIds);

		return this;
	}

	public Set<String> getDeviceIds() {
		return super.getValues(deviceIds);
	}

	public DeviceEventSearchParams addDeviceIds(String... deviceIds) {
		this.deviceIds = super.addValues(this.deviceIds, deviceIds);
        return this;
    }

    public DeviceEventSearchParams addTelemetryIds(String... telemetryIds) {
        this.telemetryIds = super.addValues(this.telemetryIds, telemetryIds);return this;
    }

	public Set<String> getDeviceActionTypeIds() {
		return super.getValues(deviceActionTypeIds);
	}

	public DeviceEventSearchParams addDeviceActionTypeIds(String... deviceActionTypeIds) {
		this.deviceActionTypeIds = super.addValues(this.deviceActionTypeIds, deviceActionTypeIds);

		return this;
	}

	public Set<String> getStatuses() {
		return super.getValues(statuses);
	}

	public DeviceEventSearchParams addStatuses(String... statuses) {
		this.statuses = super.addValues(this.statuses, statuses);

		return this;
	}

	public Instant getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Instant createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public Instant getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(Instant createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

}
