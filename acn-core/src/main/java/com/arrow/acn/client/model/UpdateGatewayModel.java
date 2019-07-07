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

public class UpdateGatewayModel extends CreateGatewayModel {
	private static final long serialVersionUID = -1587112251938386153L;

	private String hid;

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}
}
