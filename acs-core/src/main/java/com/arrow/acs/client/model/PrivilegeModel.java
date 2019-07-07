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

public class PrivilegeModel extends DefinitionModelAbstract<PrivilegeModel> {
	private static final long serialVersionUID = 857418574760029905L;

	private String productHid;
	private String systemName;

	@Override
	protected PrivilegeModel self() {
		return this;
	}

	public PrivilegeModel withProductHid(String productHid) {
		setProductHid(productHid);
		return this;
	}

	public PrivilegeModel withSystemName(String systemName) {
		setSystemName(systemName);
		return this;
	}

	public String getProductHid() {
		return productHid;
	}

	public void setProductHid(String productHid) {
		this.productHid = productHid;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
