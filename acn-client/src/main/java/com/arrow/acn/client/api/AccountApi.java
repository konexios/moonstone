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
package com.arrow.acn.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpPost;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.model.AccountRegistrationModel;
import com.arrow.acn.client.model.AccountRegistrationOK;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;

public class AccountApi extends ApiAbstract {
	private static final String ACCOUNT_BASE_URL = API_BASE + "/accounts";
	private static final String CREATE_OR_LOGIN_URL = ACCOUNT_BASE_URL;

	AccountApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	/**
	 * Sends POST request to create new account or login the existing one based
	 * on {@code model} parameter
	 *
	 * @param model
	 *            {@link AccountRegistrationModel}
	 *
	 * @return {@link AccountRegistrationOK}
	 *
	 * @throws AcnClientException
	 *             if request failed
	 */
	public AccountRegistrationOK createOrLogin(AccountRegistrationModel model) {
		String method = "createOrLogin";
		try {
			URI uri = buildUri(CREATE_OR_LOGIN_URL);
			AccountRegistrationOK result = execute(new HttpPost(uri), JsonUtils.toJson(model),
			        AccountRegistrationOK.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
