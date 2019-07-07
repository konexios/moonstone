package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.util.Set;

public class PrivilegeSearchParams extends DefinitionDocumentParamsAbstract implements Serializable {
	private static final long serialVersionUID = 5401739763178596786L;

	private Set<String> productIds;
	private String systemName;
	private Boolean hidden;

	public Set<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<String> productIds) {
		this.productIds = productIds;
	}

	public PrivilegeSearchParams addProductIds(String... productIds) {
		this.productIds = super.addValues(this.productIds, productIds);

		return this;
	}

	public PrivilegeSearchParams withProductIds(String... productIds) {
		this.productIds = null;

		return addProductIds(productIds);
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getHidden() {
		return hidden;
	}
}
