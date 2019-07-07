package com.arrow.selene.device.mqttrouter;

import java.util.Map;

public class AdapterRequestData {
    private String command;
    private Map<String, String> payload;

    @SuppressWarnings("unchecked")
    public AdapterRequestData populateFrom(Map<String, Object> map) {
        command = (String) map.getOrDefault("command", command);
        payload = (Map<String, String>) map.getOrDefault("payload", payload);
        return this;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }
}
