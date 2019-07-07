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

public class DeviceConfigBackupModel extends BaseDeviceConfigBackupModel {

	private static final long serialVersionUID = 3689405692628372427L;

	private String gatewayHid;
	private String applicationEngineHid;
	private List<DeviceActionModel> actions = new ArrayList<>();

	public DeviceConfigBackupModel withGatewayHid(String gatewayHid) {
		setGatewayHid(gatewayHid);
		return this;
	}

	public DeviceConfigBackupModel withApplicationEngineHid(String applicationEngineHid) {
		setApplicationEngineHid(applicationEngineHid);
		return this;
	}

	public DeviceConfigBackupModel withActions(List<DeviceActionModel> actions) {
		setActions(actions);
		return this;
	}

	public String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}

	public String getApplicationEngineHid() {
		return applicationEngineHid;
	}

	public void setApplicationEngineHid(String applicationEngineHid) {
		this.applicationEngineHid = applicationEngineHid;
	}

	public List<DeviceActionModel> getActions() {
		return actions;
	}

	public void setActions(List<DeviceActionModel> actions) {
		this.actions = actions;
	}
}
