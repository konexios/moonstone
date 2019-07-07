package com.arrow.pegasus.data;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class LastLogin implements Serializable {
	private static final long serialVersionUID = -20653834416009459L;

	@Id
	private String userId;
	@Field
	private Instant lastLogin;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String name) {
		this.userId = name;
	}

	public Instant getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Instant value) {
		this.lastLogin = value;
	}

	public LastLogin withUserId(String userId) {
		setUserId(userId);
		return this;
	}

	public LastLogin withLastLogin(Instant lastLogin) {
		setLastLogin(lastLogin);
		return this;
	}
}