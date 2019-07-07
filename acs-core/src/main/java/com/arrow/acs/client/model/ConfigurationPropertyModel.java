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
package com.arrow.acs.client.model;

import java.io.Serializable;

public class ConfigurationPropertyModel implements Serializable {

	public enum ConfigurationPropertyCategory {
		Api, Branding, Email, Gateway, MetaData, Settings
	}

	public enum ConfigurationPropertyDataType {
		String, Integer, Long, Boolean, Json
	}

	private static final long serialVersionUID = 686843365799143882L;

	private ConfigurationPropertyDataType dataType;
	private ConfigurationPropertyCategory category;
	private String name;
	private String value;
	private String jsonClass;

	public ConfigurationPropertyModel withDataType(ConfigurationPropertyDataType dataType) {
		setDataType(dataType);
		return this;
	}

	public ConfigurationPropertyModel withCategory(ConfigurationPropertyCategory category) {
		setCategory(category);
		return this;
	}

	public ConfigurationPropertyModel withName(String name) {
		setName(name);
		return this;
	}

	public ConfigurationPropertyModel withValue(String value) {
		setValue(value);
		return this;
	}

	public ConfigurationPropertyModel withJsonClass(String jsonClass) {
		setJsonClass(jsonClass);
		return this;
	}

	public ConfigurationPropertyDataType getDataType() {
		return dataType;
	}

	public void setDataType(ConfigurationPropertyDataType dataType) {
		this.dataType = dataType;
	}

	public ConfigurationPropertyCategory getCategory() {
		return category;
	}

	public void setCategory(ConfigurationPropertyCategory category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getJsonClass() {
		return jsonClass;
	}

	public void setJsonClass(String jsonClass) {
		this.jsonClass = jsonClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((jsonClass == null) ? 0 : jsonClass.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		ConfigurationPropertyModel other = (ConfigurationPropertyModel) obj;
		if (category != other.category)
			return false;
		if (dataType != other.dataType)
			return false;
		if (jsonClass == null) {
			if (other.jsonClass != null)
				return false;
		} else if (!jsonClass.equals(other.jsonClass))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
