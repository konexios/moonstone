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

import com.arrow.acs.client.model.TsModelAbstract;

public class ConfigBackupModel extends TsModelAbstract<ConfigBackupModel> {

	private static final long serialVersionUID = 6313590525521543445L;

	public enum Type {
		DEVICE, GATEWAY
	}

	private String name;
	private Type type;
	private String objectHid;
	private DeviceConfigBackupModel deviceConfig;
	private GatewayConfigBackupModel gatewayConfig;

	@Override
	protected ConfigBackupModel self() {
		return this;
	}

	public ConfigBackupModel withName(String name) {
		setName(name);
		return this;
	}

	public ConfigBackupModel withType(Type type) {
		setType(type);
		return this;
	}

	public ConfigBackupModel withDeviceConfig(DeviceConfigBackupModel deviceConfig) {
		setDeviceConfig(deviceConfig);
		return this;
	}

	public ConfigBackupModel withGatewayConfig(GatewayConfigBackupModel gatewayConfig) {
		setGatewayConfig(gatewayConfig);
		return this;
	}

	public ConfigBackupModel withObjectHid(String objectHid) {
		setObjectHid(objectHid);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getObjectHid() {
		return objectHid;
	}

	public void setObjectHid(String objectHid) {
		this.objectHid = objectHid;
	}

	public DeviceConfigBackupModel getDeviceConfig() {
		return deviceConfig;
	}

	public void setDeviceConfig(DeviceConfigBackupModel deviceConfig) {
		this.deviceConfig = deviceConfig;
	}

	public GatewayConfigBackupModel getGatewayConfig() {
		return gatewayConfig;
	}

	public void setGatewayConfig(GatewayConfigBackupModel gatewayConfig) {
		this.gatewayConfig = gatewayConfig;
	}
}
