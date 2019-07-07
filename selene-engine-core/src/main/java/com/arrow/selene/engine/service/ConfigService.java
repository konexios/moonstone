package com.arrow.selene.engine.service;

import com.arrow.selene.engine.EngineProperties;

public class ConfigService extends com.arrow.selene.service.ConfigService {

    private static class SingletonHolder {
        private static final ConfigService SINGLETON = new ConfigService();
    }

    public static ConfigService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private EngineProperties engineProperties;

    private ConfigService() {
        super();
        String method = "ConfigService";
        logDebug(method, "...");
        try {
            engineProperties = new EngineProperties().populateFrom(getConfig()).populateFrom(filterConfig("engine"));
        } catch (Exception e) {
            logError(method, "error populating engine properties", e);
        }
        logDebug(method, "done!");
    }

    public EngineProperties getEngineProperties() {
        return engineProperties;
    }
}
