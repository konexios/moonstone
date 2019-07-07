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

import com.arrow.acs.AcsUtils;

public class NodeTypeRegistrationModel implements Serializable {
	private static final long serialVersionUID = -4297835378597209705L;

	private String name;
	private String description;
	private boolean enabled = true;
	private String deviceCategoryHid;

	public void trim() {
		name = AcsUtils.trimToNull(name);
		description = AcsUtils.trimToNull(description);
	}

	public NodeTypeRegistrationModel withName(String name) {
		setName(name);
		return this;
	}

	public NodeTypeRegistrationModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public NodeTypeRegistrationModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public NodeTypeRegistrationModel withDeviceCategoryHid(String deviceCategoryHid) {
		setDeviceCategoryHid(deviceCategoryHid);
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDeviceCategoryHid() {
		return deviceCategoryHid;
	}

	public void setDeviceCategoryHid(String deviceCategoryHid) {
		this.deviceCategoryHid = deviceCategoryHid;
	}
}
