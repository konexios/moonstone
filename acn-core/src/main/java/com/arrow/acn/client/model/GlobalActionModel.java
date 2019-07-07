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

import java.util.ArrayList;
import java.util.List;

import com.arrow.acs.client.model.DefinitionModelAbstract;

public class GlobalActionModel extends DefinitionModelAbstract<GlobalActionModel> {

	private static final long serialVersionUID = 852549552944909034L;

	private String globalActionTypeHid;
	private String applicationHid;
	private String systemName;
	private List<GlobalActionPropertyModel> properties = new ArrayList<>();
	private List<GlobalActionInputModel> input = new ArrayList<>();

	@Override
	protected GlobalActionModel self() {
		return this;
	}

	public String getGlobalActionTypeHid() {
		return globalActionTypeHid;
	}

	public void setGlobalActionTypeHid(String globalActionTypeHid) {
		this.globalActionTypeHid = globalActionTypeHid;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public List<GlobalActionPropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(List<GlobalActionPropertyModel> properties) {
		this.properties = properties;
	}

	public List<GlobalActionInputModel> getInput() {
		return input;
	}

	public void setInput(List<GlobalActionInputModel> input) {
		this.input = input;
	}
}
