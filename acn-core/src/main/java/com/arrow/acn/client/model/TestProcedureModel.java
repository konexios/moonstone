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

import com.arrow.acs.client.model.DefinitionModelAbstract;

public class TestProcedureModel extends DefinitionModelAbstract<TestProcedureModel> {
	private static final long serialVersionUID = 2938213963854262466L;
	
	private String deviceTypeHid;
	private List<TestProcedureStepModel> steps = new ArrayList<>();

	@Override
	protected TestProcedureModel self() {
		return this;
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
}
