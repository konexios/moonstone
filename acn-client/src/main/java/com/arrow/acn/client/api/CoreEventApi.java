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
import java.util.Collections;
import java.util.Map;

import org.apache.http.client.methods.HttpPut;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.StatusModel;

public class CoreEventApi extends ApiAbstract {
	private static final String CORE_EVENT_BASE_URL = "/api/v1/core/events";
	private static final String PUT_FAILED_URL = CORE_EVENT_BASE_URL + "/{hid}/failed";
	private static final String PUT_RECEIVED_URL = CORE_EVENT_BASE_URL + "/{hid}/received";
	private static final String PUT_SUCCEEDED_URL = CORE_EVENT_BASE_URL + "/{hid}/succeeded";

	CoreEventApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public StatusModel putFailed(String hid, String error) {
		String method = "putFailed";
		try {
			URI uri = buildUri(PUT_FAILED_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpPut(uri), JsonUtils.toJson(Collections.singletonMap("error", error)),
					StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public StatusModel putReceived(String hid) {
		String method = "putReceived";
		try {
			URI uri = buildUri(PUT_RECEIVED_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpPut(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public StatusModel putSucceeded(String hid) {
		return putSucceeded(hid, Collections.emptyMap());
	}

	public StatusModel putSucceeded(String hid, Map<String, String> parameters) {
		String method = "putSucceeded";
		try {
			URI uri = buildUri(PUT_SUCCEEDED_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpPut(uri), JsonUtils.toJson(parameters), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
