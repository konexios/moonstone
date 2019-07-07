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

import com.arrow.acs.client.model.AuditableDocumentModelAbstract;

public class IbmDeviceModel extends AuditableDocumentModelAbstract<IbmDeviceModel> {
	private static final long serialVersionUID = -1149559101858341899L;

	private String authToken;
	private String ibmAccountHid;
	private String deviceHid;
	private boolean enabled;

	@Override
	protected IbmDeviceModel self() {
		return this;
	}

	public IbmDeviceModel withAuthToken(String authToken) {
		setAuthToken(authToken);
		return this;
	}

	public IbmDeviceModel withIbmAccountHid(String ibmAccountHid) {
		setIbmAccountHid(ibmAccountHid);
		return this;
	}

	public IbmDeviceModel withDeviceHid(String deviceHid) {
		setDeviceHid(deviceHid);
		return this;
	}

	public IbmDeviceModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getIbmAccountHid() {
		return ibmAccountHid;
	}

	public void setIbmAccountHid(String ibmAccountHid) {
		this.ibmAccountHid = ibmAccountHid;
	}

	public String getDeviceHid() {
		return deviceHid;
	}

	public void setDeviceHid(String deviceHid) {
		this.deviceHid = deviceHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
