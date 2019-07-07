package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class WidgetConfigurationSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = 7680476275718608653L;

	private Set<String> widgetIds;

	public Set<String> getWidgetIds() {
		return super.getValues(widgetIds);
	}

	public WidgetConfigurationSearchParams addWidgetIds(String... widgetIds) {
		this.widgetIds = super.addValues(this.widgetIds, widgetIds);

		return this;
	}
}