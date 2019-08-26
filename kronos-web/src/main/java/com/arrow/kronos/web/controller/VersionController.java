package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosCoreCloudVersion;
import com.arrow.kronos.KronosCoreVersion;
import com.arrow.kronos.web.KronosWebConstants;
import com.arrow.kronos.web.KronosWebVersion;
import com.arrow.pegasus.PegasusCoreDbVersion;
import com.arrow.pegasus.PegasusCoreHubVersion;
import com.arrow.pegasus.PegasusCoreVersion;
import com.arrow.pegasus.PegasusCoreWebVersion;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.PegasusPrivateClientVersion;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;
import com.arrow.rhea.RheaCoreVersion;
import com.arrow.rhea.client.api.RheaPrivateClientVersion;

import moonstone.acn.AcnCoreVersion;
import moonstone.acn.client.AcnClientVersion;
import moonstone.acs.AcsCoreVersion;
import moonstone.acs.client.api.AcsClientVersion;
import moonstone.acs.client.model.VersionAndLibraryModel;
import moonstone.acs.client.model.VersionModel;

@RestController
@RequestMapping("/api/kronos/version")
public class VersionController extends ControllerAbstract {

	@Autowired
	private CoreConfigurationPropertyUtil coreConfigurationPropertyUtil;

	public VersionController() {
		logInfo(getClass().getSimpleName(), "...");
	}


	@RequestMapping("/webapp")
	public VersionModel version() {
		return KronosWebVersion.get();
	}

	@RequestMapping("/components")
	public VersionAndLibraryModel componentVersion() {

		VersionAndLibraryModel model = new VersionAndLibraryModel().withVersionModel(KronosWebVersion.get());

		List<VersionModel> libraries = new ArrayList<>();
		libraries.add(AcnClientVersion.get());
		libraries.add(AcnCoreVersion.get());
		libraries.add(AcsCoreVersion.get());
		libraries.add(AcsClientVersion.get());
		libraries.add(KronosCoreVersion.get());
		libraries.add(KronosCoreCloudVersion.get());
		libraries.add(PegasusCoreVersion.get());
		libraries.add(PegasusCoreDbVersion.get());
		libraries.add(PegasusCoreHubVersion.get());
		libraries.add(PegasusCoreWebVersion.get());
		libraries.add(PegasusPrivateClientVersion.get());
		libraries.add(RheaCoreVersion.get());
		libraries.add(RheaPrivateClientVersion.get());

		Collections.sort(libraries, new Comparator<VersionModel>() {

			@Override
			public int compare(VersionModel o1, VersionModel o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		model.withLibraries(libraries);

		return model;
	}

	@RequestMapping("/settings")
	public Map<String, String> settings() {
		Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
		Assert.notNull(product, "Unable to find product! systemName=" + ProductSystemNames.KRONOS);

		Map<String, String> settingsMap = new HashMap<>();

		ConfigurationProperty descriptionProperty = getProductConfiguration(product,
		        ConfigurationPropertyCategory.Settings,
		        KronosWebConstants.CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_DESCRIPTION);

		ConfigurationProperty metricsProperty = getProductConfiguration(product, ConfigurationPropertyCategory.Settings,
		        KronosWebConstants.CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_METRICS);

		ConfigurationProperty eulaProperty = getProductConfiguration(product, ConfigurationPropertyCategory.Settings,
		        KronosWebConstants.CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_EULA);

		settingsMap.put(KronosWebConstants.CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_DESCRIPTION,
		        descriptionProperty.getValue());
		settingsMap.put(KronosWebConstants.CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_METRICS, metricsProperty.getValue());
		settingsMap.put(KronosWebConstants.CONFIGURATION_NAME_LOGIN_SCREEN_DEV_REG_EULA, eulaProperty.getValue());

		return settingsMap;
	}

	private ConfigurationProperty getProductConfiguration(Product product, ConfigurationPropertyCategory category,
	        String configurationPropertyName) {
		Assert.notNull(product, "product is null");
		Assert.notNull(category, "category is null");
		Assert.hasText(configurationPropertyName, "configurationPropertyName is empty");

		ConfigurationProperty productConfiguration = coreConfigurationPropertyUtil.getConfigurationProperty(product,
		        category, configurationPropertyName);
		Assert.notNull(productConfiguration, "Unable to find product configuration! category="
		        + ConfigurationPropertyCategory.Settings + " name=" + configurationPropertyName);

		return productConfiguration;
	}
}
