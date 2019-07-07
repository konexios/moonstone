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

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemModel;
import com.arrow.acn.client.model.TelemetryStatModel;
import com.arrow.acn.client.search.TelemetryCountSearchCriteria;
import com.arrow.acn.client.search.TelemetryDeleteSearchCriteria;
import com.arrow.acn.client.search.TelemetrySearchCriteria;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public final class TelemetryApi extends ApiAbstract {
	private final String TELEMETRY_BASE_URL = API_BASE + "/telemetries";
	private final String FIND_BY_APPLICATION_HID = TELEMETRY_BASE_URL + "/applications/{applicationHid}";
	private final String FIND_BY_DEVICE_HID = TELEMETRY_BASE_URL + "/devices/{deviceHid}";
	private final String FIND_LATEST_BY_DEVICE_HID = FIND_BY_DEVICE_HID + "/latest";
	private final String FIND_BY_NODE_HID = TELEMETRY_BASE_URL + "/nodes/{nodeHid}";
	private final String CREATE_URL = TELEMETRY_BASE_URL;
	private final String BATCH_CREATE_URL = CREATE_URL + "/batch";
	private final String COUNT_BY_DEVICE_HID = FIND_BY_DEVICE_HID + "/count";
	private final String BULK_DELETE_LAST_TELEMETRY = TELEMETRY_BASE_URL
			+ "/devices/{deviceHid}/bulkDeleteLastTelemetry";

	private TypeReference<PagingResultModel<TelemetryItemModel>> pagingResultTypeRef;
	private TypeReference<ListResultModel<TelemetryItemModel>> resultTypeRef;

	TelemetryApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	/**
	 * Sends POST request to store telemetry
	 *
	 * @param parameters {@link IotParameters} representing bunch of telemetry
	 *                   received from a device
	 *
	 * @return {@link StatusModel} containing status of telemetry storing request
	 *
	 * @throws AcnClientException if request failed
	 */
	public StatusModel create(IotParameters parameters) {
		String method = "create";
		try {
			URI uri = buildUri(CREATE_URL);
			StatusModel result = execute(new HttpPost(uri), JsonUtils.toJson(parameters), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain telemetry by specific {@code applicationHid} and
	 * corresponding to {@code criteria}
	 *
	 * @param applicationHid {@link String} representing specific application Hid
	 * @param criteria       {@link TelemetrySearchCriteria} representing search
	 *                       filter parameters
	 *
	 * @return subset of {@link TelemetryItemModel} containing telemetry parameters.
	 *         <b>Note:</b> resulting subset may contain not all telemetry
	 *         corresponding to search parameters because it cannot exceed page size
	 *         passed in {@code criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<TelemetryItemModel> findByApplicationHid(String applicationHid,
			TelemetrySearchCriteria criteria) {
		String method = "findByApplicationHid";
		try {
			URI uri = buildUri(FIND_BY_APPLICATION_HID.replace("{applicationHid}", applicationHid), criteria);
			PagingResultModel<TelemetryItemModel> result = execute(new HttpGet(uri), criteria,
					getPagingResultTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain telemetry by specific {@code deviceHid}
	 *
	 * @param deviceHid {@link String} representing specific device Hid
	 *
	 * @return list of {@link TelemetryItemModel} containing telemetry parameters.
	 *
	 * @throws AcnClientException if request failed
	 */
	public ListResultModel<TelemetryItemModel> getLatestTelemetry(String deviceHid) {
		String method = "getLatestTelemetry";
		try {
			URI uri = buildUri(FIND_LATEST_BY_DEVICE_HID.replace("{deviceHid}", deviceHid));
			ListResultModel<TelemetryItemModel> result = execute(new HttpGet(uri), getResultTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain telemetry of specific device and corresponding to
	 * {@code criteria}
	 *
	 * @param deviceHid {@link String} representing specific device
	 * @param criteria  {@link TelemetrySearchCriteria} representing search filter
	 *                  parameters
	 *
	 * @return subset of {@link TelemetryItemModel} containing telemetry parameters.
	 *         <b>Note:</b> resulting subset may contain not all telemetry
	 *         corresponding to search parameters because it cannot exceed page size
	 *         passed in {@code criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<TelemetryItemModel> findByDeviceHid(String deviceHid, TelemetrySearchCriteria criteria) {
		String method = "findByDeviceHid";
		try {
			URI uri = buildUri(FIND_BY_DEVICE_HID.replace("{deviceHid}", deviceHid), criteria);
			PagingResultModel<TelemetryItemModel> result = execute(new HttpGet(uri), criteria,
					getPagingResultTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain telemetry by specific {@code nodeHid} and
	 * corresponding to {@code criteria}
	 *
	 * @param nodeHid  {@link String} representing specific node Hid
	 * @param criteria {@link TelemetrySearchCriteria} representing search filter
	 *                 parameters
	 *
	 * @return subset of {@link TelemetryItemModel} containing telemetry parameters.
	 *         <b>Note:</b> resulting subset may contain not all telemetry
	 *         corresponding to search parameters because it cannot exceed page size
	 *         passed in {@code criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<TelemetryItemModel> findByNodeHid(String nodeHid, TelemetrySearchCriteria criteria) {
		String method = "findByNodeHid";
		try {
			URI uri = buildUri(FIND_BY_NODE_HID.replace("{nodeHid}", nodeHid), criteria);
			PagingResultModel<TelemetryItemModel> result = execute(new HttpGet(uri), criteria,
					getPagingResultTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to store set of telemetry
	 *
	 * @param parameters {@link IotParameters} representing set of telemetry bunches
	 *                   received from devices
	 *
	 * @return {@link StatusModel} containing status of batch telemetry creation
	 *         request
	 *
	 * @throws AcnClientException if request failed
	 */
	public StatusModel batchCreate(List<IotParameters> parameters) {
		String method = "batchCreate";
		try {
			URI uri = buildUri(BATCH_CREATE_URL);
			StatusModel result = execute(new HttpPost(uri), JsonUtils.toJson(parameters), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain telemetry count of specific device and
	 * corresponding to {@code criteria}
	 *
	 * @param deviceHid {@link String} representing specific device
	 * @param criteria  {@link TelemetryCountSearchCriteria} representing search
	 *                  filter parameters
	 *
	 * @return {@link TelemetryStatModel} containing telemetry count
	 *
	 * @throws AcnClientException if request failed
	 */
	public TelemetryStatModel countByDeviceHid(String deviceHid, TelemetryCountSearchCriteria criteria) {
		String method = "countByDeviceHid";
		try {
			URI uri = buildUri(COUNT_BY_DEVICE_HID.replace("{deviceHid}", deviceHid), criteria);
			TelemetryStatModel result = execute(new HttpGet(uri), criteria, TelemetryStatModel.class);
			logDebug(method, "device hid: %s", result.getDeviceHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Send DELETE request to remove set of last telemetry items
	 *
	 * @param lastTelemetryItemsIds     {@link List<String>} ids of last telemetry
	 *                                  items
	 *
	 * @param deviceHid                 {@link String} representing specific device
	 *
	 * @param removeTelemetryDefinition {@link boolean} true - if needs remove
	 *                                  telemetry definition if exists
	 *
	 * @return {@link StatusModel} containing status of batch telemetry delete
	 *         request
	 */
	public StatusModel bulkDeleteLastTelemetries(List<String> lastTelemetryItemsIds, String deviceHid,
			boolean removeTelemetryDefinition) {

		String method = "bulkDeleteLastTelemetries";

		TelemetryDeleteSearchCriteria criteria = new TelemetryDeleteSearchCriteria();
		criteria.withRemoveDefinitions(removeTelemetryDefinition);

		try {
			URI uri = buildUri(BULK_DELETE_LAST_TELEMETRY.replace("{deviceHid}", deviceHid), criteria);
			StatusModel result = execute(new HttpPut(uri), JsonUtils.toJson(lastTelemetryItemsIds), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<PagingResultModel<TelemetryItemModel>> getPagingResultTypeRef() {
		return pagingResultTypeRef != null ? pagingResultTypeRef
				: (pagingResultTypeRef = new TypeReference<PagingResultModel<TelemetryItemModel>>() {
				});
	}

	private synchronized TypeReference<ListResultModel<TelemetryItemModel>> getResultTypeRef() {
		return resultTypeRef != null ? resultTypeRef
				: (resultTypeRef = new TypeReference<ListResultModel<TelemetryItemModel>>() {
				});
	}
}
