package com.arrow.selene.data;

import com.arrow.acn.client.model.CloudPlatform;

public class Gateway extends BaseEntity {
	private static final long serialVersionUID = -4501299270811912518L;

	private String hid;
	private String name;
	private String uid;
	private String iotConnectUrl;
	private String apiKey;
	private String secretKey;
	private String externalId;
	private String topology;
	private long heartBeatIntervalMs;
	private int purgeTelemetryIntervalDays;
	private int purgeMessagesIntervalDays;
	private CloudPlatform cloudPlatform = CloudPlatform.IotConnect;
	private String properties;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

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

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getIotConnectUrl() {
		return iotConnectUrl;
	}

	public void setIotConnectUrl(String iotConnectUrl) {
		this.iotConnectUrl = iotConnectUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getTopology() {
		return topology;
	}

	public void setTopology(String topology) {
		this.topology = topology;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public long getHeartBeatIntervalMs() {
		return heartBeatIntervalMs;
	}

	public void setHeartBeatIntervalMs(long heartBeatIntervalMs) {
		this.heartBeatIntervalMs = heartBeatIntervalMs;
	}

	public int getPurgeTelemetryIntervalDays() {
		return purgeTelemetryIntervalDays;
	}

	public void setPurgeTelemetryIntervalDays(int purgeTelemetryIntervalDays) {
		this.purgeTelemetryIntervalDays = purgeTelemetryIntervalDays;
	}

	public int getPurgeMessagesIntervalDays() {
		return purgeMessagesIntervalDays;
	}

	public void setPurgeMessagesIntervalDays(int purgeMessagesIntervalDays) {
		this.purgeMessagesIntervalDays = purgeMessagesIntervalDays;
	}

	public CloudPlatform getCloudPlatform() {
		return cloudPlatform;
	}

	public void setCloudPlatform(CloudPlatform cloudPlatform) {
		this.cloudPlatform = cloudPlatform;
	}
}
