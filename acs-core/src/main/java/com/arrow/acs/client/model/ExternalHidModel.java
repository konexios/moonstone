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

public class ExternalHidModel extends HidModel {
	private static final long serialVersionUID = -4548385746085920390L;

	private String externalId;

	public ExternalHidModel withExternalId(String externalId) {
		setExternalId(externalId);
		return this;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Override
	protected HidModel self() {
		return this;
	}
}
