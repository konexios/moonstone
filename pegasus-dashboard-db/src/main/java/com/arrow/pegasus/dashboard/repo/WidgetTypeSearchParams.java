package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class WidgetTypeSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = 639073721512498771L;

	private Set<String> productIds;
	private Set<String> categories;
	private Boolean enabled;

	public Set<String> getProductIds() {
		return super.getValues(productIds);
	}

	public WidgetTypeSearchParams addProductIds(String... productIds) {
		this.productIds = super.addValues(this.productIds, productIds);

		return this;
	}

	public Set<String> getCategories() {
		return super.getValues(categories);
	}

	public WidgetTypeSearchParams addCategories(String... categories) {
		this.categories = super.addValues(this.categories, categories);

		return this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public WidgetTypeSearchParams setEnabled(Boolean enabled) {
		this.enabled = enabled;

		return this;
	}
}