package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class BoardSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = -3703879574558166129L;

	private Set<String> productIds;
	private Set<String> applicationIds;
	private Set<String> userIds;
	private Set<String> categories;

	public Set<String> getProductIds() {
		return super.getValues(productIds);
	}

	public BoardSearchParams addProductIds(String... productIds) {
		this.productIds = super.addValues(this.productIds, productIds);

		return this;
	}

	public Set<String> getApplicationIds() {
		return super.getValues(applicationIds);
	}

	public BoardSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = super.addValues(this.applicationIds, applicationIds);

		return this;
	}

	public Set<String> getUserIds() {
		return super.getValues(userIds);
	}

	public BoardSearchParams addUserIds(String... userIds) {
		this.userIds = super.addValues(this.userIds, userIds);

		return this;
	}

	public Set<String> getCategories() {
		return super.getValues(categories);
	}

	public BoardSearchParams addCategories(String... categories) {
		this.categories = super.addValues(this.categories, categories);

		return this;
	}
}
