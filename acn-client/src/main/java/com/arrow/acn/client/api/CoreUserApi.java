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
import java.util.List;

import org.apache.http.client.methods.HttpPost;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.SamlAccountModel;
import com.arrow.acs.client.model.StatusModel;

public class CoreUserApi extends ApiAbstract {
	private static final String CORE_USERS_BASE_URL = API_BASE + "/core/users";
	private static final String SYNC_SAML_ACCOUNTS_URL = CORE_USERS_BASE_URL + "/sync-saml-accounts";

	CoreUserApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public StatusModel syncSamlAccounts(List<SamlAccountModel> samlAccounts) {
		String method = "syncSamlAccounts";
		try {
			URI uri = buildUri(SYNC_SAML_ACCOUNTS_URL);
			StatusModel result = execute(new HttpPost(uri), JsonUtils.toJson(samlAccounts), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
