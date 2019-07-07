package com.arrow.rhea.repo;

import java.util.Set;

public class SoftwareProductSearchParams extends RheaSearchParamsAbstract {
	private static final long serialVersionUID = -6055651472101274113L;

	private Boolean editable;
	private Set<String> softwareVendorIds;
	private String name;

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Set<String> getSoftwareVendorIds() {
		return softwareVendorIds;
	}

	public SoftwareProductSearchParams addSoftwareVendorIds(String... softwareVendorIds) {
		this.softwareVendorIds = addValues(this.softwareVendorIds, softwareVendorIds);
		return this;
	}

	public String getName() {
		return name;
	}

	public SoftwareProductSearchParams setName(String name) {
		this.name = name;
		return this;
	}
}
