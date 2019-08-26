package moonstone.selene.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import moonstone.selene.SeleneConstants;
import moonstone.selene.SeleneProperties;

public class ConfigService extends ServiceAbstract {

    private Map<String, String> config = new HashMap<>();
    private SeleneProperties seleneProperties;

    private static class SingletonHolder {
        static final ConfigService SINGLETON = new ConfigService();
    }

    public static ConfigService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    protected ConfigService() {
        String method = "ConfigService";
        try (InputStream is = getInputStream()) {
            Properties properties = new Properties();
            properties.load(is);
            properties.stringPropertyNames().forEach(name -> config.put(name, properties.getProperty(name)));
            logInfo(method, "selene.properties loaded successfully");

            // init selene specific properties
            seleneProperties = new SeleneProperties().populateFrom(config).populateFrom(filterConfig("selene"));
        } catch (Exception e) {
            logError(method, "ERROR loading selene.properties", e);
            System.exit(1);
        }
    }

    private InputStream getInputStream() throws FileNotFoundException {
        String method = "getInputStream";
        InputStream is;
        String filePath = System.getProperty(SeleneConstants.ENV_SELENE_CONFIG);
        if (StringUtils.isEmpty(filePath)) {
            logWarn(method, "WARNING!!!!!!!!! environment variable %s is not set, loading %s ...",
                    SeleneConstants.ENV_SELENE_CONFIG, SeleneConstants.DEFAULT_SELENE_CONFIG);
            is = ConfigService.class.getResourceAsStream(SeleneConstants.DEFAULT_SELENE_CONFIG);
        } else {
            is = new FileInputStream(filePath);
        }
        return is;
    }

    public SeleneProperties getSeleneProperties() {
        return seleneProperties;
    }

    protected Map<String, String> filterConfig(String prefix) {
        Map<String, String> result = new HashMap<>();
        config.forEach((key, value) -> {
            if (key.startsWith(prefix)) {
                String name = key.substring(prefix.length() + 1);
                result.put(name, value);
            }
        });
        return result;
    }

    protected Map<String, String> getConfig() {
        return config;
    }
}
