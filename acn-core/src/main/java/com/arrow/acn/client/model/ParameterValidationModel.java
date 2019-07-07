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

public class ParameterValidationModel implements Serializable {
	private static final long serialVersionUID = -5853957566950403385L;
	
	public enum ValidationType {
		STRING("String"), MULTILINE_STRING("Multiline string"), SELECT("Select"), JSON("JSON"), XML("XML"), HTML(
		        "HTML"), KEY_VALUE_PAIRS("Key-value pairs"), EMAIL("E-mail");

		private String value;

		private ValidationType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getName() {
			return name();
		}

		public static ValidationType fromValue(String value) {
			if (value != null) {
				for (ValidationType item : ValidationType.values()) {
					if (item.getValue().equals(value)) {
						return item;
					}
				}
			}
			throw new IllegalArgumentException("Unsupported AttributeType");
		}
	}

	private ValidationType type = ValidationType.STRING;
	private String defaultValue;
	private String data;
	
	public ParameterValidationModel() {}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ValidationType getType() {
		return type;
	}

	public void setType(ValidationType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterValidationModel other = (ParameterValidationModel) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
