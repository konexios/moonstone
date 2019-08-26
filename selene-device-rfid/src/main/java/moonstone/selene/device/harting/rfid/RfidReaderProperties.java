package moonstone.selene.device.harting.rfid;

import java.util.Map;

import moonstone.selene.engine.DeviceProperties;
import moonstone.selene.engine.EngineConstants.Mqtt;

public class RfidReaderProperties extends DeviceProperties {
    private static final long serialVersionUID = -3077230294486717092L;

    private int diagnosticIntervalSec = 300;
    private int syncTimeIntervalMin = 15;
    private String mqttUrl = Mqtt.DEFAULT_URL;
    private String mqttUserName = Mqtt.DEFAULT_USERNAME;
    private String mqttPassword = Mqtt.DEFAULT_PASSWORD;
    private String mqttCommandTopic = "harting/command";
    private String mqttResponseTopic = "harting/response";
    private String mqttTelemetryTopic = "harting/telemetry";
    private String mqttRouteCommandTopic = "harting/route/command";
    private String mqttRouteResponseTopic = "harting/route/response";

    @Override
    public RfidReaderProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        diagnosticIntervalSec = Integer
                .parseInt(map.getOrDefault("diagnosticIntervalSec", Integer.toString(diagnosticIntervalSec)));
        syncTimeIntervalMin = Integer
                .parseInt(map.getOrDefault("syncTimeIntervalMin", Integer.toString(syncTimeIntervalMin)));
        mqttUrl = map.getOrDefault("mqttUrl", mqttUrl);
        mqttUserName = map.getOrDefault("mqttUsername", mqttUserName);
        mqttPassword = map.getOrDefault("mqttPassword", mqttPassword);
        mqttCommandTopic = map.getOrDefault("mqttCommandTopic", mqttCommandTopic);
        mqttResponseTopic = map.getOrDefault("mqttResponseTopic", mqttResponseTopic);
        mqttTelemetryTopic = map.getOrDefault("mqttTelemetryTopic", mqttTelemetryTopic);
        mqttRouteCommandTopic = map.getOrDefault("mqttRouteCommandTopic", mqttRouteCommandTopic);
        mqttRouteResponseTopic = map.getOrDefault("mqttRouteResponseTopic", mqttRouteResponseTopic);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("diagnosticIntervalSec", Integer.toString(diagnosticIntervalSec));
        map.put("syncTimeIntervalMin", Integer.toString(syncTimeIntervalMin));
        if (mqttUrl != null)
            map.put("mqttUrl", mqttUrl);
        if (mqttUserName != null)
            map.put("mqttUserName", mqttUserName);
        if (mqttPassword != null)
            map.put("mqttPassword", mqttPassword);
        if (mqttCommandTopic != null)
            map.put("mqttCommandTopic", mqttCommandTopic);
        if (mqttResponseTopic != null)
            map.put("mqttResponseTopic", mqttResponseTopic);
        if (mqttTelemetryTopic != null)
            map.put("mqttTelemetryTopic", mqttTelemetryTopic);
        if (mqttRouteCommandTopic != null)
            map.put("mqttRouteCommandTopic", mqttRouteCommandTopic);
        if (mqttRouteResponseTopic != null)
            map.put("mqttRouteResponseTopic", mqttRouteResponseTopic);
        return map;
    }

    public int getDiagnosticIntervalSec() {
        return diagnosticIntervalSec;
    }

    public void setDiagnosticIntervalSec(int diagnosticIntervalSec) {
        this.diagnosticIntervalSec = diagnosticIntervalSec;
    }

    public int getSyncTimeIntervalMin() {
        return syncTimeIntervalMin;
    }

    public void setSyncTimeIntervalMin(int syncTimeIntervalMin) {
        this.syncTimeIntervalMin = syncTimeIntervalMin;
    }

    public String getMqttUrl() {
        return mqttUrl;
    }

    public void setMqttUrl(String mqttUrl) {
        this.mqttUrl = mqttUrl;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getMqttCommandTopic() {
        return mqttCommandTopic;
    }

    public void setMqttCommandTopic(String mqttCommandTopic) {
        this.mqttCommandTopic = mqttCommandTopic;
    }

    public String getMqttResponseTopic() {
        return mqttResponseTopic;
    }

    public void setMqttResponseTopic(String mqttResponseTopic) {
        this.mqttResponseTopic = mqttResponseTopic;
    }

    public String getMqttTelemetryTopic() {
        return mqttTelemetryTopic;
    }

    public void setMqttTelemetryTopic(String mqttTelemetryTopic) {
        this.mqttTelemetryTopic = mqttTelemetryTopic;
    }

    public String getMqttRouteCommandTopic() {
        return mqttRouteCommandTopic;
    }

    public void setMqttRouteCommandTopic(String mqttRouteCommandTopic) {
        this.mqttRouteCommandTopic = mqttRouteCommandTopic;
    }

    public String getMqttRouteResponseTopic() {
        return mqttRouteResponseTopic;
    }

    public void setMqttRouteResponseTopic(String mqttRouteResponseTopic) {
        this.mqttRouteResponseTopic = mqttRouteResponseTopic;
    }
}
