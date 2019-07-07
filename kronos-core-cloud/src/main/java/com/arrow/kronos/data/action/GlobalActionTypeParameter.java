package com.arrow.kronos.data.action;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class GlobalActionTypeParameter implements Serializable {

	private static final long serialVersionUID = 4548071430595925208L;

	private final static boolean DEFAULT_REQUIRED = false;

	@NotBlank
	private String name;
	private String description;
	private Set<ParameterValidation> validationTypes = new HashSet<>();
	private int order;
	private boolean required = DEFAULT_REQUIRED;

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

	public Set<ParameterValidation> getValidationTypes() {
		return validationTypes;
	}

	public void setValidationTypes(Set<ParameterValidation> validationTypes) {
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
