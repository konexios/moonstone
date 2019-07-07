package com.arrow.kronos.repo;

import java.io.Serializable;
import java.util.Set;

public class LastTelemetryItemSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 6621786592666223502L;

	private Set<String> deviceIds;
	private Set<String> telemetryIds;
	private Set<String> names;
	private Set<String> types;

	public Set<String> getDeviceIds() {
		return super.getValues(deviceIds);
	}

	public LastTelemetryItemSearchParams addDeviceIds(String... deviceIds) {
		this.deviceIds = super.addValues(this.deviceIds, deviceIds);

		return this;
	}

	public Set<String> getTelemetryIds() {
		return super.getValues(telemetryIds);
	}

	public LastTelemetryItemSearchParams addTelemetryIds(String... telemetryIds) {
		this.telemetryIds = super.addValues(this.telemetryIds, telemetryIds);

		return this;
	}

	public Set<String> getNames() {
		return super.getValues(names);
	}

	public LastTelemetryItemSearchParams addNames(String... names) {
		this.names = super.addValues(this.names, names);

		return this;
	}

	public Set<String> getTypes() {
		return super.getValues(types);
	}

	public LastTelemetryItemSearchParams addTypes(String... types) {
		this.types = super.addValues(this.types, types);

		return this;
	}
}
