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

import java.io.Serializable;

public class CreateSocialEventRegistrationModel implements Serializable {
	private static final long serialVersionUID = 7677396417422603524L;

	private String socialEventHid;
	private String origPassword;
	private String origEmail;
	private String email;
	private String userHid;

	public CreateSocialEventRegistrationModel withSocialEventHid(String socialEventHid) {
		setSocialEventHid(socialEventHid);
		return this;
	}

	public CreateSocialEventRegistrationModel withOrigPassword(String origPassword) {
		setOrigPassword(origPassword);
		return this;
	}

	public CreateSocialEventRegistrationModel withOrigEmail(String origEmail) {
		setOrigEmail(origEmail);
		return this;
	}

	public CreateSocialEventRegistrationModel withEmail(String email) {
		setEmail(email);
		return this;
	}

	public CreateSocialEventRegistrationModel withUserHid(String userHid) {
		setUserHid(userHid);
		return this;
	}

	public String getSocialEventHid() {
		return socialEventHid;
	}

	public void setSocialEventHid(String socialEventHid) {
		this.socialEventHid = socialEventHid;
	}

	public String getOrigPassword() {
		return origPassword;
	}

	public void setOrigPassword(String origPassword) {
		this.origPassword = origPassword;
	}

	public String getOrigEmail() {
		return origEmail;
	}

	public void setOrigEmail(String origEmail) {
		this.origEmail = origEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((origEmail == null) ? 0 : origEmail.hashCode());
		result = prime * result + ((origPassword == null) ? 0 : origPassword.hashCode());
		result = prime * result + ((socialEventHid == null) ? 0 : socialEventHid.hashCode());
		result = prime * result + ((userHid == null) ? 0 : userHid.hashCode());
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
		CreateSocialEventRegistrationModel other = (CreateSocialEventRegistrationModel) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (origEmail == null) {
			if (other.origEmail != null)
				return false;
		} else if (!origEmail.equals(other.origEmail))
			return false;
		if (origPassword == null) {
			if (other.origPassword != null)
				return false;
		} else if (!origPassword.equals(other.origPassword))
			return false;
		if (socialEventHid == null) {
			if (other.socialEventHid != null)
				return false;
		} else if (!socialEventHid.equals(other.socialEventHid))
			return false;
		if (userHid == null) {
			if (other.userHid != null)
				return false;
		} else if (!userHid.equals(other.userHid))
			return false;
		return true;
	}
}
