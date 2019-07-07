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

public class LoginPolicyModel implements Serializable {
	private static final long serialVersionUID = 5249540968674733320L;

	private int maxFailedLogins;
	private int lockTimeoutSecs;

	public LoginPolicyModel withMaxFailedLogins(int maxFailedLogins) {
		setMaxFailedLogins(maxFailedLogins);
		return this;
	}

	public LoginPolicyModel withLockTimeoutSecs(int lockTimeoutSecs) {
		setLockTimeoutSecs(lockTimeoutSecs);
		return this;
	}

	public int getMaxFailedLogins() {
		return maxFailedLogins;
	}

	public void setMaxFailedLogins(int maxFailedLogins) {
		this.maxFailedLogins = maxFailedLogins;
	}

	public int getLockTimeoutSecs() {
		return lockTimeoutSecs;
	}

	public void setLockTimeoutSecs(int lockTimeoutSecs) {
		this.lockTimeoutSecs = lockTimeoutSecs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lockTimeoutSecs;
		result = prime * result + maxFailedLogins;
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
		LoginPolicyModel other = (LoginPolicyModel) obj;
		if (lockTimeoutSecs != other.lockTimeoutSecs)
			return false;
		if (maxFailedLogins != other.maxFailedLogins)
			return false;
		return true;
	}
}
