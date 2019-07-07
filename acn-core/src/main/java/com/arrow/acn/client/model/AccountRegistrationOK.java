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
package com.arrow.acn.client.model;

import com.arrow.acs.client.model.HidModel;

public class AccountRegistrationOK extends HidModel {
	private static final long serialVersionUID = -331531657230625568L;

	private String email;
	private String name;
	private String applicationHid;

	public AccountRegistrationOK withEmail(String email) {
		setEmail(email);
		return this;
	}

	public AccountRegistrationOK withName(String name) {
		setName(name);
		return this;
	}

	public AccountRegistrationOK withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((applicationHid == null) ? 0 : applicationHid.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountRegistrationOK other = (AccountRegistrationOK) obj;
		if (applicationHid == null) {
			if (other.applicationHid != null)
				return false;
		} else if (!applicationHid.equals(other.applicationHid))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
