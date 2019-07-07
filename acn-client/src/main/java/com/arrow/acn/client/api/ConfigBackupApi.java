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

import com.arrow.acn.client.model.ConfigBackupModel;
import com.arrow.acs.client.api.ApiConfig;

public class ConfigBackupApi extends ApiAbstract {

	private static final String CONFIG_BACKUPS_BASE_URL = API_BASE + "/config-backups";
	private static final String SPECIFIC_CONFIG_BACKUP_URL = CONFIG_BACKUPS_BASE_URL + "/{hid}";

	ConfigBackupApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public ConfigBackupModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(SPECIFIC_CONFIG_BACKUP_URL.replace("{hid}", hid));
			ConfigBackupModel result = execute(new HttpGet(uri), ConfigBackupModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
