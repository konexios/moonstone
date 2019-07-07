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
import java.util.ArrayList;
import java.util.List;

import com.arrow.acs.AcsUtils;

public class TestProcedureRegistrationModel implements Serializable {
	private static final long serialVersionUID = 8527833325939436585L;

	private String name;
	private String description;
	private boolean enabled = true;
	private String deviceTypeHid;
	private List<TestProcedureStepModel> steps = new ArrayList<>();

	public void trim() {
		name = AcsUtils.trimToNull(name);
		description = AcsUtils.trimToNull(description);
		deviceTypeHid = AcsUtils.trimToNull(deviceTypeHid);
	}

	public List<TestProcedureStepModel> getSteps() {
		return steps;
	}

	public void setSteps(List<TestProcedureStepModel> testProcedureSteps) {
		this.steps = testProcedureSteps;
	}

	public String getDeviceTypeHid() {
		return deviceTypeHid;
	}

	public void setDeviceTypeHid(String deviceTypeHid) {
		this.deviceTypeHid = deviceTypeHid;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
