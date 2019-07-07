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

public class CreateCompanyModel implements Serializable {

	private static final long serialVersionUID = 737309244917140959L;

	private String name;
	private String abbrName;
	private CompanyStatus status;
	private AddressModel address;
	private AddressModel billingAddress;
	private ContactModel contact;
	private ContactModel billingContact;
	private PasswordPolicyModel passwordPolicy;
	private LoginPolicyModel loginPolicy;
	private String parentCompanyHid;

	public CreateCompanyModel withName(String name) {
		setName(name);
		return this;
	}

	public CreateCompanyModel withAbbrName(String abbrName) {
		setAbbrName(abbrName);
		return this;
	}

	public CreateCompanyModel withStatus(CompanyStatus status) {
		setStatus(status);
		return this;
	}

	public CreateCompanyModel withAddress(AddressModel address) {
		setAddress(address);
		return this;
	}

	public CreateCompanyModel withBillingAddress(AddressModel billingAddress) {
		setBillingAddress(billingAddress);
		return this;
	}

	public CreateCompanyModel withContact(ContactModel contact) {
		setContact(contact);
		return this;
	}

	public CreateCompanyModel withBillingContact(ContactModel billingContact) {
		setBillingContact(billingContact);
		return this;
	}

	public CreateCompanyModel withPasswordPolicyModel(PasswordPolicyModel passwordPolicyModel) {
		setPasswordPolicy(passwordPolicyModel);
		return this;
	}

	public CreateCompanyModel withLoginPolicy(LoginPolicyModel loginPolicy) {
		setLoginPolicy(loginPolicy);
		return this;
	}

	public CreateCompanyModel withParentCompanyHid(String parentCompanyHid) {
		setParentCompanyHid(parentCompanyHid);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	public CompanyStatus getStatus() {
		return status;
	}

	public void setStatus(CompanyStatus status) {
		this.status = status;
	}

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}

	public AddressModel getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressModel billingAddress) {
		this.billingAddress = billingAddress;
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

	public PasswordPolicyModel getPasswordPolicy() {
		return passwordPolicy;
	}

	public void setPasswordPolicy(PasswordPolicyModel passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public LoginPolicyModel getLoginPolicy() {
		return loginPolicy;
	}

	public void setLoginPolicy(LoginPolicyModel loginPolicy) {
		this.loginPolicy = loginPolicy;
	}

	public String getParentCompanyHid() {
		return parentCompanyHid;
	}

	public void setParentCompanyHid(String parentCompanyHid) {
		this.parentCompanyHid = parentCompanyHid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abbrName == null) ? 0 : abbrName.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + ((billingContact == null) ? 0 : billingContact.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((loginPolicy == null) ? 0 : loginPolicy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentCompanyHid == null) ? 0 : parentCompanyHid.hashCode());
		result = prime * result + ((passwordPolicy == null) ? 0 : passwordPolicy.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateCompanyModel other = (CreateCompanyModel) obj;
		if (abbrName == null) {
			if (other.abbrName != null)
				return false;
		} else if (!abbrName.equals(other.abbrName))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (billingAddress == null) {
			if (other.billingAddress != null)
				return false;
		} else if (!billingAddress.equals(other.billingAddress))
			return false;
		if (billingContact == null) {
			if (other.billingContact != null)
				return false;
		} else if (!billingContact.equals(other.billingContact))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (loginPolicy == null) {
			if (other.loginPolicy != null)
				return false;
		} else if (!loginPolicy.equals(other.loginPolicy))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentCompanyHid == null) {
			if (other.parentCompanyHid != null)
				return false;
		} else if (!parentCompanyHid.equals(other.parentCompanyHid))
			return false;
		if (passwordPolicy == null) {
			if (other.passwordPolicy != null)
				return false;
		} else if (!passwordPolicy.equals(other.passwordPolicy))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
}
