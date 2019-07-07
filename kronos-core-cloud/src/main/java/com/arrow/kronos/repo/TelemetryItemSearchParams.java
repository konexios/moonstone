package com.arrow.kronos.repo;

import java.io.Serializable;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class TelemetryItemSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -8375677214733344088L;

	private Set<String> telemetryIds;
	private Set<String> names;
	private Set<String> deviceIds;
	private Long fromTimestamp;
	private Long toTimestamp;

	public Set<String> getTelemetryIds() {
		return telemetryIds;
	}

	public TelemetryItemSearchParams setTelemetryIds(String... telemetryIds) {
		this.telemetryIds = super.addValues(this.telemetryIds, telemetryIds);

		return this;
	}

	public Set<String> getNames() {
		return names;
	}

	public TelemetryItemSearchParams setNames(String... names) {
		this.names = super.addValues(this.names, names);

		return this;
	}

	public Set<String> getDeviceIds() {
		return super.getValues(this.deviceIds);
	}

	public TelemetryItemSearchParams setDeviceIds(String... deviceIds) {
		this.deviceIds = super.addValues(this.deviceIds, deviceIds);

		return this;
	}

	public Long getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Long fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public Long getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}
}
