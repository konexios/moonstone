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
package com.arrow.acn;

public interface AcnEventNames {
	interface ServerToGateway {
		final static String GATEWAY_SOFTWARE_UPDATE = "ServerToGateway_GatewaySoftwareUpdate";

		final static String DEVICE_START = "ServerToGateway_DeviceStart";
		final static String DEVICE_STOP = "ServerToGateway_DeviceStop";

		final static String DEVICE_PROPERTY_CHANGE = "ServerToGateway_DevicePropertyChange";
		final static String DEVICE_COMMAND = "ServerToGateway_DeviceCommand";
		final static String DEVICE_STATE_REQUEST = "ServerToGateway_DeviceStateRequest";

		final static String SENSOR_PROPERTY_CHANGE = "ServerToGateway_SensorPropertyChange";
		final static String SENSOR_TELEMETRY_CHANGE = "ServerToGateway_SensorTelemetryChange";

		final static String GATEWAY_SOFTWARE_RELEASE = "ServerToGateway_GatewaySoftwareRelease";
		final static String DEVICE_SOFTWARE_RELEASE = "ServerToGateway_DeviceSoftwareRelease";

		final static String GATEWAY_CONFIGURATION_UPDATE = "ServerToGateway_GatewayConfigurationUpdate";
		final static String DEVICE_CONFIGURATION_UPDATE = "ServerToGateway_DeviceConfigurationUpdate";

		final static String GATEWAY_CONFIGURATION_RESTORE = "ServerToGateway_GatewayConfigurationRestore";
		final static String DEVICE_CONFIGURATION_RESTORE = "ServerToGateway_DeviceConfigurationRestore";

	}

	interface GatewayToServer {
		final static String API_REQUEST = "GatewayToServer_ApiRequest";
	}
}