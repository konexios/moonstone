package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class ConfigurationPageSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = 7680476275718608653L;

	private Set<String> widgetConfigurationIds;

	public Set<String> getWidgetConfigurationIds() {
		return super.getValues(widgetConfigurationIds);
	}

	public ConfigurationPageSearchParams addWidgetConfigurationIds(String... widgetConfigurationIds) {
		this.widgetConfigurationIds = super.addValues(this.widgetConfigurationIds, widgetConfigurationIds);

		return this;
	}
}