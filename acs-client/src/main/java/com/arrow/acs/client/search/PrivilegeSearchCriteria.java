package com.arrow.acs.client.search;

public class PrivilegeSearchCriteria extends SearchCriteria {
	private static final String NAME = "name";
	private static final String SYSTEM_NAME = "systemName";
	private static final String PRODUCT_HID = "productHid";
	private static final String ENABLED = "enabled";

	public PrivilegeSearchCriteria withName(String name) {
		simpleCriteria.put(NAME, name);
		return this;
	}

	public PrivilegeSearchCriteria withSystemName(String systemName) {
		simpleCriteria.put(SYSTEM_NAME, systemName);
		return this;
	}

	public PrivilegeSearchCriteria withProductHid(String productHid) {
		simpleCriteria.put(PRODUCT_HID, productHid);
		return this;
	}

	public PrivilegeSearchCriteria withEnabled(boolean enabled) {
		simpleCriteria.put(ENABLED, Boolean.toString(enabled));
		return this;
	}
}
