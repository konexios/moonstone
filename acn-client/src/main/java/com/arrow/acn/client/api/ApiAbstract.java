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

import com.arrow.acs.client.api.ApiConfig;

public abstract class ApiAbstract extends com.arrow.acs.client.api.ApiAbstract {
	protected static final String API_BASE = "/api/v1/kronos";

	ApiAbstract(ApiConfig apiConfig) {
		super();
		setApiConfig(apiConfig);
	}
}
