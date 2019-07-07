package com.arrow.rhea.client.search;

import com.arrow.acs.client.search.SearchCriteria;

public class SoftwareProductSearchCriteria extends SearchCriteria {

	private static final String ENABLED = "enabled";
	private static final String COMPANY_ID = "companyId";
	private static final String SOFTWARE_VENDOR_ID = "softwareVendorId";
	private static final String NAME = "name";

	public SoftwareProductSearchCriteria withEnabled(Boolean enabled) {
		if (enabled != null) {
			simpleCriteria.put(ENABLED, enabled.toString());
		}
		return this;
	}

	public SoftwareProductSearchCriteria withCompanyId(String companyId) {
		if (companyId != null) {
			simpleCriteria.put(COMPANY_ID, companyId);
		}
		return this;
	}

	public SoftwareProductSearchCriteria withSoftwareVendorIds(String... softwareVendorIds) {
		if (softwareVendorIds != null) {
			arrayCriteria.put(SOFTWARE_VENDOR_ID, softwareVendorIds);
		}
		return this;
	}

	public SoftwareProductSearchCriteria withName(String name) {
		if (name != null) {
			simpleCriteria.put(NAME, name);
		}
		return this;
	}
}
