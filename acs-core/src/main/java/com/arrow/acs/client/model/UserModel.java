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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserModel extends AuditableDocumentModelAbstract<UserModel> {
	private static final long serialVersionUID = -3099948569824181477L;

	private String login;
	private UserStatus status;
	private String companyHid;
	private ContactModel contact;
	private AddressModel address;
	private List<String> roleHids = new ArrayList<>();
	@JsonIgnore
	private List<RoleModel> refRoles = new ArrayList<>();

	@Override
	protected UserModel self() {
		return this;
	}

	public UserModel withLogin(String login) {
		setLogin(login);
		return this;
	}

	public UserModel withStatus(UserStatus status) {
		setStatus(status);
		return this;
	}

	public UserModel withCompanyHid(String companyHid) {
		setCompanyHid(companyHid);
		return this;
	}

	public UserModel withContact(ContactModel contact) {
		setContact(contact);
		return this;
	}

	public UserModel withAddress(AddressModel address) {
		setAddress(address);
		return this;
	}

	public UserModel withRoleHids(List<String> roleHids) {
		setRoleHids(roleHids);
		return this;
	}

	public List<RoleModel> getRefRoles() {
		return refRoles;
	}

	public void setRefRoles(List<RoleModel> refRoles) {
		this.refRoles = refRoles;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getCompanyHid() {
		return companyHid;
	}

	public void setCompanyHid(String companyHid) {
		this.companyHid = companyHid;
	}

	public ContactModel getContact() {
		return contact;
	}

	public void setContact(ContactModel contact) {
		this.contact = contact;
	}

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}

	public List<String> getRoleHids() {
		return roleHids;
	}

	public void setRoleHids(List<String> roleHids) {
		this.roleHids = roleHids;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
