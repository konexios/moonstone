package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class PropertyValueSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = -66616115682829529L;

	private Set<String> pagePropertyIds;

	public Set<String> getPagePropertyIds() {
		return super.getValues(pagePropertyIds);
	}

	public PropertyValueSearchParams addPagePropertyIds(String... pagePropertyIds) {
		this.pagePropertyIds = super.addValues(this.pagePropertyIds, pagePropertyIds);

		return this;
	}
}