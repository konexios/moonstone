package com.arrow.rhea.client.search;

import moonstone.acs.client.search.SearchCriteria;

public class DeviceCategorySearchCriteria extends SearchCriteria {

	private static final String ENABLED = "enabled";

	public DeviceCategorySearchCriteria withEnabled(Boolean enabled) {
		if (enabled != null) {
			simpleCriteria.put(ENABLED, enabled.toString());
		}
		return this;
	}
}
