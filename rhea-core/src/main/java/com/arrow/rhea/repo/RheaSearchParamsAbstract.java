package com.arrow.rhea.repo;

import java.io.Serializable;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public abstract class RheaSearchParamsAbstract extends AuditableDocumentSearchParams implements Serializable {

	private static final long serialVersionUID = 3569796315562341025L;

	private Set<String> companyIds;
	private Boolean enabled;

	public Set<String> getCompanyIds() {
		return super.getValues(companyIds);
	}

	public RheaSearchParamsAbstract addCompanyIds(String... companyIds) {
		this.companyIds = super.addValues(this.companyIds, companyIds);

		return this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public RheaSearchParamsAbstract setEnabled(Boolean enabled) {
		this.enabled = enabled;

		return this;
	}
}