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

public class NodeTypeModel extends DefinitionModelAbstract<NodeTypeModel> {
	private static final long serialVersionUID = 5534197607758563587L;

	private boolean enabled;
	private String deviceCategoryHid;

	@Override
	protected NodeTypeModel self() {
		return this;
	}

	public NodeTypeModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getDeviceCategoryHid() {
		return deviceCategoryHid;
	}
	
	public void setDeviceCategoryHid(String deviceCategoryHid) {
		this.deviceCategoryHid = deviceCategoryHid;
	}
	
	public NodeTypeModel withDeviceCategoryHid(String deviceCategoryHid) {
		setDeviceCategoryHid(deviceCategoryHid);
		return this;
	}
}
