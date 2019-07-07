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

import org.apache.http.client.methods.HttpGet;

import com.arrow.acn.client.model.TelemetryUnitModel;
import com.arrow.acn.client.search.TelemetryUnitSearchCriteria;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.ListResultModel;
import com.fasterxml.jackson.core.type.TypeReference;

public class TelemetryUnitApi extends ApiAbstract {

	private static final String TELEMETRY_UNIT_BASE_URL = API_BASE + "/telemetries/units";

	private TypeReference<ListResultModel<TelemetryUnitModel>> telemetryUnitModelTypeRef;

	TelemetryUnitApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public ListResultModel<TelemetryUnitModel> findAllBy(TelemetryUnitSearchCriteria criteria) {
		String method = "findAllBy";
		try {
			URI uri = buildUri(TELEMETRY_UNIT_BASE_URL, criteria);
			ListResultModel<TelemetryUnitModel> result = execute(new HttpGet(uri), getTelemetryUnitModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<ListResultModel<TelemetryUnitModel>> getTelemetryUnitModelTypeRef() {
		return telemetryUnitModelTypeRef != null ? telemetryUnitModelTypeRef
				: (telemetryUnitModelTypeRef = new TypeReference<ListResultModel<TelemetryUnitModel>>() {
				});
	}
}
