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
import java.util.ArrayList;
import java.util.List;

import com.arrow.acs.AcsUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SamlAccountModel implements Serializable {
	private static final long serialVersionUID = 1789272637985518778L;

	private String principal;
	private String firstName;
	private String lastName;
	private String email;
	private boolean enabled;
	private List<String> defaultRoleHids = new ArrayList<>();
	@JsonIgnore
	private List<RoleModel> refRoles = new ArrayList<>();

	public SamlAccountModel withPrincipal(String principal) {
		setPrincipal(principal);
		return this;
	}

	public SamlAccountModel withFirstName(String firstName) {
		setFirstName(firstName);
		return this;
	}

	public SamlAccountModel withLastName(String lastName) {
		setLastName(lastName);
		return this;
	}

	public SamlAccountModel withEmail(String email) {
		setEmail(email);
		return this;
	}

	public SamlAccountModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public SamlAccountModel withDefaultRoleHids(List<String> defaultRoleHids) {
		setDefaultRoleHids(defaultRoleHids);
		return this;
	}

	public List<RoleModel> getRefRoles() {
		return refRoles;
	}

	public void setRefRoles(List<RoleModel> refRoles) {
		this.refRoles = refRoles;
	}

	public boolean validate() {
		return AcsUtils.isNotEmpty(principal) && (AcsUtils.isNotEmpty(firstName) || AcsUtils.isNotEmpty(lastName));
	}

	public List<String> getDefaultRoleHids() {
		return defaultRoleHids;
	}

	public void setDefaultRoleHids(List<String> defaultRoleHids) {
		this.defaultRoleHids = defaultRoleHids;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
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
		SamlAccountModel other = (SamlAccountModel) obj;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		return true;
	}

	public String toString() {
		return String.format("[%s|%s|%s|%s|%s]", principal, firstName, lastName, email, enabled);
	}
}
