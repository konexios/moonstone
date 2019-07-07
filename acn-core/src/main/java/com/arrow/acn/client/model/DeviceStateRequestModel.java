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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class DeviceStateRequestModel implements Serializable {
	private static final long serialVersionUID = -6083315821383368773L;

	private Map<String, String> states = new HashMap<>();
	private String timestamp;

	public DeviceStateRequestModel withTimestamp(Instant timestamp) {
		setTimestamp(timestamp.toString());
		return this;
	}

	public DeviceStateRequestModel withState(String name, String value) {
		states.put(name, value);
		return this;
	}

	public DeviceStateRequestModel withStates(Map<String, String> states) {
		setStates(states);
		return this;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, String> getStates() {
		return states;
	}

	public void setStates(Map<String, String> states) {
		this.states = states;
	}
}
