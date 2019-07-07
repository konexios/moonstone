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

public class UserAppAuthenticationModel extends UserAuthenticationModel {

	private static final long serialVersionUID = -5006638956859437797L;

	private String applicationCode;

	public UserAppAuthenticationModel withApplicationCode(String applicationCode) {
		setApplicationCode(applicationCode);
		return this;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((applicationCode == null) ? 0 : applicationCode.hashCode());
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
		UserAppAuthenticationModel other = (UserAppAuthenticationModel) obj;
		if (applicationCode == null) {
			if (other.applicationCode != null)
				return false;
		} else if (!applicationCode.equals(other.applicationCode))
			return false;
		return true;
	}
}
