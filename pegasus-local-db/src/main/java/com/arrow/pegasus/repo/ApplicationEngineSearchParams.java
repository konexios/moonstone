package com.arrow.pegasus.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DefinitionDocumentParamsAbstract;

public class ApplicationEngineSearchParams extends DefinitionDocumentParamsAbstract {
	private static final long serialVersionUID = 8321922028572506086L;

	private Set<String> productIds;
	private Set<String> zoneIds;

	public Set<String> getProductIds() {
		return productIds;
	}

	public void addProductIds(String... productIds) {
		this.productIds = super.addValues(this.productIds, productIds);
	}

	public Set<String> getZoneIds() {
		return zoneIds;
	}

	public void addZoneIds(String... zoneIds) {
		this.zoneIds = super.addValues(this.zoneIds, zoneIds);
	}
}
