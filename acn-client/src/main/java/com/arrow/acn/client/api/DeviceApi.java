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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.model.AuditLogModel;
import com.arrow.acn.client.model.AvailableFirmwareModel;
import com.arrow.acn.client.model.ConfigBackupModel;
import com.arrow.acn.client.model.CreateConfigBackupModel;
import com.arrow.acn.client.model.DeviceEventModel;
import com.arrow.acn.client.model.DeviceModel;
import com.arrow.acn.client.model.DeviceRegistrationModel;
import com.arrow.acn.client.search.DeviceSearchCriteria;
import com.arrow.acn.client.search.EventsSearchCriteria;
import com.arrow.acn.client.search.LogsSearchCriteria;
import com.arrow.acn.client.search.SortedSearchCriteria;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.ErrorModel;
import com.arrow.acs.client.model.ExternalHidModel;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public class DeviceApi extends ApiAbstract {
	private static final String DEVICES_BASE_URL = API_BASE + "/devices";
	private static final String CREATE_OR_UPDATE_URL = DEVICES_BASE_URL;
	private static final String FIND_ALL_BY_URL = DEVICES_BASE_URL;
	private static final String SPECIFIC_DEVICE_URL = DEVICES_BASE_URL + "/{hid}";
	private static final String FIND_BY_HID_URL = SPECIFIC_DEVICE_URL;
	private static final String UPDATE_EXISTING_URL = SPECIFIC_DEVICE_URL;
	private static final String SPECIFIC_EVENTS_URL = SPECIFIC_DEVICE_URL + "/events";
	private static final String SPECIFIC_LOGS_URL = SPECIFIC_DEVICE_URL + "/logs";
	private static final String SEND_ERROR_URL = SPECIFIC_DEVICE_URL + "/errors";
	private static final String CONFIGURATION_BACKUP_URL = SPECIFIC_DEVICE_URL + "/config-backups";
	private static final String AVAILABLE_FIRMWARE = SPECIFIC_DEVICE_URL + "/firmware/available";
	private static final String SPECIFIC_CONFIGURATION_BACKUP_URL = CONFIGURATION_BACKUP_URL + "/{configBackupHid}";
	private static final String RESTORE_CONFIGURATION_URL = SPECIFIC_CONFIGURATION_BACKUP_URL + "/restore";

	private static final Pattern PATTERN = Pattern.compile("{hid}", Pattern.LITERAL);

	// instantiation is expensive for these objects
	private TypeReference<PagingResultModel<DeviceModel>> deviceModelTypeRef;
	private TypeReference<PagingResultModel<DeviceEventModel>> deviceEventModelTypeRef;
	private TypeReference<PagingResultModel<AuditLogModel>> auditLogModelTypeRef;
	private TypeReference<PagingResultModel<ConfigBackupModel>> configBackupModelTypeRef;
	private TypeReference<List<AvailableFirmwareModel>> rtuAvailableFirmwareModelTypeRef;

	DeviceApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	/**
	 * Sends GET request to obtain parameters of devices corresponding
	 * {@code criteria}
	 *
	 * @param criteria {@link DeviceSearchCriteria} representing search filter
	 *                 parameters
	 *
	 * @return subset of {@link DeviceModel} containing device parameters.
	 *         <b>Note:</b> resulting subset may contain not all devices
	 *         corresponding to search parameters because it cannot exceed page size
	 *         passed in {@code criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<DeviceModel> findAllBy(DeviceSearchCriteria criteria) {
		String method = "findAllBy";
		try {
			URI uri = buildUri(FIND_ALL_BY_URL, criteria);
			PagingResultModel<DeviceModel> result = execute(new HttpGet(uri), getDeviceModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to create new or update existing device according to
	 * {@code model} passed. <b>Note:</b> device uniqueness is defined by
	 * {@code uid} and {@code applicationId} passed in {@code model}
	 *
	 * @param model {@link DeviceRegistrationModel} representing parameters of
	 *              device to be created/updated
	 *
	 * @return {@link ExternalHidModel} containing external {@code hid} of device
	 *         created/updated
	 *
	 * @throws AcnClientException if request failed
	 */
	public ExternalHidModel createOrUpdate(DeviceRegistrationModel model) {
		String method = "createOrUpdate";
		try {
			URI uri = buildUri(CREATE_OR_UPDATE_URL);
			ExternalHidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), ExternalHidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain parameters of device specified by its {@code hid}
	 *
	 * @param hid {@link String} representing specific device
	 *
	 * @return {@link DeviceModel} containing device parameters
	 *
	 * @throws AcnClientException if request failed
	 */
	public DeviceModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(PATTERN.matcher(FIND_BY_HID_URL).replaceAll(Matcher.quoteReplacement(hid)));
			DeviceModel result = execute(new HttpGet(uri), DeviceModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends PUT request to update specific existing device according to
	 * {@code model} passed
	 *
	 * @param hid   {@link String} representing {@code hid} of device to be updated
	 * @param model {@link DeviceRegistrationModel} representing device parameters
	 *              to be updated
	 *
	 * @return {@link HidModel} containing {@code hid} of device updated
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel updateExistingDevice(String hid, DeviceRegistrationModel model) {
		String method = "updateExistingDevice";
		try {
			URI uri = buildUri(PATTERN.matcher(UPDATE_EXISTING_URL).replaceAll(Matcher.quoteReplacement(hid)));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain events related to specific device and
	 * corresponding {@code criteria}
	 *
	 * @param hid      {@link String} representing device {@code hid}
	 * @param criteria {@link EventsSearchCriteria} representing search filter
	 *                 parameters.
	 *
	 * @return subset of {@link DeviceEventModel} containing event parameters.
	 *         <b>Note:</b> resulting subset may contain not all events
	 *         corresponding to search parameters because it cannot exceed page size
	 *         passed in{@code
	 * criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<DeviceEventModel> listHistoricalDeviceEvents(String hid, EventsSearchCriteria criteria) {
		String method = "listHistoricalDeviceEvents";
		try {
			URI uri = buildUri(PATTERN.matcher(SPECIFIC_EVENTS_URL).replaceAll(Matcher.quoteReplacement(hid)),
					criteria);
			PagingResultModel<DeviceEventModel> result = execute(new HttpGet(uri), criteria,
					getDeviceEventModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain logs related to specific device and corresponding
	 * {@code criteria}
	 *
	 * @param hid      {@link String} representing device {@code hid}
	 * @param criteria {@link LogsSearchCriteria} representing search filter
	 *                 parameters.
	 *
	 * @return subset of {@link AuditLogModel} containing event parameters.
	 *         <b>Note:</b> resulting subset may contain not all logs corresponding
	 *         to search parameters because it cannot exceed page size passed in
	 *         {@code criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<AuditLogModel> listDeviceAuditLogs(String hid, LogsSearchCriteria criteria) {
		String method = "listDeviceAuditLogs";
		try {
			URI uri = buildUri(PATTERN.matcher(SPECIFIC_LOGS_URL).replaceAll(Matcher.quoteReplacement(hid)), criteria);
			PagingResultModel<AuditLogModel> result = execute(new HttpGet(uri), criteria, getAuditLogModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to submit an error
	 *
	 * @param model {@link ErrorModel} content of the error (code and message)
	 *
	 * @return {@link HidModel} containing external {@code hid} of the created audit
	 *         log
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel sendError(String hid, ErrorModel model) {
		String method = "sendError";
		try {
			URI uri = buildUri(SEND_ERROR_URL.replace("{hid}", hid));
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public HidModel backupConfiguration(String hid, CreateConfigBackupModel model) {
		String method = "backupConfiguration";
		try {
			URI uri = buildUri(CONFIGURATION_BACKUP_URL.replace("{hid}", hid));
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public StatusModel restoreConfiguration(String deviceHid, String configBackupHid) {
		String method = "restoreConfiguration";
		try {
			URI uri = buildUri(RESTORE_CONFIGURATION_URL.replace("{hid}", deviceHid).replace("{configBackupHid}",
					configBackupHid));
			StatusModel result = execute(new HttpPost(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public PagingResultModel<ConfigBackupModel> listConfigurationBackups(String hid, SortedSearchCriteria criteria) {
		String method = "listConfigurationBackups";
		try {
			URI uri = buildUri(CONFIGURATION_BACKUP_URL.replace("{hid}", hid), criteria);
			PagingResultModel<ConfigBackupModel> result = execute(new HttpGet(uri), criteria,
					getConfigBackupModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<AvailableFirmwareModel> availableFirmware(String hid) {
		try {
			URI uri = buildUri(AVAILABLE_FIRMWARE.replace("{hid}", hid));
			List<AvailableFirmwareModel> result = execute(new HttpGet(uri), getAvailableFirmwareModelTypeRef());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public StatusModel deleteDevice(String hid) {
		String method = "deleteDevice";
		try {
			URI uri = buildUri(SPECIFIC_DEVICE_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpDelete(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<PagingResultModel<DeviceModel>> getDeviceModelTypeRef() {
		return deviceModelTypeRef != null ? deviceModelTypeRef
				: (deviceModelTypeRef = new TypeReference<PagingResultModel<DeviceModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<DeviceEventModel>> getDeviceEventModelTypeRef() {
		return deviceEventModelTypeRef != null ? deviceEventModelTypeRef
				: (deviceEventModelTypeRef = new TypeReference<PagingResultModel<DeviceEventModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<AuditLogModel>> getAuditLogModelTypeRef() {
		return auditLogModelTypeRef != null ? auditLogModelTypeRef
				: (auditLogModelTypeRef = new TypeReference<PagingResultModel<AuditLogModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<ConfigBackupModel>> getConfigBackupModelTypeRef() {
		return configBackupModelTypeRef != null ? configBackupModelTypeRef
				: (configBackupModelTypeRef = new TypeReference<PagingResultModel<ConfigBackupModel>>() {
				});
	}

	private synchronized TypeReference<List<AvailableFirmwareModel>> getAvailableFirmwareModelTypeRef() {
		return rtuAvailableFirmwareModelTypeRef != null ? rtuAvailableFirmwareModelTypeRef
				: (rtuAvailableFirmwareModelTypeRef = new TypeReference<List<AvailableFirmwareModel>>() {
				});
	}
}
