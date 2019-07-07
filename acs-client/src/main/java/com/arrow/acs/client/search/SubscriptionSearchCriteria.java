package com.arrow.acs.client.search;

public class SubscriptionSearchCriteria extends SearchCriteria {
	private static final String COMPANY_HID = "companyHid";
	private static final String INCLUDE_DISABLED = "includeDisabled";

	public SubscriptionSearchCriteria withCompanyHid(String companyHid) {
		simpleCriteria.put(COMPANY_HID, companyHid);
		return this;
	}

	public SubscriptionSearchCriteria withIncludeDisabled(boolean includeDisabled) {
		simpleCriteria.put(INCLUDE_DISABLED, Boolean.toString(includeDisabled));
		return this;
	}
}
