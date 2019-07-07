package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class WidgetSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = 2761958607824496310L;

	private Set<String> parentIds;
	private Set<String> widgetTypeIds;

	public Set<String> getParentIds() {
		return super.getValues(parentIds);
	}

	public WidgetSearchParams addParentIds(String... parentIds) {
		this.parentIds = super.addValues(this.parentIds, parentIds);

		return this;
	}

	public Set<String> getWidgetTypeIds() {
		return super.getValues(widgetTypeIds);
	}

	public WidgetSearchParams addWidgetTypeIds(String... widgetTypeIds) {
		this.widgetTypeIds = super.addValues(this.widgetTypeIds, widgetTypeIds);

		return this;
	}
}