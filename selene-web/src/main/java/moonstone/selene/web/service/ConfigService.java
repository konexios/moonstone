package moonstone.selene.web.service;

import org.apache.commons.beanutils.BeanUtils;

import moonstone.selene.web.WebProperties;

public class ConfigService extends moonstone.selene.service.ConfigService {

	private static boolean configServiceCreated = false;

	private static class SingletonHolder {
		private static final ConfigService SINGLETON = new ConfigService();
	}

	public static ConfigService getInstance() {
		if (configServiceCreated) {
			return SingletonHolder.SINGLETON;
		}
		return null;
	}

	public static ConfigService createInstance() {
		return SingletonHolder.SINGLETON;
	}

	private WebProperties webProperties;

	private ConfigService() {
		super();
		String method = "ConfigService";
		logDebug(method, "...");
		try {
			webProperties = new WebProperties();
			BeanUtils.populate(webProperties, getConfig());
			// backward compatible
			BeanUtils.populate(webProperties, filterConfig("web"));
			BeanUtils.populate(webProperties, filterConfig("engine"));
		} catch (Exception e) {
			logError(method, "error populating web properties", e);
		}
		configServiceCreated = true;
		logDebug(method, "done!");
	}

	public WebProperties getWebProperties() {
		return webProperties;
	}
}
