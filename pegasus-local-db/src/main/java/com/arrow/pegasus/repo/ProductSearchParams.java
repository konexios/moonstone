package com.arrow.pegasus.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DefinitionDocumentParamsAbstract;

public class ProductSearchParams extends DefinitionDocumentParamsAbstract {
	private static final long serialVersionUID = 8321922028572506086L;

	private String systemName;
	private Boolean apiSigningRequired;
	private Set<String> parentProductIds;
	private Boolean hidden;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Boolean getApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(Boolean apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public Set<String> getParentProductIds() {
		return parentProductIds;
	}

	public void addParentProductIds(String... parentProductIds) {
		this.parentProductIds = super.addValues(this.parentProductIds, parentProductIds);
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
}
