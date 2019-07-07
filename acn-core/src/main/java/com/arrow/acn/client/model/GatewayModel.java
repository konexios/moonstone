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

public class GatewayModel extends BaseDeviceModelAbstract<GatewayModel> {
	private static final long serialVersionUID = 8738104557146056949L;

	public enum GatewayType {
		Local, Cloud, Mobile
	}

	private GatewayType type;
	private String deviceType;
	private String osName;
	private String sdkVersion;
	private String applicationHid;

	@Override
	protected GatewayModel self() {
		return this;
	}

	public GatewayModel withType(GatewayType type) {
		setType(type);
		return this;
	}

	public GatewayModel withDeviceType(String deviceType) {
		setDeviceType(deviceType);
		return this;
	}

	public GatewayModel withOsName(String osName) {
		setOsName(osName);
		return this;
	}
	
	public GatewayModel withSdkVersion(String sdkVersion) {
		setSdkVersion(sdkVersion);
		return this;
	}

	public GatewayModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public GatewayType getType() {
		return type;
	}

	public void setType(GatewayType type) {
		this.type = type;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public String getApplicationHid() {
		return applicationHid;
	}
}