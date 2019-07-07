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

import java.util.HashMap;
import java.util.Map;

import com.arrow.acs.client.model.AuditableDocumentModelAbstract;

public abstract class BaseDeviceModelAbstract<T extends BaseDeviceModelAbstract<T>>
		extends AuditableDocumentModelAbstract<T> {
	private static final long serialVersionUID = -5456674946112674229L;

	protected String uid;
	protected String name;
	protected String userHid;
	protected String nodeHid;
	protected String softwareName;
	protected String softwareVersion;
	protected String softwareReleaseHid;
	protected String softwareReleaseName;
	protected String softwareReleaseVersion;

	protected Map<String, String> info = new HashMap<>();
	protected Map<String, String> properties = new HashMap<>();

	public T withUid(String uid) {
		setUid(uid);
		return self();
	}

	public T withName(String name) {
		setName(name);
		return self();
	}

	public T withUserHid(String userHid) {
		setUserHid(userHid);
		return self();
	}

	public T withNodeHid(String nodeHid) {
		setNodeHid(nodeHid);
		return self();
	}

	public T withSoftwareName(String softwareName) {
		setSoftwareName(softwareName);
		return self();
	}

	public T withSoftwareVersion(String softwareVersion) {
		setSoftwareVersion(softwareVersion);
		return self();
	}

	public T withSoftwareReleaseHid(String softwareReleaseHid) {
		setSoftwareReleaseHid(softwareReleaseHid);
		return self();
	}

	public T withSoftwareReleaseName(String softwareReleaseName) {
		setSoftwareReleaseName(softwareReleaseName);
		return self();
	}

	public T withSoftwareReleaseVersion(String softwareReleaseVersion) {
		setSoftwareReleaseVersion(softwareReleaseVersion);
		return self();
	}

	public T withInfo(Map<String, String> info) {
		setInfo(info);
		return self();
	}

	public T withProperties(Map<String, String> properties) {
		setProperties(properties);
		return self();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	public String getNodeHid() {
		return nodeHid;
	}

	public void setNodeHid(String nodeHid) {
		this.nodeHid = nodeHid;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getSoftwareReleaseHid() {
		return softwareReleaseHid;
	}

	public void setSoftwareReleaseHid(String softwareReleaseHid) {
		this.softwareReleaseHid = softwareReleaseHid;
	}

	public String getSoftwareReleaseName() {
		return softwareReleaseName;
	}

	public void setSoftwareReleaseName(String softwareReleaseName) {
		this.softwareReleaseName = softwareReleaseName;
	}

	public String getSoftwareReleaseVersion() {
		return softwareReleaseVersion;
	}

	public void setSoftwareReleaseVersion(String softwareReleaseVersion) {
		this.softwareReleaseVersion = softwareReleaseVersion;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
