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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.PasswordModel;
import com.arrow.acs.client.model.StatusMessagesModel;
import com.arrow.acs.client.model.UserAppAuthenticationModel;
import com.arrow.acs.client.model.UserAuthenticationModel;
import com.arrow.acs.client.model.UserModel;
import com.arrow.acs.client.search.UserSearchCriteria;
import com.fasterxml.jackson.core.type.TypeReference;

public final class UserApi extends AcsApiAbstract {
	private static final String USERS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/users";

	// instantiation is expensive for these objects
	private TypeReference<PagingResultModel<UserModel>> userModelTypeRef;

	UserApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public UserModel authenticate(String username, String password) {
		String method = "authenticate";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/auth");
			UserAuthenticationModel model = new UserAuthenticationModel().withUsername(username).withPassword(password);
			UserModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), UserModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public UserModel authenticate2(String username, String password, String applicationCode) {
		String method = "authenticate";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/auth2");
			UserAppAuthenticationModel model = (UserAppAuthenticationModel) new UserAppAuthenticationModel()
					.withUsername(username).withPassword(password);
			model.setApplicationCode(applicationCode);
			UserModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), UserModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public UserModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/" + hid);
			UserModel result = execute(new HttpGet(uri), UserModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public UserModel findByLogin(String username) {
		String method = "findByLogin";
		try {
			URI uri = new URIBuilder(buildUri(USERS_ROOT_URL) + "/logins").addParameter("login", username).build();
			UserModel result = execute(new HttpGet(uri), UserModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public PagingResultModel<UserModel> findByCriteria(UserSearchCriteria criteria) {
		String method = "findByCriteria";
		try {
			URI uri = buildUri(USERS_ROOT_URL, criteria);
			PagingResultModel<UserModel> result = execute(new HttpGet(uri), getUserModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public PasswordModel resetPassword(String hid) {
		String method = "resetPassword";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/" + hid + "/reset-password");
			PasswordModel result = execute(new HttpPost(uri), PasswordModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public StatusMessagesModel setNewPassword(String hid, PasswordModel model) {
		String method = "setNewPassword";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/" + hid + "/set-new-password");
			StatusMessagesModel result = execute(new HttpPost(uri), StatusMessagesModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel createUser(UserModel model) {
		String method = "createUser";
		try {
			URI uri = buildUri(USERS_ROOT_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel updateUser(String hid, UserModel model) {
		String method = "updateUser";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/" + hid);
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	synchronized TypeReference<PagingResultModel<UserModel>> getUserModelTypeRef() {
		return userModelTypeRef != null ? userModelTypeRef
				: (userModelTypeRef = new TypeReference<PagingResultModel<UserModel>>() {
				});
	}
}
