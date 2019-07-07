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
import java.util.HashMap;
import java.util.Map;

public class DeviceTypeTelemetryModel implements Serializable {

	private static final long serialVersionUID = 1351634662217682079L;

	private String description;
	private String name;
	private String type;
	private Map<String, String> variables = new HashMap<>();
	private String telemetryUnitHid;

	public DeviceTypeTelemetryModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public DeviceTypeTelemetryModel withName(String name) {
		setName(name);
		return this;
	}

	public DeviceTypeTelemetryModel withType(String type) {
		setType(type);
		return this;
	}

	public DeviceTypeTelemetryModel withVariables(Map<String, String> variables) {
		setVariables(variables);
		return this;
	}

	public DeviceTypeTelemetryModel withTelemetryUnitHid(String telemetryUnitHid) {
		setTelemetryUnitHid(telemetryUnitHid);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

	public String getTelemetryUnitHid() {
		return telemetryUnitHid;
	}

	public void setTelemetryUnitHid(String telemetryUnitHid) {
		this.telemetryUnitHid = telemetryUnitHid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = description != null ? description.hashCode() : 0;
		result = prime * result + (name != null ? name.hashCode() : 0);
		result = prime * result + (type != null ? type.hashCode() : 0);
		result = prime * result + (variables != null ? variables.hashCode() : 0);
		result = prime * result + (telemetryUnitHid != null ? telemetryUnitHid.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DeviceTypeTelemetryModel that = (DeviceTypeTelemetryModel) o;

		if (description != null ? !description.equals(that.description) : that.description != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		if (type != null ? !type.equals(that.type) : that.type != null) {
			return false;
		}
		if (telemetryUnitHid != null ? !telemetryUnitHid.equals(that.telemetryUnitHid)
				: that.telemetryUnitHid != null) {
			return false;
		}
		return variables != null ? variables.equals(that.variables) : that.variables == null;
	}

}
