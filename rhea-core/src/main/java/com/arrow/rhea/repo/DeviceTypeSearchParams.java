package com.arrow.rhea.repo;

import java.util.Set;

public class DeviceTypeSearchParams extends RheaSearchParamsAbstract {
	private static final long serialVersionUID = -5102522586393371201L;
	
	private Set<String> deviceProductIds;
	private Set<String> names;

	public Set<String> getDeviceProductIds() {
		return deviceProductIds;
	}

	public DeviceTypeSearchParams addDeviceProductIds(String... deviceProductIds) {
		this.deviceProductIds = addValues(this.deviceProductIds, deviceProductIds);
		return this;
	}

	public Set<String> getNames() {
		return getValues(names);
	}

	public DeviceTypeSearchParams addNames(String... names) {
		this.names = addValues(this.names, names);
		return this;
	}
}