package com.arrow.selene.engine;

import java.io.Serializable;
import java.util.Map;

import com.arrow.selene.engine.EngineConstants.RedisConfig;

public class EngineProperties implements Serializable {
    private static final long serialVersionUID = 5100033105980323285L;

    private String redisHost = RedisConfig.DEFAULT_HOST;
    private boolean enabled = true;
    private String scriptingEngine = EngineConstants.DEFAULT_SCRIPTING_ENGINE;
    private String restartScriptFilename = EngineConstants.DEFAULT_RESTART_SCRIPT_FILENAME;
    private String homeDirectory;

    public EngineProperties populateFrom(Map<String, String> map) {
        redisHost = map.getOrDefault("redisHost", redisHost);
        enabled = Boolean.parseBoolean(map.getOrDefault("enabled", Boolean.toString(enabled)));
        scriptingEngine = map.getOrDefault("scriptingEngine", scriptingEngine);
        restartScriptFilename = map.getOrDefault("restartScriptFilename", restartScriptFilename);
        homeDirectory = map.getOrDefault("homeDirectory", homeDirectory);
        return this;
    }

    public Map<String, String> populateTo(Map<String, String> map) {
        if (redisHost != null)
            map.put("redisHost", redisHost);
        map.put("enabled", Boolean.toString(enabled));
        if (scriptingEngine != null)
            map.put("scriptingEngine", scriptingEngine);
        if (restartScriptFilename != null)
            map.put("restartScriptFilename", restartScriptFilename);
        if (homeDirectory != null)
            map.put("homeDirectory", homeDirectory);
        return map;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getScriptingEngine() {
        return scriptingEngine;
    }

    public void setScriptingEngine(String scriptingEngine) {
        this.scriptingEngine = scriptingEngine;
    }

    public String getRestartScriptFilename() {
        return restartScriptFilename;
    }

    public void setRestartScriptFilename(String restartScriptFilename) {
        this.restartScriptFilename = restartScriptFilename;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }
}
