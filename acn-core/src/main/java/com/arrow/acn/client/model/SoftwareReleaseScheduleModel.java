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

import com.arrow.acs.client.model.AuditableDocumentModelAbstract;

public class SoftwareReleaseScheduleModel extends AuditableDocumentModelAbstract<SoftwareReleaseScheduleModel> {

	private static final long serialVersionUID = 3666946823955944387L;

	private String applicationHid;
	private String scheduledDate;
	private String softwareReleaseHid;
	// private String deviceCategoryHid;
	private AcnDeviceCategory deviceCategory;
	private String comments;
	private List<String> objectHids = new ArrayList<String>();
	private SoftwareReleaseScheduleStatus status;
	private boolean notifyOnStart;
	private boolean notifyOnEnd;
	private boolean notifyOnSubmit;
	private String notifyEmails;
	private String name;
	private boolean onDemand;
	private String deviceTypeHid;
	private String hardwareVersionHid;
	private Long timeToExpireSeconds;

	@Override
	protected SoftwareReleaseScheduleModel self() {
		return this;
	}

	public SoftwareReleaseScheduleModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public SoftwareReleaseScheduleModel withScheduledDate(String scheduledDate) {
		setScheduledDate(scheduledDate);
		return this;
	}

	public SoftwareReleaseScheduleModel withSoftwareReleaseHid(String softwareReleaseHid) {
		setSoftwareReleaseHid(softwareReleaseHid);
		return this;
	}

	public SoftwareReleaseScheduleModel withComments(String comments) {
		setComments(comments);
		return this;
	}

	public SoftwareReleaseScheduleModel withObjectHids(List<String> objectHids) {
		setObjectHids(objectHids);
		return this;
	}

	public SoftwareReleaseScheduleModel withObjectHid(String objectHid) {
		objectHids.add(objectHid);
		return this;
	}

	public SoftwareReleaseScheduleModel withStatus(SoftwareReleaseScheduleStatus status) {
		setStatus(status);
		return this;
	}

	public SoftwareReleaseScheduleModel withNotifyOnStart(boolean notifyOnStart) {
		setNotifyOnStart(notifyOnStart);
		return this;
	}

	public SoftwareReleaseScheduleModel withNotifyOnEnd(boolean notifyOnEnd) {
		setNotifyOnEnd(notifyOnEnd);
		return this;
	}

	public SoftwareReleaseScheduleModel withNotifyEmails(String notifyEmails) {
		setNotifyEmails(notifyEmails);
		return this;
	}

	// public SoftwareReleaseScheduleModel withDeviceCategoryHid(String
	// deviceCategoryHid) {
	// setDeviceCategoryHid(deviceCategoryHid);
	// return this;
	// }

	public SoftwareReleaseScheduleModel withDeviceCategory(AcnDeviceCategory deviceCategory) {
		setDeviceCategory(deviceCategory);
		return this;
	}

	public SoftwareReleaseScheduleModel withNotifyOnSubmit(boolean notifyOnSubmit) {
		setNotifyOnSubmit(notifyOnSubmit);
		return this;
	}

	public SoftwareReleaseScheduleModel withName(String name) {
		setName(name);
		return this;
	}

	public SoftwareReleaseScheduleModel withDeviceTypeHid(String deviceTypeHid) {
		setDeviceTypeHid(deviceTypeHid);
		return this;
	}

	public SoftwareReleaseScheduleModel withOnDemand(boolean onDemand) {
		setOnDemand(onDemand);
		return this;
	}

	public SoftwareReleaseScheduleModel withHardwareVersionHid(String hardwareVersionHid) {
		setHardwareVersionHid(hardwareVersionHid);
		return this;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getSoftwareReleaseHid() {
		return softwareReleaseHid;
	}

	public void setSoftwareReleaseHid(String softwareReleaseHid) {
		this.softwareReleaseHid = softwareReleaseHid;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<String> getObjectHids() {
		return objectHids;
	}

	public void setObjectHids(List<String> objectHids) {
		this.objectHids.addAll(objectHids);
	}

	public SoftwareReleaseScheduleStatus getStatus() {
		return status;
	}

	public void setStatus(SoftwareReleaseScheduleStatus status) {
		this.status = status;
	}

	public boolean isNotifyOnStart() {
		return notifyOnStart;
	}

	public void setNotifyOnStart(boolean notifyOnStart) {
		this.notifyOnStart = notifyOnStart;
	}

	public boolean isNotifyOnEnd() {
		return notifyOnEnd;
	}

	public void setNotifyOnEnd(boolean notifyOnEnd) {
		this.notifyOnEnd = notifyOnEnd;
	}

	public String getNotifyEmails() {
		return notifyEmails;
	}

	public void setNotifyEmails(String notifyEmails) {
		this.notifyEmails = notifyEmails;
	}

	// public String getDeviceCategoryHid() {
	// return deviceCategoryHid;
	// }
	//
	// public void setDeviceCategoryHid(String deviceCategoryHid) {
	// this.deviceCategoryHid = deviceCategoryHid;
	// }

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public boolean getNotifyOnSubmit() {
		return notifyOnSubmit;
	}

	public void setNotifyOnSubmit(boolean notifyOnSubmit) {
		this.notifyOnSubmit = notifyOnSubmit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOnDemand() {
		return onDemand;
	}

	public void setOnDemand(boolean onDemand) {
		this.onDemand = onDemand;
	}

	public String getDeviceTypeHid() {
		return deviceTypeHid;
	}

	public void setDeviceTypeHid(String deviceTypeHid) {
		this.deviceTypeHid = deviceTypeHid;
	}

	public String getHardwareVersionHid() {
		return hardwareVersionHid;
	}

	public void setHardwareVersionHid(String hardwareVersionHid) {
		this.hardwareVersionHid = hardwareVersionHid;
	}

	public Long getTimeToExpireSeconds() {
		return timeToExpireSeconds;
	}

	public void setTimeToExpireSeconds(Long timeToExpireSeconds) {
		this.timeToExpireSeconds = timeToExpireSeconds;
	}
}
