package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class DeviceStateTransSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 3405784362510255315L;

	private Set<String> deviceIds;
	private Set<String> types;
	private Set<String> statuses;
	private Instant createdDateFrom;
	private Instant createdDateTo;
	private Instant updatedDateFrom;
	private Instant updatedDateTo;

	public Set<String> getDeviceIds() {
		return deviceIds;
	}

	public DeviceStateTransSearchParams addDeviceIds(String... deviceIds) {
		this.deviceIds = super.addValues(this.deviceIds, deviceIds);
		return this;
	}

	public Set<String> getTypes() {
		return super.getValues(types);
	}

	public DeviceStateTransSearchParams addTypes(String... types) {
		this.types = super.addValues(this.types, types);

		return this;
	}

	public Set<String> getStatuses() {
		return super.getValues(statuses);
	}

	public DeviceStateTransSearchParams addStatuses(String... statuses) {
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

	public Instant getUpdatedDateFrom() {
		return updatedDateFrom;
	}

	public void setUpdatedDateFrom(Instant updatedDateFrom) {
		this.updatedDateFrom = updatedDateFrom;
	}

	public Instant getUpdatedDateTo() {
		return updatedDateTo;
	}

	public void setUpdatedDateTo(Instant updatedDateTo) {
		this.updatedDateTo = updatedDateTo;
	}

}