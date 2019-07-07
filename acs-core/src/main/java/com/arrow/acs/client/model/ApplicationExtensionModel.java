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

public class ApplicationExtensionModel extends AuditableDocumentModelAbstract<ApplicationExtensionModel> {
	private static final long serialVersionUID = 4656876691147842639L;

	private String applicationHid;
	private String productExtensionHid;
	private boolean enabled = true;

	@Override
	protected ApplicationExtensionModel self() {
		return this;
	}

	public ApplicationExtensionModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public ApplicationExtensionModel withProductionExtensionHid(String productExtensionHid) {
		setProductExtensionHid(productExtensionHid);
		return this;
	}

	public ApplicationExtensionModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public String getProductExtensionHid() {
		return productExtensionHid;
	}

	public void setProductExtensionHid(String productExtensionHid) {
		this.productExtensionHid = productExtensionHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
