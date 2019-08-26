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

import moonstone.acs.AcsUtils;
import moonstone.acs.client.api.ApiConfig;

public final class AcnClient {
	private ApiConfig apiConfig;
	private AccountApi accountApi;
	private ConfigBackupApi configBackupApi;
	private CoreEventApi coreEventApi;
	private CoreUserApi coreUserApi;
	private DeviceActionApi deviceActionApi;
	private DeviceApi deviceApi;
	private DeviceStateApi deviceStateApi;
	private DeviceTypeApi deviceTypeApi;
	private GatewayApi gatewayApi;
	private NodeApi nodeApi;
	private NodeTypeApi nodeTypeApi;
	private RTUFirmwareApi rtuFirmwareApi;
	private SoftwareReleaseScheduleApi softwareReleaseScheduleApi;
	private SoftwareReleaseTransApi softwareReleaseTransApi;
	private TelemetryApi telemetryApi;
	private TelemetryUnitApi telemetryUnitApi;

	public AcnClient(ApiConfig apiConfig) {
		AcsUtils.notNull(apiConfig, "apiConfig is not set");
		this.apiConfig = apiConfig;
	}

	public void setApiConfig(ApiConfig apiConfig) {
		AcsUtils.notNull(apiConfig, "apiConfig is not set");
		this.apiConfig = apiConfig;
		if (accountApi != null)
			getAccountApi().setApiConfig(apiConfig);
		if (configBackupApi != null)
			getConfigBackupApi().setApiConfig(apiConfig);
		if (coreEventApi != null)
			getCoreEventApi().setApiConfig(apiConfig);
		if (coreUserApi != null)
			getCoreUserApi().setApiConfig(apiConfig);
		if (deviceActionApi != null)
			getDeviceActionApi().setApiConfig(apiConfig);
		if (deviceApi != null)
			getDeviceApi().setApiConfig(apiConfig);
		if (deviceStateApi != null)
			getDeviceStateApi().setApiConfig(apiConfig);
		if (deviceTypeApi != null)
			getDeviceTypeApi().setApiConfig(apiConfig);
		if (gatewayApi != null)
			getGatewayApi().setApiConfig(apiConfig);
		if (nodeApi != null)
			getNodeApi().setApiConfig(apiConfig);
		if (nodeTypeApi != null)
			getNodeTypeApi().setApiConfig(apiConfig);
		if (rtuFirmwareApi != null)
			getRTUFirmwareApi().setApiConfig(apiConfig);
		if (softwareReleaseScheduleApi != null)
			getSoftwareReleaseScheduleApi().setApiConfig(apiConfig);
		if (softwareReleaseTransApi != null)
			getSoftwareReleaseTransApi().setApiConfig(apiConfig);
		if (telemetryApi != null)
			getTelemetryApi().setApiConfig(apiConfig);
		if (telemetryUnitApi != null)
			getTelemetryUnitApi().setApiConfig(apiConfig);
	}

	public ApiConfig getApiConfig() {
		return apiConfig;
	}

	public synchronized AccountApi getAccountApi() {
		return accountApi != null ? accountApi : (accountApi = new AccountApi(apiConfig));
	}

	public synchronized CoreEventApi getCoreEventApi() {
		return coreEventApi != null ? coreEventApi : (coreEventApi = new CoreEventApi(apiConfig));
	}

	public synchronized CoreUserApi getCoreUserApi() {
		return coreUserApi != null ? coreUserApi : (coreUserApi = new CoreUserApi(apiConfig));
	}

	public synchronized DeviceActionApi getDeviceActionApi() {
		return deviceActionApi != null ? deviceActionApi : (deviceActionApi = new DeviceActionApi(apiConfig));
	}

	public synchronized DeviceApi getDeviceApi() {
		return deviceApi != null ? deviceApi : (deviceApi = new DeviceApi(apiConfig));
	}

	public synchronized DeviceStateApi getDeviceStateApi() {
		return deviceStateApi != null ? deviceStateApi : (deviceStateApi = new DeviceStateApi(apiConfig));
	}

	public synchronized GatewayApi getGatewayApi() {
		return gatewayApi != null ? gatewayApi : (gatewayApi = new GatewayApi(apiConfig));
	}

	public synchronized NodeApi getNodeApi() {
		return nodeApi != null ? nodeApi : (nodeApi = new NodeApi(apiConfig));
	}

	public synchronized NodeTypeApi getNodeTypeApi() {
		return nodeTypeApi != null ? nodeTypeApi : (nodeTypeApi = new NodeTypeApi(apiConfig));
	}

	public synchronized SoftwareReleaseScheduleApi getSoftwareReleaseScheduleApi() {
		return softwareReleaseScheduleApi != null ? softwareReleaseScheduleApi
				: (softwareReleaseScheduleApi = new SoftwareReleaseScheduleApi(apiConfig));
	}

	public synchronized SoftwareReleaseTransApi getSoftwareReleaseTransApi() {
		return softwareReleaseTransApi != null ? softwareReleaseTransApi
				: (softwareReleaseTransApi = new SoftwareReleaseTransApi(apiConfig));
	}

	public synchronized TelemetryApi getTelemetryApi() {
		return telemetryApi != null ? telemetryApi : (telemetryApi = new TelemetryApi(apiConfig));
	}

	public synchronized ConfigBackupApi getConfigBackupApi() {
		return configBackupApi != null ? configBackupApi : (configBackupApi = new ConfigBackupApi(apiConfig));
	}

	public synchronized RTUFirmwareApi getRTUFirmwareApi() {
		return rtuFirmwareApi != null ? rtuFirmwareApi : (rtuFirmwareApi = new RTUFirmwareApi(apiConfig));
	}

	public synchronized TelemetryUnitApi getTelemetryUnitApi() {
		return telemetryUnitApi != null ? telemetryUnitApi : (telemetryUnitApi = new TelemetryUnitApi(apiConfig));
	}

	public synchronized DeviceTypeApi getDeviceTypeApi() {
		return deviceTypeApi != null ? deviceTypeApi : (deviceTypeApi = new DeviceTypeApi(apiConfig));
	}
}
