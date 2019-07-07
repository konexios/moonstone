package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.profile.CompanyStatus;

public class CompanySearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 1285942824929991724L;

	private String name;
	private String abbrName;
	private EnumSet<CompanyStatus> statuses;
	private Set<String> parentCompanyIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	public EnumSet<CompanyStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<CompanyStatus> statuses) {
		this.statuses = statuses;
	}

	public Set<String> getParentCompanyIds() {
		return parentCompanyIds;
	}

	public void addParentCompanyIds(String... parentCompanyIds) {
		this.parentCompanyIds = super.addValues(this.parentCompanyIds, parentCompanyIds);
	}
}
