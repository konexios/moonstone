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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.arrow.acn.client.model.CreateSoftwareReleaseScheduleModel;
import com.arrow.acn.client.model.SoftwareReleaseScheduleAutomationModel;
import com.arrow.acn.client.model.SoftwareReleaseScheduleModel;
import com.arrow.acn.client.model.SoftwareReleaseTransModel;
import com.arrow.acn.client.model.UpdateSoftwareReleaseScheduleModel;
import com.arrow.acn.client.search.SoftwareReleaseScheduleSearchCriteria;
import com.arrow.acn.client.search.SoftwareReleaseScheduleTransactionsListSearchCriteria;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.fasterxml.jackson.core.type.TypeReference;

public class SoftwareReleaseScheduleApi extends ApiAbstract {

	private static final String SOFTWARE_RELEASE_SCHEDULE_BASE_URL = API_BASE + "/software/releases/schedules";
	private static final String CREATE_URL = SOFTWARE_RELEASE_SCHEDULE_BASE_URL;
	private static final String SPECIFIC_SOFTWARE_RELEASE_URL = SOFTWARE_RELEASE_SCHEDULE_BASE_URL + "/%s";
	private static final String UPDATE_URL = SPECIFIC_SOFTWARE_RELEASE_URL;
	private static final String FIND_BY_HID = SPECIFIC_SOFTWARE_RELEASE_URL;
	private static final String LIST_ALL_TRANSACTIONS = SPECIFIC_SOFTWARE_RELEASE_URL + "/transactions";
	private static final String FIND_ALL_BY_URL = SOFTWARE_RELEASE_SCHEDULE_BASE_URL;
	private static final String CREATE_AND_START_URL = SOFTWARE_RELEASE_SCHEDULE_BASE_URL + "/start";

	private TypeReference<PagingResultModel<SoftwareReleaseScheduleModel>> softwareReleaseScheduleModelTypeRef;
	private TypeReference<PagingResultModel<SoftwareReleaseTransModel>> softwareReleaseTransModelTypeRef;

	SoftwareReleaseScheduleApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public HidModel create(CreateSoftwareReleaseScheduleModel model) {
		String method = "create";
		try {
			URI uri = buildUri(CREATE_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public HidModel createAndStart(SoftwareReleaseScheduleAutomationModel model) {
		String method = "createAndStart";
		try {
			URI uri = buildUri(CREATE_AND_START_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public HidModel update(String hid, UpdateSoftwareReleaseScheduleModel model) {
		String method = "update";
		try {
			URI uri = buildUri(String.format(UPDATE_URL, hid));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareReleaseScheduleModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(String.format(FIND_BY_HID, hid));
			SoftwareReleaseScheduleModel result = execute(new HttpGet(uri), SoftwareReleaseScheduleModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public PagingResultModel<SoftwareReleaseScheduleModel> findAllBy(SoftwareReleaseScheduleSearchCriteria criteria) {
		String method = "findAllBy";
		try {
			URI uri = buildUri(FIND_ALL_BY_URL, criteria);
			PagingResultModel<SoftwareReleaseScheduleModel> result = execute(new HttpGet(uri),
					getSoftwareReleaseScheduleModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public PagingResultModel<SoftwareReleaseTransModel> listTransactions(String hid,
			SoftwareReleaseScheduleTransactionsListSearchCriteria criteria) {
		String method = "listTransactions";
		try {
			URI uri = buildUri(String.format(LIST_ALL_TRANSACTIONS, hid), criteria);
			PagingResultModel<SoftwareReleaseTransModel> result = execute(new HttpGet(uri),
					getSoftwareReleaseTransModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<PagingResultModel<SoftwareReleaseScheduleModel>> getSoftwareReleaseScheduleModelTypeRef() {
		return softwareReleaseScheduleModelTypeRef != null ? softwareReleaseScheduleModelTypeRef
				: (softwareReleaseScheduleModelTypeRef = new TypeReference<PagingResultModel<SoftwareReleaseScheduleModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<SoftwareReleaseTransModel>> getSoftwareReleaseTransModelTypeRef() {
		return softwareReleaseTransModelTypeRef != null ? softwareReleaseTransModelTypeRef
				: (softwareReleaseTransModelTypeRef = new TypeReference<PagingResultModel<SoftwareReleaseTransModel>>() {
				});
	}
}
