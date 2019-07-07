package com.arrow.dashboard.runtime.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserRuntimeInstance implements Serializable {
	private static final long serialVersionUID = -4142992085533320302L;

	private String userId;
	private String login;
	private String applicationId;
	private String companyId;
	private List<String> userAuthorities = new ArrayList<String>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<String> getUserAuthorities() {
		return userAuthorities;
	}

	public void setUserAuthorities(List<String> userAuthorities) {
		this.userAuthorities = userAuthorities;
	}

	public UserRuntimeInstance withUserId(String userId) {
		setUserId(userId);

		return this;
	}

	public UserRuntimeInstance withLogin(String login) {
		setLogin(login);

		return this;
	}

	public UserRuntimeInstance withApplicationId(String applicationId) {
		setApplicationId(applicationId);

		return this;
	}

	public UserRuntimeInstance withCompanyId(String companyId) {
		setCompanyId(companyId);

		return this;
	}

	public UserRuntimeInstance withUserAuthorities(List<String> userAuthorities) {
		setUserAuthorities(userAuthorities);

		return this;
	}
}