package com.arrow.pegasus.web.model;

import java.io.Serializable;

public class LoginPolicyModel implements Serializable {
	private static final long serialVersionUID = -8165095241355450047L;

	private int lockTimeoutSecs;
	private int maxFailedLogins;

	public LoginPolicyModel() {
	}

	public LoginPolicyModel withLockTimeoutSecs(int lockTimeoutSecs) {
		setLockTimeoutSecs(lockTimeoutSecs);
		return this;
	}

	public LoginPolicyModel withMaxFailedLogins(int maxFailedLogins) {
		setMaxFailedLogins(maxFailedLogins);
		return this;
	}

	public int getLockTimeoutSecs() {
		return lockTimeoutSecs;
	}

	public void setLockTimeoutSecs(int lockTimeoutSecs) {
		this.lockTimeoutSecs = lockTimeoutSecs;
	}

	public int getMaxFailedLogins() {
		return maxFailedLogins;
	}

	public void setMaxFailedLogins(int maxFailedLogins) {
		this.maxFailedLogins = maxFailedLogins;
	}

}
