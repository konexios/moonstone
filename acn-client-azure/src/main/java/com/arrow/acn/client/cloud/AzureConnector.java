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
package com.arrow.acn.client.cloud;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.api.AcnClient;
import com.arrow.acn.client.model.AzureConfigModel;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.MessageCallback;

public class AzureConnector extends CloudConnectorAbstract {

	private final static String CONNECTION_STRING_FORMAT = "HostName=%s;DeviceId=%s;SharedAccessKey=%s";

	private final AzureConfigModel model;
	private final String gatewayUid;
	private DeviceClient client;

	private AtomicLong eventCounter = new AtomicLong();
	private LocalEventCallback eventCallback = new LocalEventCallback();
	private LocalMessageCallback messageCallback = new LocalMessageCallback();

	public AzureConnector(AzureConfigModel model, String gatewayUid, AcnClient acnClient) {
		super(acnClient);
		this.model = model;
		this.gatewayUid = gatewayUid;
	}

	@Override
	public void start() {
		String method = "start";
		AcsUtils.notNull(model, "model is NULL");
		AcsUtils.notNull(gatewayUid, "gatewayUid is NULL");
		try {
			if (client == null) {
				String connectionString = String.format(CONNECTION_STRING_FORMAT, model.getHost(), gatewayUid,
				        model.getAccessKey());
				logInfo(method, "creating azure client ...");
				client = new DeviceClient(connectionString, IotHubClientProtocol.MQTT);
				client.setMessageCallback(messageCallback, null);
				logInfo(method, "connecting azure client ...");
				client.open();
			} else {
				logWarn(method, "client is already initialized");
			}
		} catch (AcsRuntimeException e) {
			throw e;
		} catch (Throwable t) {
			throw new AcsSystemException("Unable to start azure connector", t);
		}
	}

	@Override
	public void stop() {
		String method = "stop";
		if (client != null) {
			try {
				logInfo(method, "stopping client ...");
				client.closeNow();
			} catch (Throwable e) {
			}
			client = null;
		}
	}

	@Override
	public void send(IotParameters payload) {
		String method = "send";
		if (client != null) {
			String json = JsonUtils.toJson(payload);
			Message message = new Message(json);
			long counter = eventCounter.getAndIncrement();
			logDebug(method, "counter: %d, json size: %d", counter, json.length());
			client.sendEventAsync(message, eventCallback, counter);
		} else {
			logError(method, "client is NULL");
		}
	}

	@Override
	public void sendBatch(List<IotParameters> batch, TransferMode transferMode) {
		if (transferMode == TransferMode.GZIP_BATCH) {
			throw new AcsLogicalException(
			        "TransferMode not supported for Azure integration: " + TransferMode.GZIP_BATCH.name());
		}
		if (batch != null) {
			batch.forEach(this::send);
		}
	}

	class LocalMessageCallback implements MessageCallback {
		public IotHubMessageResult execute(Message msg, Object context) {
			String method = "messageCallback";
			byte[] bytes = msg.getBytes();
			logInfo(method, "payload: %s", new String(bytes, Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
			validateAndProcessEvent(gatewayUid, bytes);
			return IotHubMessageResult.COMPLETE;
		}
	}

	class LocalEventCallback implements IotHubEventCallback {
		public void execute(IotHubStatusCode status, Object context) {
			String method = "eventCallback";
			Long counter = (Long) context;
			logDebug(method, "counter: %d, status: %s", counter, status.name());
		}
	}
}
