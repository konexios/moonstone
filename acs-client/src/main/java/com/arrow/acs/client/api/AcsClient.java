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
package com.arrow.acs.client.api;

import com.arrow.acs.AcsUtils;

public final class AcsClient {
	private ApiConfig apiConfig;
	private ApplicationApi applicationApi;
	private CacheApi cacheApi;
	private CompanyApi companyApi;
	private PrivilegeApi privilegeApi;
	private ProductApi productApi;
	private RoleApi roleApi;
	private SubscriptionApi subscriptionApi;
	private UserApi userApi;

	public AcsClient(ApiConfig apiConfig) {
		setApiConfig(apiConfig);
	}

	public void setApiConfig(ApiConfig apiConfig) {
		AcsUtils.notNull(apiConfig, "apiConfig is not set");
		this.apiConfig = apiConfig;
		if (applicationApi != null)
			applicationApi.setApiConfig(apiConfig);
		if (cacheApi != null)
			cacheApi.setApiConfig(apiConfig);
		if (companyApi != null)
			companyApi.setApiConfig(apiConfig);
		if (privilegeApi != null)
			privilegeApi.setApiConfig(apiConfig);
		if (productApi != null)
			productApi.setApiConfig(apiConfig);
		if (roleApi != null)
			roleApi = new RoleApi(apiConfig);
		if (subscriptionApi != null)
			subscriptionApi = new SubscriptionApi(apiConfig);
		if (userApi != null)
			userApi = new UserApi(apiConfig);
	}

	public ApiConfig getApiConfig() {
		return apiConfig;
	}

	public synchronized ApplicationApi getApplicationApi() {
		return applicationApi != null ? applicationApi : (applicationApi = new ApplicationApi(apiConfig));
	}

	public synchronized CacheApi getCacheApi() {
		return cacheApi != null ? cacheApi : (cacheApi = new CacheApi(apiConfig));
	}

	public synchronized CompanyApi getCompanyApi() {
		return companyApi != null ? companyApi : (companyApi = new CompanyApi(apiConfig));
	}

	public synchronized PrivilegeApi getPrivilegeApi() {
		return privilegeApi != null ? privilegeApi : (privilegeApi = new PrivilegeApi(apiConfig));
	}

	public synchronized ProductApi getProductApi() {
		return productApi != null ? productApi : (productApi = new ProductApi(apiConfig));
	}

	public synchronized RoleApi getRoleApi() {
		return productApi != null ? roleApi : (roleApi = new RoleApi(apiConfig));
	}

	public synchronized SubscriptionApi getSubscriptionApi() {
		return subscriptionApi != null ? subscriptionApi : (subscriptionApi = new SubscriptionApi(apiConfig));
	}

	public synchronized UserApi getUserApi() {
		return userApi != null ? userApi : (userApi = new UserApi(apiConfig));
	}

}
