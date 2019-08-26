package com.arrow.rhea.client.search;

import moonstone.acs.client.search.SearchCriteria;

public class DeviceProductSearchCriteria extends SearchCriteria {

	private static final String ENABLED = "enabled";
	private static final String COMPANY_ID = "companyId";
	private static final String DEVICE_MANUFACTURER_ID = "deviceManufacturerId";
	private static final String DEVICE_CATEGORY = "deviceCategory";

	public DeviceProductSearchCriteria withEnabled(Boolean enabled) {
		if (enabled != null) {
			simpleCriteria.put(ENABLED, enabled.toString());
		}
		return this;
	}

	public DeviceProductSearchCriteria withCompanyId(String companyId) {
		if (companyId != null) {
			simpleCriteria.put(COMPANY_ID, companyId);
		}
		return this;
	}

	public DeviceProductSearchCriteria withDeviceManufacturerIds(String... deviceManufacturerIds) {
		if (deviceManufacturerIds != null) {
			arrayCriteria.put(DEVICE_MANUFACTURER_ID, deviceManufacturerIds);
		}
		return this;
	}

	public DeviceProductSearchCriteria withDeviceCategories(String... deviceCategories) {
		if (deviceCategories != null) {
			arrayCriteria.put(DEVICE_CATEGORY, deviceCategories);
		}

		return this;
	}
}
