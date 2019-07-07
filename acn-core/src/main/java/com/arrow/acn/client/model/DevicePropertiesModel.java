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
import java.util.HashMap;
import java.util.Map;

import com.arrow.acs.AcsUtils;

public class DevicePropertiesModel implements Serializable {

	private static final long serialVersionUID = -5181401793241848125L;

	private String deviceHid;
	private Map<String, String> properties = new HashMap<>();

	public void trim() {
		deviceHid = AcsUtils.trimToNull(deviceHid);
	}

	public DevicePropertiesModel withDeviceHid(String deviceHid) {
		setDeviceHid(deviceHid);
		return this;
	}

	public DevicePropertiesModel withProperties(Map<String, String> properties) {
		setProperties(properties);
		return this;
	}

	public String getDeviceHid() {
		return deviceHid;
	}

	public void setDeviceHid(String deviceHid) {
		this.deviceHid = deviceHid;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
