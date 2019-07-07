package com.arrow.dashboard.runtime.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class UserWrapper implements Serializable {
	private static final long serialVersionUID = 6110978714126071026L;

	private UserRuntimeInstance userRuntimeInstance;

	public UserWrapper(UserRuntimeInstance userRuntimeInstance) {
		this.userRuntimeInstance = userRuntimeInstance;
	}

	public String getLogin() {
		return userRuntimeInstance.getLogin();
	}

	public String getUserId() {
		return userRuntimeInstance.getUserId();
	}

	public String getApplicationId() {
		return userRuntimeInstance.getApplicationId();
	}

	public String getCompanyId() {
		return userRuntimeInstance.getCompanyId();
	}

	private List<String> getUserAuthorities() {
		return userRuntimeInstance.getUserAuthorities();
	}

	public boolean hasPrivilege(String privilege) {
		boolean result = false;

		if (StringUtils.isEmpty(privilege))
			return result;

		for (String authority : getUserAuthorities())
			if (authority.equals(privilege)) {
				result = true;
				break;
			}

		return result;
	}
}