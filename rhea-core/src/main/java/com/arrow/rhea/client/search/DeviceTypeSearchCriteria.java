package com.arrow.rhea.client.search;

import com.arrow.acs.client.search.SearchCriteria;

public class DeviceTypeSearchCriteria extends SearchCriteria {

	private static final String ENABLED = "enabled";
	private static final String DEVICE_TYPE_ID = "deviceProductId";
	private static final String NAME = "name";
	private static final String COMPANY_ID = "companyId";

	public DeviceTypeSearchCriteria withEnabled(Boolean enabled) {
		if (enabled != null) {
			simpleCriteria.put(ENABLED, enabled.toString());
		}
		return this;
	}

	public DeviceTypeSearchCriteria withDeviceProductIds(String... deviceProductIds) {
		if (deviceProductIds != null) {
			arrayCriteria.put(DEVICE_TYPE_ID, deviceProductIds);
		}
		return this;
	}

	public DeviceTypeSearchCriteria withNames(String... names) {
		if (names != null) {
			arrayCriteria.put(NAME, names);
		}
		return this;
	}

	public DeviceTypeSearchCriteria withCompanyId(String companyId) {
		if (companyId != null) {
			simpleCriteria.put(COMPANY_ID, companyId);
		}
		return this;
	}
}
