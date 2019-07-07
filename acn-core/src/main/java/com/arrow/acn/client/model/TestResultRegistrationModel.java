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

public class TestResultRegistrationModel implements Serializable  {
	private static final long serialVersionUID = 5667361130792298479L;
	
	//private DeviceCategory category; 
	private String objectHid;
	private String status;
	private String testProcedureHid;
	private List<TestResultStepModel> steps = new ArrayList<>();
	private String started;
	private String ended;

	public String getTestProcedureHid() {
		return testProcedureHid;
	}

	public void setTestProcedureHid(String testProcedureId) {
		this.testProcedureHid = testProcedureId;
	}

	public TestResultRegistrationModel withTestProcedureHid(String testProcedureId) {
		this.testProcedureHid = testProcedureId;
		return this;
	}

	public String getObjectHid() {
		return objectHid;
	}

	public void setObjectHid(String objectId) {
		this.objectHid = objectId;
	}

	public TestResultRegistrationModel withObjectHid(String objectId) {
		this.objectHid = objectId;
		return this;
	}

	/*public DeviceCategory getCategory() {
        return category;
    }

    public void setCategory(DeviceCategory category) {
        this.category = category;
    }

    public TestResultCommonModel withCategory(DeviceCategory category) {
        this.category = category;
        return this;
    }*/

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TestResultRegistrationModel withStatus(String status) {
		this.status = status;
		return this;
	}

	public List<TestResultStepModel> getSteps() {
		return steps;
	}

	public void setSteps(List<TestResultStepModel> steps) {
		this.steps = steps;
	}

	public String getStarted() {
		return started;
	}

	public void setStarted(String started) {
		this.started = started;
	}
	
	public String getEnded() {
		return ended;
	}

	public void setEnded(String ended) {
		this.ended = ended;
	}
}
