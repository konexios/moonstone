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

public class ProductExtensionModel extends AuditableDocumentModelAbstract<ProductExtensionModel> {
	private static final long serialVersionUID = -8531276228194317252L;

	private String productHid;

	@Override
	protected ProductExtensionModel self() {
		return this;
	}

	public ProductExtensionModel withProductHid(String productHid) {
		setProductHid(productHid);
		return this;
	}

	public String getProductHid() {
		return productHid;
	}

	public void setProductHid(String productHid) {
		this.productHid = productHid;
	}
}
