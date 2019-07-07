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

public class DeviceModel extends BaseDeviceModelAbstract<DeviceModel> {
	private static final long serialVersionUID = -8361035771470871050L;

	private String type;
	private String gatewayHid;
	private boolean enabled;

	@Override
	protected DeviceModel self() {
		return this;
	}

	public DeviceModel withType(String type) {
		setType(type);
		return this;
	}

	public DeviceModel withGatewayHid(String gatewayHid) {
		setGatewayHid(gatewayHid);
		return this;
	}

	public DeviceModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
