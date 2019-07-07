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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.acs.client.model.DefinitionModelAbstract;

public class DeviceTypeModel extends DefinitionModelAbstract<DeviceTypeModel> {
	private static final long serialVersionUID = -658863900901932192L;

	private List<DeviceTypeTelemetryModel> telemetries = new ArrayList<>();
	private Map<String, DeviceStateValueMetadataModel> stateMetadata = new HashMap<>();
	private AcnDeviceCategory deviceCategory;

	@Override
	protected DeviceTypeModel self() {
		return this;
	}

	public DeviceTypeModel withTelemetries(List<DeviceTypeTelemetryModel> telemetries) {
		setTelemetries(telemetries);
		return this;
	}

	public DeviceTypeModel withStateMetadata(Map<String, DeviceStateValueMetadataModel> stateMetadata) {
		setStateMetadata(stateMetadata);
		return this;
	}

	public DeviceTypeModel withDeviceCategory(AcnDeviceCategory deviceCategory) {
		setDeviceCategory(deviceCategory);
		return this;
	}

	public void setTelemetries(List<DeviceTypeTelemetryModel> telemetries) {
		if (telemetries != null) {
			this.telemetries.addAll(telemetries);
		}
	}

	public List<DeviceTypeTelemetryModel> getTelemetries() {
		return telemetries;
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
}
