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

import com.arrow.acs.AcsUtils;

public class DeviceCommandModel implements Serializable {

	private static final long serialVersionUID = -4014798127395307414L;

	private String deviceHid;
	private String command;
	private String payload;
	private Long messageExpiration;

	public void trim() {
		deviceHid = AcsUtils.trimToNull(deviceHid);
		command = AcsUtils.trimToNull(command);
		payload = AcsUtils.trimToNull(payload);
	}

	public DeviceCommandModel withDeviceHid(String deviceHid) {
		setDeviceHid(deviceHid);
		return this;
	}

	public DeviceCommandModel withCommand(String command) {
		setCommand(command);
		return this;
	}

	public DeviceCommandModel withPayload(String payload) {
		setPayload(payload);
		return this;
	}

	public DeviceCommandModel withMessageExpiration(Long expiration) {
		setMessageExpiration(expiration);
		return this;
	}

	public String getDeviceHid() {
		return deviceHid;
	}

	public void setDeviceHid(String deviceHid) {
		this.deviceHid = deviceHid;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Long getMessageExpiration() {
		return messageExpiration;
	}

	public void setMessageExpiration(Long expiration) {
		this.messageExpiration = expiration;
	}
}
