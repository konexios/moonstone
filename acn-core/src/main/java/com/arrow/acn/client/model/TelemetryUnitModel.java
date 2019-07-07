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

import com.arrow.acs.client.model.DefinitionModelAbstract;

public class TelemetryUnitModel extends DefinitionModelAbstract<TelemetryUnitModel> {

	private static final long serialVersionUID = -3853274969328395791L;

	private String systemName;

	@Override
	protected TelemetryUnitModel self() {
		return this;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public TelemetryUnitModel withSystemName(String systemName) {
		setSystemName(systemName);
		return this;
	}
}
