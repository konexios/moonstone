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

public class TelemetryStatModel implements Serializable {
	private static final long serialVersionUID = -8112688070639907229L;

	private String deviceHid;
	private String name;
	private String value;

	public TelemetryStatModel withDeviceHid(String deviceHid) {
		setDeviceHid(deviceHid);
		return this;
	}

	public TelemetryStatModel withName(String name) {
		setName(name);
		return this;
	}

	public TelemetryStatModel withValue(String value) {
		setValue(value);
		return this;
	}

	public String getDeviceHid() {
		return deviceHid;
	}

	public void setDeviceHid(String deviceHid) {
		this.deviceHid = deviceHid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
