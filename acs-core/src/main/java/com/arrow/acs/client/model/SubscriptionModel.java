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

public class SubscriptionModel extends DefinitionModelAbstract<SubscriptionModel> {
	private static final long serialVersionUID = -2611087253790754890L;

	private String companyHid;
	private String startDate;
	private String endDate;
	private ContactModel contact;
	private ContactModel billingContact;
	@JsonIgnore
	private CompanyModel refCompany;

	@Override
	protected SubscriptionModel self() {
		return this;
	}

	public SubscriptionModel withCompanyHid(String companyHid) {
		setCompanyHid(companyHid);
		return this;
	}

	public SubscriptionModel withStartDate(String startDate) {
		setStartDate(startDate);
		return this;
	}

	public SubscriptionModel withEndDate(String endDate) {
		setEndDate(endDate);
		return this;
	}

	public SubscriptionModel withContact(ContactModel contact) {
		setContact(contact);
		return this;
	}

	public SubscriptionModel withBillingContact(ContactModel contact) {
		setBillingContact(contact);
		return this;
	}

	public CompanyModel getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(CompanyModel refCompany) {
		this.refCompany = refCompany;
	}

	public String getCompanyHid() {
		return companyHid;
	}

	public void setCompanyHid(String companyHid) {
		this.companyHid = companyHid;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ContactModel getContact() {
		return contact;
	}

	public void setContact(ContactModel contact) {
		this.contact = contact;
	}

	public ContactModel getBillingContact() {
		return billingContact;
	}

	public void setBillingContact(ContactModel billingContact) {
		this.billingContact = billingContact;
	}
}
