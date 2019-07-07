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
import java.util.HashSet;
import java.util.Set;

public class SoftwareReleaseScheduleAutomationModel implements Serializable {

	private static final long serialVersionUID = 650773309220706022L;

	private AcnDeviceCategory deviceCategory;
	private Set<String> objectHids = new HashSet<String>();
	private String softwareReleaseHid;
	private String userHid;
	private Long timeToExpireSeconds;
	private String jobName;

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public Set<String> getObjectHids() {
		return objectHids;
	}

	public void setObjectHids(Set<String> objectHids) {
		this.objectHids = objectHids;
	}

	public String getSoftwareReleaseHid() {
		return softwareReleaseHid;
	}

	public void setSoftwareReleaseHid(String softwareReleaseHid) {
		this.softwareReleaseHid = softwareReleaseHid;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	public Long getTimeToExpireSeconds() {
		return timeToExpireSeconds;
	}

	public void setTimeToExpireSeconds(Long timeToExpireSeconds) {
		this.timeToExpireSeconds = timeToExpireSeconds;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}
