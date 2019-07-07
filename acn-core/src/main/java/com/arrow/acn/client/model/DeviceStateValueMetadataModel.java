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

public class DeviceStateValueMetadataModel implements Serializable {
	private static final long serialVersionUID = 2160270507833662715L;

	private String name;
	private String description;
	private DeviceStateValueType type = DeviceStateValueType.String;

	public DeviceStateValueMetadataModel withName(String name) {
		setName(name);
		return this;
	}

	public DeviceStateValueMetadataModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public DeviceStateValueMetadataModel withType(DeviceStateValueType type) {
		setType(type);
		return this;
	}

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

	public DeviceStateValueType getType() {
		return type;
	}

	public void setType(DeviceStateValueType type) {
		this.type = type;
	}
}
