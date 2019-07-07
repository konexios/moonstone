package com.arrow.selene.web;

import java.io.Serializable;

import com.arrow.acn.client.cloud.TransferMode;

public class GatewayProperties implements Serializable {
	private static final long serialVersionUID = 3870883819497845145L;

	private String name = "";
	private String uid = "";
	private String iotConnectUrl = "";
	private String iotConnectMqtt = "";
	private String iotConnectMqttVHost = "";
	private String apiKey = "";
	private String secretKey = "";
	private long heartBeatIntervalMs = 60000L;
	private boolean enabled = true;
	private String cloudTransferMode = TransferMode.BATCH.name();
	private long cloudBatchSendingIntervalMs = 500L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getIotConnectUrl() {
		return iotConnectUrl;
	}

	public void setIotConnectUrl(String iotConnectUrl) {
		this.iotConnectUrl = iotConnectUrl;
	}

	public String getIotConnectMqtt() {
		return iotConnectMqtt;
	}

	public void setIotConnectMqtt(String iotConnectMqtt) {
		this.iotConnectMqtt = iotConnectMqtt;
	}

	public String getIotConnectMqttVHost() {
		return iotConnectMqttVHost;
	}

	public void setIotConnectMqttVHost(String iotConnectMqttVHost) {
		this.iotConnectMqttVHost = iotConnectMqttVHost;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public long getHeartBeatIntervalMs() {
		return heartBeatIntervalMs;
	}

	public void setHeartBeatIntervalMs(long heartBeatIntervalMs) {
		this.heartBeatIntervalMs = heartBeatIntervalMs;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCloudTransferMode() {
		return cloudTransferMode;
	}

	public void setCloudTransferMode(String cloudTransferMode) {
		this.cloudTransferMode = cloudTransferMode;
	}

	public long getCloudBatchSendingIntervalMs() {
		return cloudBatchSendingIntervalMs;
	}

	public void setCloudBatchSendingIntervalMs(long cloudBatchSendingIntervalMs) {
		this.cloudBatchSendingIntervalMs = cloudBatchSendingIntervalMs;
	}

}
