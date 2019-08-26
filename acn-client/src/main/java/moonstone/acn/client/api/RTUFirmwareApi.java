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
package moonstone.acn.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.client.model.RTUFirmwareModels.RTUFirmwareModel;
import moonstone.acn.client.model.RTUFirmwareModels.RTURequestedFirmwareModel;
import moonstone.acn.client.search.RTUAvailableSearchCriteria;
import moonstone.acn.client.search.RTURequestSearchCriteria;
import moonstone.acs.client.api.ApiConfig;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.model.StatusModel;

public class RTUFirmwareApi extends ApiAbstract {

	private static final String RTU_BASE_URL = API_BASE + "/rtu";
	private static final String REQUEST_URL = RTU_BASE_URL + "/request/{softwareReleaseHid}";
	private static final String FIND_REQUESTED = RTU_BASE_URL + "/find";
	private static final String FIND_AVAILABLE = FIND_REQUESTED + "/available";

	private TypeReference<List<RTUFirmwareModel>> rtuFirmwareModelTypeRef;
	private TypeReference<PagingResultModel<RTURequestedFirmwareModel>> rtuRequestFirmwareModelTypeRef;

	RTUFirmwareApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public StatusModel requestRTU(String softwareReleaseHid) {
		String method = "requestRTU";

		try {
			URI uri = buildUri(REQUEST_URL.replace("{softwareReleaseHid}", softwareReleaseHid));
			StatusModel result = execute(new HttpPut(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<RTUFirmwareModel> findAvailableFirmware(RTUAvailableSearchCriteria criteria) {
		try {
			URI uri = buildUri(FIND_AVAILABLE, criteria);
			List<RTUFirmwareModel> result = execute(new HttpGet(uri), getRtuFirmwareModelTypeRef());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public PagingResultModel<RTURequestedFirmwareModel> findRequestedFirmware(RTURequestSearchCriteria criteria) {
		try {
			URI uri = buildUri(FIND_REQUESTED, criteria);
			PagingResultModel<RTURequestedFirmwareModel> result = execute(new HttpGet(uri),
					getRtuRequestedFirmwareModel());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<List<RTUFirmwareModel>> getRtuFirmwareModelTypeRef() {
		return rtuFirmwareModelTypeRef != null ? rtuFirmwareModelTypeRef
				: (rtuFirmwareModelTypeRef = new TypeReference<List<RTUFirmwareModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<RTURequestedFirmwareModel>> getRtuRequestedFirmwareModel() {
		return rtuRequestFirmwareModelTypeRef != null ? rtuRequestFirmwareModelTypeRef
				: (rtuRequestFirmwareModelTypeRef = new TypeReference<PagingResultModel<RTURequestedFirmwareModel>>() {
				});
	}
}
