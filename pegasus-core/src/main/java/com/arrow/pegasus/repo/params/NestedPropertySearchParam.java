package com.arrow.pegasus.repo.params;

import java.util.Set;

public class NestedPropertySearchParam extends DocumentSearchParams {
	private static final long serialVersionUID = -3823456210622658486L;

	private String propertyName;
	private Set<String> propertyValues;

	public NestedPropertySearchParam(String propertyName, String... propertyValues) {
		this.propertyName = propertyName;
		addPropertyValues(propertyValues);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Set<String> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(Set<String> propertyValues) {
		this.propertyValues = propertyValues;
	}

	public NestedPropertySearchParam addPropertyValues(String... propertyValues) {
		this.propertyValues = addValues(this.propertyValues, propertyValues);
		return this;
	}
}
