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

import java.io.Serializable;
import java.time.Instant;

public class CreateSubscriptionModel implements Serializable {

	private static final long serialVersionUID = 7042214314827705530L;

	private String companyHid;
	private Instant startDate;
	private Instant endDate;
	private ContactModel contact;
	private ContactModel billingContact;
	private String name;
	private String description;
	private boolean enabled;

	public CreateSubscriptionModel withCompanyHid(String companyHid) {
		setCompanyHid(companyHid);
		return this;
	}

	public CreateSubscriptionModel withStartDate(Instant startDate) {
		setStartDate(startDate);
		return this;
	}

	public CreateSubscriptionModel withEndDate(Instant endDate) {
		setEndDate(endDate);
		return this;
	}

	public CreateSubscriptionModel withContact(ContactModel contact) {
		setContact(contact);
		return this;
	}

	public CreateSubscriptionModel withBillingContact(ContactModel billingContact) {
		setBillingContact(billingContact);
		return this;
	}

	public CreateSubscriptionModel withName(String name) {
		setName(name);
		return this;
	}

	public CreateSubscriptionModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public CreateSubscriptionModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getCompanyHid() {
		return companyHid;
	}

	public void setCompanyHid(String companyHid) {
		this.companyHid = companyHid;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
