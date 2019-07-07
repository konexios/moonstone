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

import java.util.HashMap;
import java.util.Map;

import com.arrow.acs.client.model.DefinitionModelAbstract;

public class DeviceActionTypeModel extends DefinitionModelAbstract<DeviceActionTypeModel> {

	private static final long serialVersionUID = 4188855459940428628L;

	private boolean enabled;
	private String systemName;
	private String applicationHid;
	private Map<String, String> parameters = new HashMap<>();

	@Override
	protected DeviceActionTypeModel self() {
		return this;
	}

	public DeviceActionTypeModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public DeviceActionTypeModel withSystemName(String systemName) {
		setSystemName(systemName);
		return this;
	}

	public DeviceActionTypeModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public DeviceActionTypeModel withParameters(Map<String, String> parameters) {
		setParameters(parameters);
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
