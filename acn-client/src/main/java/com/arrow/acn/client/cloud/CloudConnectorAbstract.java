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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.api.AcnClient;
import com.arrow.acn.client.api.CoreEventApi;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.GatewayPayloadSigner;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.Loggable;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.GatewayEventModel;

public abstract class CloudConnectorAbstract extends Loggable {
	private String gatewayHid;
	protected MessageListener listener;
	protected AcnClient acnClient;

	protected CloudConnectorAbstract(AcnClient acnClient) {
		this.acnClient = acnClient;
	}

	protected String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}

	public void setListener(MessageListener listener) {
		this.listener = listener;
	}

	public abstract void start();

	public abstract void stop();

	public abstract void send(IotParameters payload);

	public abstract void sendBatch(List<IotParameters> batch, TransferMode transferMode);

	protected void validateAndProcessEvent(String topic, byte[] payload) {
		String method = "validateAndProcessEvent";
//		GatewayEventModel model = JsonUtils.fromJson(new String(payload, Charset.defaultCharset()),
//		        GatewayEventModel.class);

		GatewayEventModel model = JsonUtils.fromJson(new String(payload, Charset.forName("UTF-8")),
				GatewayEventModel.class);

		if (model == null || AcsUtils.isEmpty(model.getName())) {
			logError(method, "ignore invalid payload: %s", payload);
			return;
		}
		CoreEventApi eventApi = acnClient.getCoreEventApi();
		eventApi.putReceived(model.getHid());
		if (isSignatureValid(model)) {
			logDebug(method, "signature is valid");
			if (listener == null) {
				logError(method, "listener is not defined");
				eventApi.putFailed(model.getHid(), "Listener is not defined");
			} else {
				try {
					listener.processMessage(topic, payload);
					eventApi.putSucceeded(model.getHid());
				} catch (Exception e) {
					eventApi.putFailed(model.getHid(), e.getMessage());
				}
			}
		} else {
			logWarn(method, "signature is invalid, even with UTF-8 charset");
			eventApi.putFailed(model.getHid(), "Signature is invalid");
		}
	}

	private boolean isSignatureValid(GatewayEventModel model) {
		String method = "isSignatureValid";
		if (AcsUtils.isEmpty(model.getSignature())) {
			logDebug(method, "skipping signature validation");
			return true;
		}
		logDebug(method, "validating signature");
		ApiConfig apiConfig = acnClient.getApiConfig();
		GatewayPayloadSigner signer = GatewayPayloadSigner.create(apiConfig.getSecretKey())
				.withApiKey(apiConfig.getApiKey()).withHid(model.getHid()).withName(model.getName())
				.withEncrypted(model.isEncrypted());
		for (Entry<String, String> entry : model.getParameters().entrySet()) {
			signer.withParameter(entry.getKey(), entry.getValue());
		}
		String signatureVersion = model.getSignatureVersion();
		switch (signatureVersion) {
		case GatewayPayloadSigner.PAYLOAD_SIGNATURE_VERSION_1: {
			return Objects.equals(signer.signV1(), model.getSignature());
		}
		default: {
			logWarn(method, "signature of version %s is not supported", signatureVersion);
			return false;
		}
		}
	}
}
