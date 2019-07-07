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

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import com.arrow.acn.client.ClientConstants.Mqtt;
import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.api.AcnClient;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;

public abstract class MqttConnectorAbstract extends CloudConnectorAbstract implements MessageListener {
	private int qos;
	private CustomMqttClient client;

	protected MqttConnectorAbstract(String url, AcnClient acnClient) {
		super(acnClient);
		client = new CustomMqttClient(url);
	}

	protected MqttConnectorAbstract(String url, String gatewayHid, AcnClient acnClient) {
		super(acnClient);
		client = new CustomMqttClient(url, gatewayHid);
		setGatewayHid(gatewayHid);
	}

	@Override
	public void start() {
		String method = "MqttConnectorAbstract.start";
		client.setOptions(mqttConnectOptions());
		String topic = subscriberTopic();
		if (!AcsUtils.isEmpty(topic)) {
			client.setTopics(topic);
		} else {
			logWarn(method, "no topic to subscribe!");
		}
		client.setListener(this);
		client.connect(false);
	}

	@Override
	public void stop() {
		client.disconnect();
	}

	@Override
	public void sendBatch(List<IotParameters> batch, TransferMode transferMode) {
		byte[] input = JsonUtils.toJsonBytes(batch);
		if (input != null && input.length > 0) {
			if (transferMode == TransferMode.GZIP_BATCH) {
				client.publish(publisherGzipBatchTopic(batch), AcsUtils.gzip(input), getQos());
			} else {
				client.publish(publisherBatchTopic(batch), input, getQos());
			}
		}
	}

	@Override
	public void send(IotParameters payload) {
		if (payload != null) {
			client.publish(publisherTopic(payload), JsonUtils.toJsonBytes(payload), getQos());
		}
	}

	protected int getQos() {
		return qos;
	}

	protected void setQos(int qos) {
		this.qos = qos;
	}

	protected MqttConnectOptions mqttConnectOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setConnectionTimeout(Mqtt.DEFAULT_CONNECTION_TIMEOUT_SECS);
		options.setKeepAliveInterval(Mqtt.DEFAULT_KEEP_ALIVE_INTERVAL_SECS);
		return options;
	}

	@Override
	public void processMessage(String topic, byte[] payload) {
		validateAndProcessEvent(topic, payload);
	}

	protected abstract String publisherTopic(IotParameters payload);

	protected abstract String publisherBatchTopic(List<IotParameters> payload);

	protected abstract String publisherGzipBatchTopic(List<IotParameters> payload);

	protected abstract String subscriberTopic();
}
