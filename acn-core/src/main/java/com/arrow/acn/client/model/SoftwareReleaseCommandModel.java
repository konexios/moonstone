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

public class SoftwareReleaseCommandModel implements Serializable {
	private static final long serialVersionUID = 3497553393740908159L;

	private String softwareReleaseTransHid;
	private String hid;
	private String fromSoftwareVersion;
	private String toSoftwareVersion;
	private String md5checksum;
	private String tempToken;

	public SoftwareReleaseCommandModel withSoftwareReleaseTransHid(String softwareReleaseTransHid) {
		setSoftwareReleaseTransHid(softwareReleaseTransHid);
		return this;
	}

	public SoftwareReleaseCommandModel withHid(String hid) {
		setHid(hid);
		return this;
	}

	public SoftwareReleaseCommandModel withFromSoftwareVersion(String fromSoftwareVersion) {
		setFromSoftwareVersion(fromSoftwareVersion);
		return this;
	}

	public SoftwareReleaseCommandModel withToSoftwareVersion(String toSoftwareVersion) {
		setToSoftwareVersion(toSoftwareVersion);
		return this;
	}

	public SoftwareReleaseCommandModel withTempToken(String tempToken) {
		setTempToken(tempToken);
		return this;
	}

	public String getSoftwareReleaseTransHid() {
		return softwareReleaseTransHid;
	}

	public void setSoftwareReleaseTransHid(String softwareReleaseTransHid) {
		this.softwareReleaseTransHid = softwareReleaseTransHid;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getFromSoftwareVersion() {
		return fromSoftwareVersion;
	}

	public void setFromSoftwareVersion(String fromSoftwareVersion) {
		this.fromSoftwareVersion = fromSoftwareVersion;
	}

	public String getToSoftwareVersion() {
		return toSoftwareVersion;
	}

	public void setToSoftwareVersion(String toSoftwareVersion) {
		this.toSoftwareVersion = toSoftwareVersion;
	}

	public String getMd5checksum() {
		return md5checksum;
	}

	public void setMd5checksum(String md5checksum) {
		this.md5checksum = md5checksum;
	}

	public String getTempToken() {
		return tempToken;
	}

	public void setTempToken(String tempToken) {
		this.tempToken = tempToken;
	}
}
