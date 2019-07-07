package com.arrow.selene.engine;

import java.io.Serializable;
import java.util.Map;

public class DeviceProperties implements Serializable {
    private static final long serialVersionUID = 8396960318239435598L;

    private boolean enabled = true;
    private boolean persistTelemetry = EngineConstants.DEFAULT_DEVICE_PERSIST_TELEMETRY;
    private int numThreads = 1;
    private long maxPollingIntervalMs = EngineConstants.DEFAULT_MAX_POLLING_INTERVAL_MS;

    private String externalPropertyFile;
    private String externalIniFile;
    private String externalIniFileSection;

    // used by ScriptEngine
    private String dataParsingScriptFilename;

    public DeviceProperties populateFrom(Map<String, String> map) {
        enabled = Boolean.parseBoolean(map.getOrDefault("enabled", Boolean.toString(enabled)));
        persistTelemetry = Boolean
                .parseBoolean(map.getOrDefault("persistTelemetry", Boolean.toString(persistTelemetry)));
        numThreads = Integer.parseInt(map.getOrDefault("numThreads", Integer.toString(numThreads)));
        maxPollingIntervalMs = Long
                .parseLong(map.getOrDefault("maxPollingIntervalMs", Long.toString(maxPollingIntervalMs)));
        externalPropertyFile = map.getOrDefault("externalPropertyFile", externalPropertyFile);
        externalIniFile = map.getOrDefault("externalIniFile", externalIniFile);
        externalIniFileSection = map.getOrDefault("externalIniFileSection", externalIniFileSection);
        return this;
    }

    public Map<String, String> populateTo(Map<String, String> map) {
        map.put("enabled", Boolean.toString(enabled));
        map.put("persistTelemetry", Boolean.toString(persistTelemetry));
        map.put("numThreads", Integer.toString(numThreads));
        map.put("maxPollingIntervalMs", Long.toString(maxPollingIntervalMs));
        if (externalPropertyFile != null)
            map.put("externalPropertyFile", externalPropertyFile);
        if (externalIniFile != null)
            map.put("externalIniFile", externalIniFile);
        if (externalIniFileSection != null)
            map.put("externalIniFileSection", externalIniFileSection);
        return map;
    }

    public String getDataParsingScriptFilename() {
        return dataParsingScriptFilename;
    }

    public void setDataParsingScriptFilename(String dataParsingScriptFilename) {
        this.dataParsingScriptFilename = dataParsingScriptFilename;
    }

    public String getExternalIniFileSection() {
        return externalIniFileSection;
    }

    public void setExternalIniFileSection(String externalIniFileSection) {
        this.externalIniFileSection = externalIniFileSection;
    }

    public String getExternalPropertyFile() {
        return externalPropertyFile;
    }

    public void setExternalPropertyFile(String externalPropertyFile) {
        this.externalPropertyFile = externalPropertyFile;
    }

    public String getExternalIniFile() {
        return externalIniFile;
    }

    public void setExternalIniFile(String externalIniFile) {
        this.externalIniFile = externalIniFile;
    }

    public long getMaxPollingIntervalMs() {
        return maxPollingIntervalMs;
    }

    public void setMaxPollingIntervalMs(long maxPollingIntervalMs) {
        this.maxPollingIntervalMs = maxPollingIntervalMs;
    }

    public boolean isPersistTelemetry() {
        return persistTelemetry;
    }

    public void setPersistTelemetry(boolean persistTelemetry) {
        this.persistTelemetry = persistTelemetry;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }
}
