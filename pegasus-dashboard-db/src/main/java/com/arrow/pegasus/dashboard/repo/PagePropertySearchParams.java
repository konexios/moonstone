package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class PagePropertySearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = -66616115682829529L;

	private Set<String> configurationPageIds;

	public Set<String> getConfigurationPageIds() {
		return super.getValues(configurationPageIds);
	}

	public PagePropertySearchParams addConfigurationPageIds(String... configurationPageIds) {
		this.configurationPageIds = super.addValues(this.configurationPageIds, configurationPageIds);

		return this;
	}
}