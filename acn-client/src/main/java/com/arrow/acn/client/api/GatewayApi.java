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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.model.AuditLogModel;
import com.arrow.acn.client.model.ConfigBackupModel;
import com.arrow.acn.client.model.CreateConfigBackupModel;
import com.arrow.acn.client.model.CreateGatewayModel;
import com.arrow.acn.client.model.DeviceCommandModel;
import com.arrow.acn.client.model.DeviceModel;
import com.arrow.acn.client.model.GatewayConfigModel;
import com.arrow.acn.client.model.GatewayModel;
import com.arrow.acn.client.model.UpdateGatewayModel;
import com.arrow.acn.client.search.GatewaySearchCriteria;
import com.arrow.acn.client.search.LogsSearchCriteria;
import com.arrow.acn.client.search.SortedSearchCriteria;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.ErrorModel;
import com.arrow.acs.client.model.ExternalHidModel;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public final class GatewayApi extends ApiAbstract {
	private static final String GATEWAYS_BASE_URL = API_BASE + "/gateways";
	private static final String REGISTER_URL = GATEWAYS_BASE_URL;
	private static final String FIND_ALL_BY_URL = GATEWAYS_BASE_URL;
	private static final String SPECIFIC_GATEWAY_URL = GATEWAYS_BASE_URL + "/{hid}";
	private static final String FIND_URL = SPECIFIC_GATEWAY_URL;
	private static final String UPDATE_EXISTING_URL = SPECIFIC_GATEWAY_URL;
	private static final String DEVICES_URL = SPECIFIC_GATEWAY_URL + "/devices";
	private static final String CHECKIN_URL = SPECIFIC_GATEWAY_URL + "/checkin";
	private static final String DEVICE_COMMAND_URL = SPECIFIC_GATEWAY_URL + "/commands/device-command";
	private static final String CONFIG_URL = SPECIFIC_GATEWAY_URL + "/config";
	private static final String HEARTBEAT_URL = SPECIFIC_GATEWAY_URL + "/heartbeat";
	private static final String LOGS_URL = SPECIFIC_GATEWAY_URL + "/logs";
	private static final String SEND_ERROR_URL = SPECIFIC_GATEWAY_URL + "/errors";
	private static final String CONFIGURATION_BACKUP_URL = SPECIFIC_GATEWAY_URL + "/config-backups";
	private static final String SPECIFIC_CONFIGURATION_BACKUP_URL = CONFIGURATION_BACKUP_URL + "/{configBackupHid}";
	private static final String RESTORE_CONFIGURATION_URL = SPECIFIC_CONFIGURATION_BACKUP_URL + "/restore";

	// instantiation is expensive for these objects
	private TypeReference<PagingResultModel<GatewayModel>> gatewayModelTypeRef;
	private TypeReference<PagingResultModel<AuditLogModel>> auditLogModelTypeRef;
	private TypeReference<ListResultModel<DeviceModel>> deviceModelTypeRef;
	private TypeReference<PagingResultModel<ConfigBackupModel>> configBackupModelTypeRef;

	GatewayApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	/**
	 * Sends GET request to obtain parameters of gateways corresponding
	 * {@code criteria}
	 *
	 * @param criteria {@link GatewaySearchCriteria} representing search filter
	 *                 parameters
	 *
	 * @return subset of {@link GatewayModel} containing gateway parameters.
	 *         <b>Note:</b> resulting subset may contain not all gateways
	 *         corresponding to search parameters because it cannot exceed page size
	 *         passed in {@code criteria}
	 *
	 * @throws AcnClientException if request failed
	 */
	public PagingResultModel<GatewayModel> findAllBy(GatewaySearchCriteria criteria) {
		String method = "findAllBy";
		try {
			URI uri = buildUri(FIND_ALL_BY_URL, criteria);
			PagingResultModel<GatewayModel> result = execute(new HttpGet(uri), getGatewayModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to register new or update existing gateway according to
	 * {@code model} passed
	 *
	 * @param model {@link GatewayModel} representing parameters of gateway to be
	 *              created/updated
	 *
	 * @return {@link ExternalHidModel} containing external {@code hid} of gateway
	 *         created/updated
	 *
	 * @throws AcnClientException if request failed
	 */
	public ExternalHidModel registerNewGateway(CreateGatewayModel model) {
		String method = "registerNewGateway";
		try {
			URI uri = buildUri(REGISTER_URL);
			logInfo(method, "execute ...");
			ExternalHidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), ExternalHidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain parameters of gateway specified by its
	 * {@code hid}
	 *
	 * @param hid {@link String} representing specific gateway
	 *
	 * @return {@link GatewayModel} containing gateway parameters
	 *
	 * @throws AcnClientException if request failed
	 */
	public GatewayModel find(String hid) {
		String method = "find";
		try {
			URI uri = buildUri(FIND_URL.replace("{hid}", hid));
			GatewayModel result = execute(new HttpGet(uri), GatewayModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain all devices associated with gateway specified by
	 * its {@code hid}
	 *
	 * @param hid {@link String} representing specific gateway
	 *
	 * @return list of {@link DeviceModel} containing device parameters
	 *
	 * @throws AcnClientException if request failed
	 */
	public ListResultModel<DeviceModel> listGatewayDevices(String hid) {
		String method = "find";
		try {
			URI uri = buildUri(DEVICES_URL.replace("{hid}", hid));
			ListResultModel<DeviceModel> result = execute(new HttpGet(uri), getDeviceModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends PUT request to update specific existing gateway according to
	 * {@code model} passed
	 *
	 * @param hid   {@link String} representing {@code hid} of gateway to be updated
	 * @param model {@link GatewayModel} representing gateway parameters to be
	 *              updated
	 *
	 * @return {@link HidModel} containing {@code hid} of gateway updated
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel updateExistingGateway(String hid, UpdateGatewayModel model) {
		String method = "updateExistingGateway";
		try {
			URI uri = buildUri(UPDATE_EXISTING_URL.replace("{hid}", hid));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends PUT request to check-in existing gateway on portal
	 *
	 * @param hid {@link String} representing {@code hid} of gateway to be
	 *            checked-in
	 *
	 * @return {@link StatusModel} containing status of check-in request
	 *
	 * @throws AcnClientException if request failed
	 */
	public StatusModel checkin(String hid) {
		String method = "checkin";
		try {
			URI uri = buildUri(CHECKIN_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpPut(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to force portal send command to gateway and device
	 *
	 * @param hid   {@link String} representing {@code hid} of gateway, to which
	 *              command should be sent
	 * @param model {@link DeviceCommandModel} representing command parameters to be
	 *              sent to device
	 *
	 * @return {@link HidModel} containing {@code hid} of gateway
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel sendCommandToGatewayAndDevice(String hid, DeviceCommandModel model) {
		String method = "sendCommandToGatewayAndDevice";
		try {
			URI uri = buildUri(DEVICE_COMMAND_URL.replace("{hid}", hid));
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to download configuration of specific gateway
	 *
	 * @param hid {@link String} representing {@code hid} of gateway, which
	 *            configuration should be downloaded
	 *
	 * @return {@link GatewayConfigModel} containing gateway configuration
	 *
	 * @throws AcnClientException if request failed
	 */
	public GatewayConfigModel downloadGatewayConfiguration(String hid) {
		String method = "downloadGatewayConfiguration";
		try {
			URI uri = buildUri(CONFIG_URL.replace("{hid}", hid));
			GatewayConfigModel result = execute(new HttpGet(uri), GatewayConfigModel.class);
			logDebug(method, "hasKey: %s, hasAws: %s, hasIbm: %s", result.getKey() != null, result.getAws() != null,
					result.getIbm() != null);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends PUT request to pass heartbeat of existing gateway to portal
	 *
	 * @param hid {@link String} representing {@code hid} of gateway to be kept
	 *            alive
	 *
	 * @return {@link StatusModel} containing status of heartbeat request
	 *
	 * @throws AcnClientException if request failed
	 */
	public StatusModel heartbeat(String hid) {
		String method = "heartbeat";
		try {
			URI uri = buildUri(HEARTBEAT_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpPut(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain logs related to specific gateway and
	 * corresponding {@code criteria}
	 *
	 * @param hid      {@link String} representing gateway {@code hid}
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
	public PagingResultModel<AuditLogModel> listGatewayAuditLogs(String hid, LogsSearchCriteria criteria) {
		String method = "listGatewayAuditLogs";
		try {
			URI uri = buildUri(LOGS_URL.replace("{hid}", hid), criteria);
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

	public StatusModel restoreConfiguration(String gatewayHid, String configBackupHid) {
		String method = "restoreConfiguration";
		try {
			URI uri = buildUri(RESTORE_CONFIGURATION_URL.replace("{hid}", gatewayHid).replace("{configBackupHid}",
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

	public StatusModel deleteGateway(String hid) {
		String method = "deleteGateway";
		try {
			URI uri = buildUri(SPECIFIC_GATEWAY_URL.replace("{hid}", hid));
			StatusModel result = execute(new HttpDelete(uri), StatusModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<PagingResultModel<GatewayModel>> getGatewayModelTypeRef() {
		return gatewayModelTypeRef != null ? gatewayModelTypeRef
				: (gatewayModelTypeRef = new TypeReference<PagingResultModel<GatewayModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<AuditLogModel>> getAuditLogModelTypeRef() {
		return auditLogModelTypeRef != null ? auditLogModelTypeRef
				: (auditLogModelTypeRef = new TypeReference<PagingResultModel<AuditLogModel>>() {
				});
	}

	private synchronized TypeReference<ListResultModel<DeviceModel>> getDeviceModelTypeRef() {
		return deviceModelTypeRef != null ? deviceModelTypeRef
				: (deviceModelTypeRef = new TypeReference<ListResultModel<DeviceModel>>() {
				});
	}

	private synchronized TypeReference<PagingResultModel<ConfigBackupModel>> getConfigBackupModelTypeRef() {
		return configBackupModelTypeRef != null ? configBackupModelTypeRef
				: (configBackupModelTypeRef = new TypeReference<PagingResultModel<ConfigBackupModel>>() {
				});
	}
}
