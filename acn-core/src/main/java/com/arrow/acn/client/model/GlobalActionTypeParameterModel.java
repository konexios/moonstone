/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GlobalActionTypeParameterModel implements Serializable {
	private static final long serialVersionUID = -5367487228725610879L;
	
	private String name;
	private String description;
	private Set<ParameterValidationModel> validationTypes = new HashSet<>();
	private int order;
	private boolean required = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ParameterValidationModel> getValidationTypes() {
		return validationTypes;
	}

	public void setValidationTypes(Set<ParameterValidationModel> validationTypes) {
		this.validationTypes = validationTypes;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
