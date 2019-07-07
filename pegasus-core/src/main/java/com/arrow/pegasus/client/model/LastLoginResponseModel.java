package com.arrow.pegasus.client.model;

import java.io.Serializable;
import java.time.Instant;

public class LastLoginResponseModel implements Serializable {
	private static final long serialVersionUID = 859308534795331732L;

	private String userId;
	private Instant lastLogin;

	public LastLoginResponseModel withUserId(String userId) {
		setUserId(userId);
		return this;
	}

	public LastLoginResponseModel withLastLogin(Instant lastLogin) {
		setLastLogin(lastLogin);
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Instant getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Instant lastLogin) {
		this.lastLogin = lastLogin;
	}
}
