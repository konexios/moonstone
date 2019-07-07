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

import java.net.URI;

import org.apache.http.client.methods.HttpGet;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.AccessKeyModel;
import com.arrow.acs.client.model.ApplicationModel;
import com.arrow.acs.client.model.AuthModel;
import com.arrow.acs.client.model.CompanyModel;
import com.arrow.acs.client.model.PrivilegeModel;
import com.arrow.acs.client.model.ProductModel;
import com.arrow.acs.client.model.RegionModel;
import com.arrow.acs.client.model.RoleModel;
import com.arrow.acs.client.model.SubscriptionModel;
import com.arrow.acs.client.model.UserModel;
import com.arrow.acs.client.model.ZoneModel;

public final class CacheApi extends AcsApiAbstract {
	private static final String CACHE_ROOT_URL = WEB_SERVICE_ROOT_URL + "/cache";

	CacheApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public UserModel findUserByHid(String hid) {
		String method = "findUserByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/users/hids/" + hid);
			UserModel result = execute(new HttpGet(uri), UserModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ApplicationModel findApplicationByHid(String hid) {
		String method = "findApplicationByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/applications/hids/" + hid);
			ApplicationModel result = execute(new HttpGet(uri), ApplicationModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ApplicationModel findApplicationByName(String name) {
		String method = "findApplicationByName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/applications/names/" + name);
			ApplicationModel result = execute(new HttpGet(uri), ApplicationModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public CompanyModel findCompanyByHid(String hid) {
		String method = "findCompanyByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/companies/hids/" + hid);
			CompanyModel result = execute(new HttpGet(uri), CompanyModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ProductModel findProductByHid(String hid) {
		String method = "findProductByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/products/hids/" + hid);
			ProductModel result = execute(new HttpGet(uri), ProductModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ProductModel findProductBySystemName(String systemName) {
		String method = "findProductBySystemName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/products/system-names/" + systemName);
			ProductModel result = execute(new HttpGet(uri), ProductModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SubscriptionModel findSubscriptionByHid(String hid) {
		String method = "findSubscriptionByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/subscriptions/hids/" + hid);
			SubscriptionModel result = execute(new HttpGet(uri), SubscriptionModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "hid: %s, name: %s", result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public AccessKeyModel findAccessKeyByHashedApiKey(String hashedApiKey) {
		String method = "findAccessKeyByHashedApiKey";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/access-keys/hashed-api-keys/" + hashedApiKey);
			AccessKeyModel result = execute(new HttpGet(uri), AccessKeyModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RoleModel findRoleByHid(String hid) {
		String method = "findRoleByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/roles/hids/" + hid);
			RoleModel result = execute(new HttpGet(uri), RoleModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public PrivilegeModel findPrivilegeByHid(String hid) {
		String method = "findPrivilegeByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/privileges/hids/" + hid);
			PrivilegeModel result = execute(new HttpGet(uri), PrivilegeModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ZoneModel findZoneByHid(String hid) {
		String method = "findZoneByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/zones/hids/" + hid);
			ZoneModel result = execute(new HttpGet(uri), ZoneModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ZoneModel findZoneBySystemName(String systemName) {
		String method = "findZoneBySystemName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/zones/system-names/" + systemName);
			ZoneModel result = execute(new HttpGet(uri), ZoneModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RegionModel findRegionByHid(String hid) {
		String method = "findRegionByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/regions/hids/" + hid);
			RegionModel result = execute(new HttpGet(uri), RegionModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RegionModel findRegionByName(String name) {
		String method = "findRegionByName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/regions/names/" + name);
			RegionModel result = execute(new HttpGet(uri), RegionModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public AuthModel findAuthByHid(String hid) {
		String method = "findAuthByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/auths/hids/" + hid);
			AuthModel result = execute(new HttpGet(uri), AuthModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public AuthModel findAuthBySamlIdp(String samlIdp) {
		String method = "findAuthBySamlIdp";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/auths/saml-idps/" + samlIdp);
			AuthModel result = execute(new HttpGet(uri), AuthModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
