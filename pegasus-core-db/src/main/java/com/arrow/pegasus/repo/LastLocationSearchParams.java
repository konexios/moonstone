package com.arrow.pegasus.repo;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.repo.params.SearchParamsAbstract;

public class LastLocationSearchParams extends SearchParamsAbstract {
	private static final long serialVersionUID = -7215769286007249547L;

	private EnumSet<LocationObjectType> objectTypes;
	private Set<String> objectIds;

	public EnumSet<LocationObjectType> getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(EnumSet<LocationObjectType> objectTypes) {
		this.objectTypes = objectTypes;
	}

	public Set<String> getObjectIds() {
		return super.getValues(objectIds);
	}

	public LastLocationSearchParams addObjectIds(String... objectIds) {
		this.objectIds = super.addValues(this.objectIds, objectIds);

		return this;
	}
}
