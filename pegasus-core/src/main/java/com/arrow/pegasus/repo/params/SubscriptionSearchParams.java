package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class SubscriptionSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 6118349343749801748L;

	private String name;
	private Set<String> companyIds;
	private Boolean enabled;
	private Instant createdBefore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubscriptionSearchParams withName(String name) {
		setName(name);

		return this;
	}

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(Set<String> companyIds) {
		this.companyIds = companyIds;
	}

	public SubscriptionSearchParams addCompanyIds(String... companyIds) {
		this.companyIds = super.addValues(this.companyIds, companyIds);

		return this;
	}

	public SubscriptionSearchParams withCompanyIds(String... companyIds) {
		this.companyIds = null;

		return addCompanyIds(companyIds);
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public SubscriptionSearchParams withEnabled(Boolean enabled) {
		setEnabled(enabled);

		return this;
	}

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public SubscriptionSearchParams createdBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}
}
