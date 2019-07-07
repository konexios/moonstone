package com.arrow.pegasus.util;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;

@Component
public class CoreConfigurationPropertyUtil extends Loggable {

	public static final String CATEGORY_META_DATA = "";
	public static final String DEVICE_TAGS = "deviceTags";

	public CoreConfigurationPropertyUtil() {
		logInfo(getClass().getSimpleName(), "...");
	}

	protected boolean isMatch(ConfigurationProperty property, ConfigurationPropertyCategory category,
	        String propertyName) {
		Assert.notNull(property, "property is null");
		Assert.notNull(category, "category is null");
		Assert.hasText(propertyName, "propertyName is empty");

		boolean result = false;
		if (property.getCategory().equals(category) && property.getName().equalsIgnoreCase(propertyName))
			result = true;

		return result;
	}

	public ConfigurationProperty getConfigurationProperty(List<ConfigurationProperty> properties,
	        ConfigurationPropertyCategory category, String propertyName) {
		Assert.notNull(properties, "properties are null");
		Assert.notNull(category, "category is null");
		Assert.hasText(propertyName, "propertyName is empty");

		String method = "getConfigurationProperty.Properties";
		logInfo(method, "category: %s, propertyName: %s", category.name(), propertyName);

		ConfigurationProperty result = null;
		for (ConfigurationProperty property : properties) {
			if (isMatch(property, category, propertyName)) {
				result = property;
				break;
			}
		}

		return result;
	}

	public ConfigurationProperty getConfigurationProperty(Product product, ConfigurationPropertyCategory category,
	        String propertyName) {
		Assert.notNull(product, "product is null");
		Assert.notNull(category, "category is null");
		Assert.hasText(propertyName, "propertyName is empty");

		String method = "getConfigurationProperty.Product";
		logInfo(method, "product: %s, category: %s, propertyName: %s", product.getName(), category.name(),
		        propertyName);

		ConfigurationProperty configurationProperty = getConfigurationProperty(product.getConfigurations(), category,
		        propertyName);
		Assert.notNull(configurationProperty, "Product does not have a configuration for " + propertyName + ".");

		return configurationProperty;
	}

	public ConfigurationProperty getConfigurationProperty(Application application,
	        ConfigurationPropertyCategory category, String propertyName, boolean defaultToProduct) {
		Assert.notNull(application, "application is null");
		Assert.notNull(category, "category is null");
		Assert.hasText(propertyName, "propertyName is empty");

		String method = "getConfigurationProperty.Application";
		logInfo(method, "application: %s, category: %s, propertyName: %s", application.getName(), category.name(),
		        propertyName);

		ConfigurationProperty configurationProperty = getConfigurationProperty(application.getConfigurations(),
		        category, propertyName);

		if (configurationProperty == null && defaultToProduct) {
			Assert.notNull(application.getRefProduct(), "RefProduct must be looked up!");
			configurationProperty = getConfigurationProperty(application.getRefProduct(), category, propertyName);
		}

		return configurationProperty;
	}

	public ConfigurationProperty getDeviceTags(Application application) {
		return getConfigurationProperty(application, ConfigurationPropertyCategory.MetaData, DEVICE_TAGS, false);
	}
}
