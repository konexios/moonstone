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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.arrow.acs.client.model.ModelAbstract;

public class DeviceStateTransModel extends ModelAbstract<DeviceStateTransModel> {
	private static final long serialVersionUID = -772811082617600920L;

	private String deviceHid;
	private String status;
	private String error;
	private Map<String, DeviceStateValueModel> states = new HashMap<>();

	public DeviceStateTransModel withDeviceHid(String deviceHid) {
		setDeviceHid(deviceHid);
		return this;
	}

	public DeviceStateTransModel withStates(Map<String, DeviceStateValueModel> states) {
		setStates(states);
		return this;
	}

	public DeviceStateTransModel withState(String name, String value) {
		states.put(name, new DeviceStateValueModel().withValue(value).withTimestamp(Instant.now()));
		return this;
	}

	public DeviceStateTransModel withStatus(String status) {
		setStatus(status);
		return this;
	}

	public DeviceStateTransModel withError(String error) {
		setError(error);
		return this;
	}

	public String getDeviceHid() {
		return deviceHid;
	}

	public void setDeviceHid(String deviceHid) {
		this.deviceHid = deviceHid;
	}

	public Map<String, DeviceStateValueModel> getStates() {
		return states;
	}

	public void setStates(Map<String, DeviceStateValueModel> states) {
		this.states = states;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	protected DeviceStateTransModel self() {
		return this;
	}
}
