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
import java.util.List;

import com.arrow.acn.client.model.GatewayModel.GatewayType;
import com.arrow.acs.client.model.ConfigurationPropertyModel;

public class GatewayConfigBackupModel extends BaseDeviceConfigBackupModel {

	private static final long serialVersionUID = -208697247504709L;

	private GatewayType type;
	private String osName;
	private String sdkVersion;
	private List<ConfigurationPropertyModel> configurations = new ArrayList<>();

	public GatewayConfigBackupModel withType(GatewayType type) {
		setType(type);
		return this;
	}

	public GatewayConfigBackupModel withOsName(String osName) {
		setOsName(osName);
		return this;
	}

	public GatewayConfigBackupModel withSdkVersion(String sdkVersion) {
		setSdkVersion(sdkVersion);
		return this;
	}

	public GatewayConfigBackupModel withConfigurations(List<ConfigurationPropertyModel> configurations) {
		setConfigurations(configurations);
		return this;
	}

	public GatewayType getType() {
		return type;
	}

	public void setType(GatewayType type) {
		this.type = type;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public List<ConfigurationPropertyModel> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationPropertyModel> configurations) {
		this.configurations = configurations;
	}
}
