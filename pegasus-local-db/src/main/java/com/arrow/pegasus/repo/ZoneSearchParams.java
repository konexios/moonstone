package com.arrow.pegasus.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DefinitionDocumentParamsAbstract;

public class ZoneSearchParams extends DefinitionDocumentParamsAbstract {
	private static final long serialVersionUID = -1003427451181949712L;

	private String systemName;
	private Set<String> regionIds;
	private Boolean hidden;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Set<String> getRegionIds() {
		return regionIds;
	}

	public void addRegionIds(String... regionIds) {
		this.regionIds = super.addValues(this.regionIds, regionIds);
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
}
