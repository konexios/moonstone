package com.arrow.kronos.repo;

import java.io.Serializable;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class TelemetrySearchParams extends AuditableDocumentSearchParams implements Serializable {

	private static final long serialVersionUID = 2998913186307791840L;

	private Set<String> deviceIds;
	private Long fromTimestamp;
	private Long toTimestamp;

	public Set<String> getDeviceIds() {
		return super.getValues(this.deviceIds);
	}

	public TelemetrySearchParams setDeviceIds(String... deviceIds) {
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
