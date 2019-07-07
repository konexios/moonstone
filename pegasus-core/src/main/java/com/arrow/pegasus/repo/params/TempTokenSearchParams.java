package com.arrow.pegasus.repo.params;

import java.util.Set;

public class TempTokenSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = -2613903551244040222L;

	private Set<String> companyIds;
	private Set<String> applicationIds;
	private Set<String> tokens;
	private Set<String> softwareReleaseTransIds;
	private Boolean expired;

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(Set<String> companyIds) {
		this.companyIds = companyIds;
	}

	public TempTokenSearchParams addCompanyIds(String... companyIds) {
		this.companyIds = addValues(this.companyIds, companyIds);
		return this;
	}

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(Set<String> applicationIds) {
		this.applicationIds = applicationIds;
	}

	public TempTokenSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = addValues(this.applicationIds, applicationIds);
		return this;
	}

	public Set<String> getTokens() {
		return tokens;
	}

	public void setTokens(Set<String> tokens) {
		this.tokens = tokens;
	}

	public TempTokenSearchParams addTokens(String... tokens) {
		this.tokens = addValues(this.tokens, tokens);
		return this;
	}

	public Set<String> getSoftwareReleaseTransIds() {
		return softwareReleaseTransIds;
	}

	public void setSoftwareReleaseTransIds(Set<String> softwareReleaseTransIds) {
		this.softwareReleaseTransIds = softwareReleaseTransIds;
	}

	public TempTokenSearchParams addSoftwareReleaseTransIds(String... softwareReleaseTransIds) {
		this.softwareReleaseTransIds = addValues(this.softwareReleaseTransIds, softwareReleaseTransIds);
		return this;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
}