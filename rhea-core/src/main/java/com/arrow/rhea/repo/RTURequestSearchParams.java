package com.arrow.rhea.repo;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.acn.client.model.RightToUseStatus;

public class RTURequestSearchParams extends RheaSearchParamsAbstract {
	private static final long serialVersionUID = 3759875221507701923L;

	private Set<String> companyIds;
	private Set<String> softwareReleaseIds;
	private EnumSet<RightToUseStatus> statuses;
	
	public RTURequestSearchParams addCompanyIds(String... companyIds) {
		this.companyIds = super.addValues(this.companyIds, companyIds);
		return this;
	}
	
	public RTURequestSearchParams addSoftwareReleaseIds(String... softwareReleaseIds) {
		this.softwareReleaseIds = super.addValues(this.softwareReleaseIds, softwareReleaseIds);
		return this;
	}

	public EnumSet<RightToUseStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<RightToUseStatus> statuses) {
		this.statuses = statuses;
	}
	
	public RTURequestSearchParams withStatuses(EnumSet<RightToUseStatus> statuses) {
		setStatuses(statuses);
		return this;
	}

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(Set<String> companyIds) {
		this.companyIds = companyIds;
	}

	public Set<String> getSoftwareReleaseIds() {
		return softwareReleaseIds;
	}

	public void setSoftwareReleaseIds(Set<String> softwareReleaseIds) {
		this.softwareReleaseIds = softwareReleaseIds;
	}
}