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

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.PrivilegeModel;
import com.arrow.acs.client.search.ApplicationSearchCriteria;
import com.fasterxml.jackson.core.type.TypeReference;

public final class PrivilegeApi extends AcsApiAbstract {
	private static final String PRIVS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/privileges";

	// instantiation is expensive for these objects
	private TypeReference<ListResultModel<PrivilegeModel>> privModelTypeRef;

	PrivilegeApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public PrivilegeModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(String.format("%s/%s", PRIVS_ROOT_URL, hid));
			PrivilegeModel result = execute(new HttpGet(uri), PrivilegeModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ListResultModel<PrivilegeModel> findByCriteria(ApplicationSearchCriteria criteria) {
		String method = "findByCriteria";
		try {
			URI uri = buildUri(PRIVS_ROOT_URL, criteria);
			ListResultModel<PrivilegeModel> result = execute(new HttpGet(uri), getPrivModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel createPrivilege(PrivilegeModel model) {
		String method = "createPrivilege";
		try {
			URI uri = buildUri(PRIVS_ROOT_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel updatePrivilege(String hid, PrivilegeModel model) {
		String method = "updatePrivilege";
		try {
			URI uri = buildUri(String.format("%s/%s", PRIVS_ROOT_URL, hid));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	synchronized TypeReference<ListResultModel<PrivilegeModel>> getPrivModelTypeRef() {
		return privModelTypeRef != null ? privModelTypeRef
				: (privModelTypeRef = new TypeReference<ListResultModel<PrivilegeModel>>() {
				});
	}
}
