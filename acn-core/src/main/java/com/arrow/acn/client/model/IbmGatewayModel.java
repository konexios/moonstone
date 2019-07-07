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

public class IbmGatewayModel extends AuditableDocumentModelAbstract<IbmGatewayModel> {
	private static final long serialVersionUID = -3664854601849510548L;

	private String ibmAccountHid;
	private String gatewayHid;
	private String authToken;
	private boolean enabled;

	@Override
	protected IbmGatewayModel self() {
		return this;
	}

	public IbmGatewayModel withIbmAccountHid(String ibmAccountHid) {
		setIbmAccountHid(ibmAccountHid);
		return this;
	}

	public IbmGatewayModel withGatewayHid(String gatewayHid) {
		setGatewayHid(gatewayHid);
		return this;
	}

	public IbmGatewayModel withAuthToken(String authToken) {
		setAuthToken(authToken);
		return this;
	}

	public IbmGatewayModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getIbmAccountHid() {
		return ibmAccountHid;
	}

	public void setIbmAccountHid(String ibmAccountHid) {
		this.ibmAccountHid = ibmAccountHid;
	}

	public String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
