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

public class AzureDeviceModel extends AuditableDocumentModelAbstract<AzureDeviceModel> {

	private static final long serialVersionUID = 8960101654526157966L;

	private String azureAccountHid;
	private String gatewayHid;
	private String primaryKey;
	private String secondaryKey;
	private boolean enabled;

	@Override
	protected AzureDeviceModel self() {
		return this;
	}

	public AzureDeviceModel withAzureAccountHid(String azureAccountHid) {
		setAzureAccountHid(azureAccountHid);
		return this;
	}

	public AzureDeviceModel withGatewayHid(String gatewayHid) {
		setGatewayHid(gatewayHid);
		return this;
	}

	public AzureDeviceModel withPrimaryKey(String primaryKey) {
		setPrimaryKey(primaryKey);
		return this;
	}

	public AzureDeviceModel withSecondaryKey(String secondaryKey) {
		setSecondaryKey(secondaryKey);
		return this;
	}

	public AzureDeviceModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getAzureAccountHid() {
		return azureAccountHid;
	}

	public void setAzureAccountHid(String azureAccountHid) {
		this.azureAccountHid = azureAccountHid;
	}

	public String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getSecondaryKey() {
		return secondaryKey;
	}

	public void setSecondaryKey(String secondaryKey) {
		this.secondaryKey = secondaryKey;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
