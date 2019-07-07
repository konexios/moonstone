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

public class ApplicationModel extends DefinitionModelAbstract<ApplicationModel> {
	private static final long serialVersionUID = 4714904875033705099L;

	private String companyHid;
	private String productHid;
	private String subscriptionHid;
	private YesNoInherit apiSigningRequired;
	private String defaultSamlEntityId;
	private String code;
	private String zoneSystemName;
	private String zoneHid;
	@JsonIgnore
	private CompanyModel refCompany;
	@JsonIgnore
	private ProductModel refProduct;
	@JsonIgnore
	private SubscriptionModel refSubscription;

	public ApplicationModel withCompanyHid(String companyHid) {
		setCompanyHid(companyHid);
		return this;
	}

	public ApplicationModel withProductHid(String productHid) {
		setProductHid(productHid);
		return this;
	}

	public ApplicationModel withSubscriptionHid(String subscriptionHid) {
		setSubscriptionHid(subscriptionHid);
		return this;
	}

	public ApplicationModel withApiSigningRequired(YesNoInherit apiSigningRequired) {
		setApiSigningRequired(apiSigningRequired);
		return this;
	}

	public ApplicationModel withDefaultSamlEntityId(String defaultSamlEntityId) {
		setDefaultSamlEntityId(defaultSamlEntityId);
		return this;
	}

	public ApplicationModel withCode(String code) {
		setCode(code);
		return this;
	}
	
	public ApplicationModel withZoneHid(String zoneHid) {
		setZoneHid(zoneHid);
		return this;
	}
	
	public ApplicationModel withZoneSystemName(String zoneSystemName) {
		setZoneSystemName(zoneSystemName);
		return this;
	}

	@Override
	protected ApplicationModel self() {
		return this;
	}

	public CompanyModel getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(CompanyModel refCompany) {
		this.refCompany = refCompany;
	}

	public ProductModel getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(ProductModel refProduct) {
		this.refProduct = refProduct;
	}

	public SubscriptionModel getRefSubscription() {
		return refSubscription;
	}

	public void setRefSubscription(SubscriptionModel refSubscription) {
		this.refSubscription = refSubscription;
	}

	public String getCompanyHid() {
		return companyHid;
	}

	public void setCompanyHid(String companyHid) {
		this.companyHid = companyHid;
	}

	public String getProductHid() {
		return productHid;
	}

	public void setProductHid(String productHid) {
		this.productHid = productHid;
	}

	public String getSubscriptionHid() {
		return subscriptionHid;
	}

	public void setSubscriptionHid(String subscriptionHid) {
		this.subscriptionHid = subscriptionHid;
	}

	public YesNoInherit getApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(YesNoInherit apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public String getDefaultSamlEntityId() {
		return defaultSamlEntityId;
	}

	public void setDefaultSamlEntityId(String defaultSamlEntityId) {
		this.defaultSamlEntityId = defaultSamlEntityId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getZoneSystemName() {
		return zoneSystemName;
	}

	public void setZoneSystemName(String zoneSystemName) {
		this.zoneSystemName = zoneSystemName;
	}

	public String getZoneHid() {
		return zoneHid;
	}

	public void setZoneHid(String zoneHid) {
		this.zoneHid = zoneHid;
	}
}
