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

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductModel extends DefinitionModelAbstract<ProductModel> {
	private static final long serialVersionUID = 1078834605777348220L;

	private String systemName;
	private boolean apiSigningRequired;
	private String parentProductHid;

	@JsonIgnore
	private ProductModel refParentProduct;

	@Override
	protected ProductModel self() {
		return this;
	}

	public ProductModel withSystemName(String systemName) {
		setSystemName(systemName);
		return this;
	}

	public ProductModel withApiSigningRequired(boolean apiSigningRequired) {
		setApiSigningRequired(apiSigningRequired);
		return this;
	}

	public ProductModel withParentProductHid(String parentProductHid) {
		setParentProductHid(parentProductHid);
		return this;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public boolean isApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(boolean apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public String getParentProductHid() {
		return parentProductHid;
	}

	public void setParentProductHid(String parentProductHid) {
		this.parentProductHid = parentProductHid;
	}

	public void setRefParentProduct(ProductModel refParentProduct) {
		this.refParentProduct = refParentProduct;
	}

	public ProductModel getRefParentProduct() {
		return refParentProduct;
	}
}
