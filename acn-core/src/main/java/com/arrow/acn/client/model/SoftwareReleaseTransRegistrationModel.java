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

public class SoftwareReleaseTransRegistrationModel implements Serializable {

	private static final long serialVersionUID = -5156352330326271900L;

	private String objectHid;
	// private String deviceCategoryHid;
	private AcnDeviceCategory deviceCategory;
	private String softwareReleaseScheduleHid;
	private String fromSoftwareReleaseHid;
	private String toSoftwareReleaseHid;
	private SoftwareReleaseTransStatus status;
	private String error;
	private String relatedSoftwareReleaseTransHid;

	public SoftwareReleaseTransRegistrationModel withObjectHid(String objectHid) {
		setObjectHid(objectHid);
		return this;
	}

	public SoftwareReleaseTransRegistrationModel withSoftwareReleaseScheduleHid(String softwareReleaseScheduleHid) {
		setSoftwareReleaseScheduleHid(softwareReleaseScheduleHid);
		return this;
	}

	public SoftwareReleaseTransRegistrationModel withFromSoftwareReleaseHid(String fromSoftwareReleaseHid) {
		setFromSoftwareReleaseHid(fromSoftwareReleaseHid);
		return this;
	}

	public SoftwareReleaseTransRegistrationModel withToSoftwareReleaseHid(String toSoftwareReleaseHid) {
		setToSoftwareReleaseHid(toSoftwareReleaseHid);
		return this;
	}

	public SoftwareReleaseTransRegistrationModel withStatus(SoftwareReleaseTransStatus status) {
		setStatus(status);
		return this;
	}

	public SoftwareReleaseTransRegistrationModel withRelatedSoftwareReleaseTransHid(
	        String relatedSoftwareReleaseTransHid) {
		setRelatedSoftwareReleaseTransHid(relatedSoftwareReleaseTransHid);
		return this;
	}

	public SoftwareReleaseTransRegistrationModel withError(String error) {
		setError(error);
		return this;
	}

	// public SoftwareReleaseTransRegistrationModel withDeviceCategoryHid(String
	// deviceCategoryHid) {
	// setDeviceCategoryHid(deviceCategoryHid);
	// return this;
	// }

	public SoftwareReleaseTransRegistrationModel withDeviceCategory(AcnDeviceCategory deviceCategory) {
		setDeviceCategory(deviceCategory);
		return this;
	}

	public String getObjectHid() {
		return objectHid;
	}

	public void setObjectHid(String objectHid) {
		this.objectHid = objectHid;
	}

	public String getSoftwareReleaseScheduleHid() {
		return softwareReleaseScheduleHid;
	}

	public void setSoftwareReleaseScheduleHid(String softwareReleaseScheduleHid) {
		this.softwareReleaseScheduleHid = softwareReleaseScheduleHid;
	}

	public String getFromSoftwareReleaseHid() {
		return fromSoftwareReleaseHid;
	}

	public void setFromSoftwareReleaseHid(String fromSoftwareReleaseHid) {
		this.fromSoftwareReleaseHid = fromSoftwareReleaseHid;
	}

	public String getToSoftwareReleaseHid() {
		return toSoftwareReleaseHid;
	}

	public void setToSoftwareReleaseHid(String toSoftwareReleaseHid) {
		this.toSoftwareReleaseHid = toSoftwareReleaseHid;
	}

	public SoftwareReleaseTransStatus getStatus() {
		return status;
	}

	public void setStatus(SoftwareReleaseTransStatus status) {
		this.status = status;
	}

	public String getRelatedSoftwareReleaseTransHid() {
		return relatedSoftwareReleaseTransHid;
	}

	public void setRelatedSoftwareReleaseTransHid(String relatedSoftwareReleaseTransHid) {
		this.relatedSoftwareReleaseTransHid = relatedSoftwareReleaseTransHid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
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
}
