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
package com.arrow.acs.client.model;

public class UpdateSubscriptionModel extends CreateSubscriptionModel {

	private static final long serialVersionUID = -4374072722271565293L;

	private String hid;

	public UpdateSubscriptionModel withHid(String hid) {
		setHid(hid);
		return this;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}
}
