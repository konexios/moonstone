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

import com.arrow.acs.AcsUtils;

public class DeviceActionModel implements Serializable {

	private static final long serialVersionUID = 1837224176636107340L;

	private String systemName;
	private String description;
	private String criteria;
	// if false - expect criteria is not empty, if true - special case for No
	// Telemetry Enhancement
	private boolean noTelemetry;
	private long noTelemetryTime;
	private long expiration;
	private boolean enabled = true;
	private Map<String, String> parameters = new HashMap<>();
	private int index;

	public void trim() {
		systemName = AcsUtils.trimToNull(systemName);
		description = AcsUtils.trimToNull(description);
		criteria = AcsUtils.trimToNull(criteria);
	}

	public DeviceActionModel withSystemName(String systemName) {
		setSystemName(systemName);
		return this;
	}

	public DeviceActionModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public DeviceActionModel withCriteria(String criteria) {
		setCriteria(criteria);
		return this;
	}

	public DeviceActionModel withNoTelemetry(boolean noTelemetry) {
		setNoTelemetry(noTelemetry);
		return this;
	}

	public DeviceActionModel withNoTelemetryTime(long noTelemetryTime) {
		setNoTelemetryTime(noTelemetryTime);
		return this;
	}

	public DeviceActionModel withExpiration(long expiration) {
		setExpiration(expiration);
		return this;
	}

	public DeviceActionModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public DeviceActionModel withParameters(Map<String, String> parameters) {
		setParameters(parameters);
		return this;
	}

	public DeviceActionModel withIndex(int index) {
		setIndex(index);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public boolean isNoTelemetry() {
		return noTelemetry;
	}

	public long getNoTelemetryTime() {
		return noTelemetryTime;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setNoTelemetry(boolean noTelemetry) {
		this.noTelemetry = noTelemetry;
	}

	public void setNoTelemetryTime(long noTelemetryTime) {
		this.noTelemetryTime = noTelemetryTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criteria == null) ? 0 : criteria.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + (int) (expiration ^ (expiration >>> 32));
		result = prime * result + index;
		result = prime * result + (noTelemetry ? 1231 : 1237);
		result = prime * result + (int) (noTelemetryTime ^ (noTelemetryTime >>> 32));
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((systemName == null) ? 0 : systemName.hashCode());
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
		DeviceActionModel other = (DeviceActionModel) obj;
		if (criteria == null) {
			if (other.criteria != null)
				return false;
		} else if (!criteria.equals(other.criteria))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled != other.enabled)
			return false;
		if (expiration != other.expiration)
			return false;
		if (index != other.index)
			return false;
		if (noTelemetry != other.noTelemetry)
			return false;
		if (noTelemetryTime != other.noTelemetryTime)
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		return true;
	}
}