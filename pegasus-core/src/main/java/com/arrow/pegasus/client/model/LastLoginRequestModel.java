package com.arrow.pegasus.client.model;

import java.io.Serializable;

public class LastLoginRequestModel implements Serializable {
	private static final long serialVersionUID = -2493585099797457458L;

	private String userId;
	private String applicationId;

	public LastLoginRequestModel withUserId(String userId) {
		setUserId(userId);
		return this;
	}

	public LastLoginRequestModel withApplicationId(String applicationId) {
		setApplicationId(applicationId);
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}
