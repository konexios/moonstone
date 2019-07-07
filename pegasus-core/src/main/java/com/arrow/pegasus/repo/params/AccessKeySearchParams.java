package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class AccessKeySearchParams extends DocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -2737830139279497564L;

	private Set<String> companyIds;
	private Set<String> subscriptionIds;
	private String name;
	private Set<String> accessLevels;
	private Set<String> pri;
	private Instant expirationDateFrom;
	private Instant expirationDateTo;
	private Set<String> applicationIds;
	
	public Set<String> getApplicationIds() {
		return super.getValues(applicationIds);
	}

	public AccessKeySearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = super.addValues(this.applicationIds, applicationIds);

		return this;
	}

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public AccessKeySearchParams addCompanyIds(String... companyIds) {
		this.companyIds = super.addValues(this.companyIds, companyIds);

		return this;
	}

	public Set<String> getSubscriptionIds() {
		return subscriptionIds;
	}

	public AccessKeySearchParams addSubscriptionIds(String... subscriptionIds) {
		this.subscriptionIds = super.addValues(this.subscriptionIds, subscriptionIds);

		return this;
	}

	public String getName() {
		return name;
	}

	public AccessKeySearchParams setName(String name) {
		this.name = name;

		return this;
	}

	public Set<String> getAccessLevels() {
		return accessLevels;
	}

	public AccessKeySearchParams addAccessLevels(String... accessLevels) {
		this.accessLevels = super.addValues(this.accessLevels, accessLevels);

		return this;
	}

	public Set<String> getPri() {
		return pri;
	}

	public AccessKeySearchParams addPri(String... pri) {
		this.pri = super.addValues(this.pri, pri);

		return this;
	}

	public Instant getExpirationDateFrom() {
		return expirationDateFrom;
	}

	public void setExpirationDateFrom(Instant expirationDateFrom) {
		this.expirationDateFrom = expirationDateFrom;
	}

	public Instant getExpirationDateTo() {
		return expirationDateTo;
	}

	public void setExpirationDateTo(Instant expirationDateTo) {
		this.expirationDateTo = expirationDateTo;
	}
}
