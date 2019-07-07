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
import java.util.List;
import java.util.Map;

import com.arrow.acs.AcsUtils;

public class DeviceTypeRegistrationModel implements Serializable {
	private static final long serialVersionUID = 3174634854949378263L;

	private String name;
	private String description;
	private List<DeviceTypeTelemetryModel> telemetries;
	private Map<String, DeviceStateValueMetadataModel> stateMetadata = new HashMap<>();
	private boolean enabled = true;
	private AcnDeviceCategory deviceCategory;

	public void trim() {
		name = AcsUtils.trimToNull(name);
		description = AcsUtils.trimToNull(description);
	}

	public DeviceTypeRegistrationModel withName(String name) {
		setName(name);
		return this;
	}

	public DeviceTypeRegistrationModel withDescription(String name) {
		setDescription(name);
		return this;
	}

	public DeviceTypeRegistrationModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public DeviceTypeRegistrationModel withTelemetries(List<DeviceTypeTelemetryModel> telemetries) {
		setTelemetries(telemetries);
		return this;
	}

	public DeviceTypeRegistrationModel withStateMetadata(Map<String, DeviceStateValueMetadataModel> stateMetadata) {
		setStateMetadata(stateMetadata);
		return this;
	}

	public DeviceTypeRegistrationModel withDeviceCategory(AcnDeviceCategory deviceCategory) {
		setDeviceCategory(deviceCategory);
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

	public List<DeviceTypeTelemetryModel> getTelemetries() {
		return telemetries;
	}

	public void setTelemetries(List<DeviceTypeTelemetryModel> telemetries) {
		this.telemetries = telemetries;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, DeviceStateValueMetadataModel> getStateMetadata() {
		return stateMetadata;
	}

	public void setStateMetadata(Map<String, DeviceStateValueMetadataModel> stateMetadata) {
		this.stateMetadata = stateMetadata;
	}

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((deviceCategory == null) ? 0 : deviceCategory.name().hashCode());
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
		DeviceTypeRegistrationModel other = (DeviceTypeRegistrationModel) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled != other.enabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (deviceCategory == null) {
			if (other.deviceCategory != null)
				return false;
		} else if (!deviceCategory.equals(other.deviceCategory))
			return false;
		return true;
	}
}
