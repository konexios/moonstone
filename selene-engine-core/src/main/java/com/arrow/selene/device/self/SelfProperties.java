package com.arrow.selene.device.self;

import java.util.Map;

import com.arrow.acn.client.cloud.TransferMode;
import com.arrow.selene.engine.DeviceProperties;
import com.arrow.selene.engine.EngineConstants;

public class SelfProperties extends DeviceProperties {
    private static final long serialVersionUID = 5803104520737366341L;

    private String iotConnectUrl;
    private String iotConnectMqtt;
    private String iotConnectMqttVHost;
    private String apiKey;
    private String secretKey;
    private long heartBeatIntervalMs = EngineConstants.DEFAULT_HEARTBEAT_INTERVAL_MS;
    private int purgeTelemetryIntervalDays = EngineConstants.DEFAULT_PURGE_TELEMETRY_INTERVAL_DAYS;
    private int purgeMessagesIntervalDays = EngineConstants.DEFAULT_PURGE_MESSAGES_INTERVAL_DAYS;
    private boolean enabled = true;
    private boolean sendGatewayErrors = false;
    private boolean sendDeviceErrors = false;

    private int healthCheckIntervalMins = EngineConstants.DEFAULT_HEALTH_CHECK_INTERVAL_MINS;
    private String healthCheckScriptFile;
    private String healthCheckParserFile;

    private String cloudTransferMode = TransferMode.BATCH.name();
    private long cloudBatchSendingIntervalMs = EngineConstants.DEFAULT_CLOUD_BATCH_SENDING_INTERVAL_MS;
    private boolean cloudTelemetryPublishing = true;

    private boolean edgeMqttEnabled = false;
    private String edgeMqttUrl = "tcp://localhost:1883";
    private String edgeMqttUsername;
    private String edgeMqttPassword;
    private String edgeMqttPublisherTopic;
    private String edgeMqttSubscriberTopic;

    private String cryptoMode = EngineConstants.DEFAULT_CRYPTO_MODE;

    @Override
    public SelfProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        iotConnectUrl = map.getOrDefault("iotConnectUrl", iotConnectUrl);
        iotConnectMqtt = map.getOrDefault("iotConnectMqtt", iotConnectMqtt);
        iotConnectMqttVHost = map.getOrDefault("iotConnectMqttVHost", iotConnectMqttVHost);
        apiKey = map.getOrDefault("apiKey", apiKey);
        secretKey = map.getOrDefault("secretKey", secretKey);
        heartBeatIntervalMs = Long
                .parseLong(map.getOrDefault("heartBeatIntervalMs", Long.toString(heartBeatIntervalMs)));
        purgeTelemetryIntervalDays = Integer
                .parseInt(map.getOrDefault("purgeTelemetryIntervalDays", Integer.toString(purgeTelemetryIntervalDays)));
        purgeMessagesIntervalDays = Integer
                .parseInt(map.getOrDefault("purgeMessagesIntervalDays", Integer.toString(purgeMessagesIntervalDays)));
        enabled = Boolean.parseBoolean(map.getOrDefault("enabled", Boolean.toString(enabled)));
        sendGatewayErrors = Boolean
                .parseBoolean(map.getOrDefault("sendGatewayErrors", Boolean.toString(sendGatewayErrors)));
        sendDeviceErrors = Boolean
                .parseBoolean(map.getOrDefault("sendDeviceErrors", Boolean.toString(sendDeviceErrors)));
        healthCheckIntervalMins = Integer
                .parseInt(map.getOrDefault("healthCheckIntervalMins", Integer.toString(healthCheckIntervalMins)));
        healthCheckScriptFile = map.getOrDefault("healthCheckScriptFile", healthCheckScriptFile);
        healthCheckParserFile = map.getOrDefault("healthCheckParserFile", healthCheckParserFile);
        cloudTransferMode = map.getOrDefault("cloudTransferMode", cloudTransferMode);
        cloudBatchSendingIntervalMs = Long
                .parseLong(map.getOrDefault("cloudBatchSendingIntervalMs", Long.toString(cloudBatchSendingIntervalMs)));
        cloudTelemetryPublishing = Boolean
                .parseBoolean(map.getOrDefault("cloudTelemetryPublishing", Boolean.toString(cloudTelemetryPublishing)));
        edgeMqttEnabled = Boolean.parseBoolean(map.getOrDefault("edgeMqttEnabled", Boolean.toString(edgeMqttEnabled)));
        edgeMqttUrl = map.getOrDefault("edgeMqttUrl", edgeMqttUrl);
        edgeMqttUsername = map.getOrDefault("edgeMqttUsername", edgeMqttUsername);
        edgeMqttPassword = map.getOrDefault("edgeMqttPassword", edgeMqttPassword);
        edgeMqttPublisherTopic = map.getOrDefault("edgeMqttPublisherTopic", edgeMqttPublisherTopic);
        edgeMqttSubscriberTopic = map.getOrDefault("edgeMqttSubscriberTopic", edgeMqttSubscriberTopic);
        cryptoMode = map.getOrDefault("cryptoMode", cryptoMode);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (iotConnectUrl != null)
            map.put("iotConnectUrl", iotConnectUrl);
        if (iotConnectMqtt != null)
            map.put("iotConnectMqtt", iotConnectMqtt);
        if (iotConnectMqttVHost != null)
            map.put("iotConnectMqttVHost", iotConnectMqttVHost);
        if (apiKey != null)
            map.put("apiKey", apiKey);
        if (secretKey != null)
            map.put("secretKey", secretKey);
        map.put("heartBeatIntervalMs", Long.toString(heartBeatIntervalMs));
        map.put("purgeTelemetryIntervalDays", Integer.toString(purgeTelemetryIntervalDays));
        map.put("purgeMessagesIntervalDays", Integer.toString(purgeMessagesIntervalDays));
        map.put("enabled", Boolean.toString(enabled));
        map.put("sendGatewayErrors", Boolean.toString(sendGatewayErrors));
        map.put("sendDeviceErrors", Boolean.toString(sendDeviceErrors));
        map.put("healthCheckIntervalMins", Integer.toString(healthCheckIntervalMins));
        if (healthCheckScriptFile != null)
            map.put("healthCheckScriptFile", healthCheckScriptFile);
        if (healthCheckParserFile != null)
            map.put("healthCheckParserFile", healthCheckParserFile);
        if (cloudTransferMode != null)
            map.put("cloudTransferMode", cloudTransferMode);
        map.put("cloudBatchSendingIntervalMs", Long.toString(cloudBatchSendingIntervalMs));
        map.put("cloudTelemetryPublishing", Boolean.toString(cloudTelemetryPublishing));
        map.put("edgeMqttEnabled", Boolean.toString(edgeMqttEnabled));
        if (edgeMqttUrl != null)
            map.put("edgeMqttUrl", edgeMqttUrl);
        if (edgeMqttUsername != null)
            map.put("edgeMqttUsername", edgeMqttUsername);
        if (edgeMqttPassword != null)
            map.put("edgeMqttPassword", edgeMqttPassword);
        if (edgeMqttPublisherTopic != null)
            map.put("edgeMqttPublisherTopic", edgeMqttPublisherTopic);
        if (edgeMqttSubscriberTopic != null)
            map.put("edgeMqttSubscriberTopic", edgeMqttSubscriberTopic);
        if (cryptoMode != null)
            map.put("cryptoMode", cryptoMode);
        return map;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSendGatewayErrors() {
        return sendGatewayErrors;
    }

    public void setSendGatewayErrors(boolean sendGatewayErrors) {
        this.sendGatewayErrors = sendGatewayErrors;
    }

    public boolean isSendDeviceErrors() {
        return sendDeviceErrors;
    }

    public void setSendDeviceErrors(boolean sendDeviceErrors) {
        this.sendDeviceErrors = sendDeviceErrors;
    }

    public int getHealthCheckIntervalMins() {
        return healthCheckIntervalMins;
    }

    public void setHealthCheckIntervalMins(int healthCheckIntervalMins) {
        this.healthCheckIntervalMins = healthCheckIntervalMins;
    }

    public String getHealthCheckParserFile() {
        return healthCheckParserFile;
    }

    public void setHealthCheckParserFile(String healthCheckParserFile) {
        this.healthCheckParserFile = healthCheckParserFile;
    }

    public String getHealthCheckScriptFile() {
        return healthCheckScriptFile;
    }

    public void setHealthCheckScriptFile(String healthCheckScriptFile) {
        this.healthCheckScriptFile = healthCheckScriptFile;
    }

    public boolean isEdgeMqttEnabled() {
        return edgeMqttEnabled;
    }

    public void setEdgeMqttEnabled(boolean edgeMqttEnabled) {
        this.edgeMqttEnabled = edgeMqttEnabled;
    }

    public String getEdgeMqttUrl() {
        return edgeMqttUrl;
    }

    public void setEdgeMqttUrl(String edgeMqttUrl) {
        this.edgeMqttUrl = edgeMqttUrl;
    }

    public String getEdgeMqttUsername() {
        return edgeMqttUsername;
    }

    public void setEdgeMqttUsername(String edgeMqttUsername) {
        this.edgeMqttUsername = edgeMqttUsername;
    }

    public String getEdgeMqttPassword() {
        return edgeMqttPassword;
    }

    public void setEdgeMqttPassword(String edgeMqttPassword) {
        this.edgeMqttPassword = edgeMqttPassword;
    }

    public String getEdgeMqttPublisherTopic() {
        return edgeMqttPublisherTopic;
    }

    public void setEdgeMqttPublisherTopic(String edgeMqttPublisherTopic) {
        this.edgeMqttPublisherTopic = edgeMqttPublisherTopic;
    }

    public String getEdgeMqttSubscriberTopic() {
        return edgeMqttSubscriberTopic;
    }

    public void setEdgeMqttSubscriberTopic(String edgeMqttSubscriberTopic) {
        this.edgeMqttSubscriberTopic = edgeMqttSubscriberTopic;
    }

    public String getCryptoMode() {
        return cryptoMode;
    }

    public void setCryptoMode(String cryptoMode) {
        this.cryptoMode = cryptoMode;
    }

    public boolean isCloudTelemetryPublishing() {
        return cloudTelemetryPublishing;
    }

    public void setCloudTelemetryPublishing(boolean cloudTelemetryPublishing) {
        this.cloudTelemetryPublishing = cloudTelemetryPublishing;
    }
}
