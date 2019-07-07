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

public class IbmAccountModel extends AuditableDocumentModelAbstract<IbmAccountModel> {
	private static final long serialVersionUID = -5704708138765500714L;

	private String apiKey;
	private String applicationHid;
	private String authToken;
	private String organizationId;
	private String userHid;
	private boolean enabled;

	@Override
	protected IbmAccountModel self() {
		return this;
	}

	public IbmAccountModel withApiKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}

	public IbmAccountModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public IbmAccountModel withAuthToken(String authToken) {
		setAuthToken(authToken);
		return this;
	}

	public IbmAccountModel withOrganizationId(String organizationId) {
		setOrganizationId(organizationId);
		return this;
	}

	public IbmAccountModel withUserHid(String userHid) {
		setUserHid(userHid);
		return this;
	}

	public IbmAccountModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
