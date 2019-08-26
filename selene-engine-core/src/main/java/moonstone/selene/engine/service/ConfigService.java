package moonstone.selene.engine.service;

import moonstone.selene.engine.EngineProperties;

public class ConfigService extends moonstone.selene.service.ConfigService {

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
