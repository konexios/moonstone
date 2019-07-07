package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.service.KronosApplicationProvisioningService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.kronos.web.model.ApplicationModels;
import com.arrow.kronos.web.model.SettingsModels;
import com.arrow.kronos.web.model.SettingsModels.KronosApplicationModel;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;

@RestController
@RequestMapping("/api/kronos/settings")
public class SettingsController extends BaseControllerAbstract {
	@Autowired
	private ClientApplicationApi clientApplicationApi;

	@Autowired
	private KronosApplicationProvisioningService kronosApplicationProvisioningService;

	@Autowired
	private KronosApplicationService kronosApplicationService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_APPLICATION_SETTINGS')")
	@RequestMapping(method = RequestMethod.GET)
	public SettingsModels.SettingsModel get(HttpSession session) {
		Application application = getCoreCacheService().findApplicationById(getApplicationId(session));
		application = getCoreCacheHelper().populateApplication(application);

		SettingsModels.SettingsModel model = new SettingsModels.SettingsModel(application);
		// model.getConfigurations().addAll(SettingsModels.ConfigurationPropertyModel
		// .fromList(application.getRefProduct().getConfigurations(), true));

		return model;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_APPLICATION_SETTINGS')")
	@RequestMapping(value = "/configurations", method = RequestMethod.PUT)
	public List<SettingsModels.ConfigurationPropertyModel> saveConfigurations(
	        @RequestBody List<SettingsModels.ConfigurationPropertyModel> configurations, HttpSession session) {
		Application application = getCoreCacheHelper()
		        .populateApplication(getCoreCacheService().findApplicationById(getApplicationId(session)));

		for (SettingsModels.ConfigurationPropertyModel property : configurations) {
			ConfigurationProperty existingProperty = findConfigurationProperty(application.getConfigurations(),
			        property.getCategory(), property.getDataType(), property.getName());
			if (existingProperty != null) {
				existingProperty.setValue(property.getValue(existingProperty));
			}
		}
		application = getCoreCacheHelper().populateApplication(clientApplicationApi.update(application, getUserId()));

		List<SettingsModels.ConfigurationPropertyModel> configurationModels = SettingsModels.ConfigurationPropertyModel
		        .fromList(application.getConfigurations(), false);
//		configurationModels.addAll(SettingsModels.ConfigurationPropertyModel
//		        .fromList(application.getRefProduct().getConfigurations(), true));

		return configurationModels;
	}

	@PreAuthorize("hasAuthority('KRONOS_PROVISION_APPLICATION')")
	@RequestMapping(value = "/provision", method = RequestMethod.POST)
	public void provisionApplication(HttpSession session) {
		kronosApplicationProvisioningService.provisionApplication(getApplicationId(session), true, getUserId());
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_KRONOS_APPLICATION')")
	@RequestMapping(value = "/kronosapplication", method = RequestMethod.GET)
	public SettingsModels.KronosApplicationUpsertModel kronosApplication(HttpSession session) {
		Assert.notNull(session, "session is null");

		Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
		Assert.notNull(product, "Unable to find product! name=" + ProductSystemNames.KRONOS);
		List<Application> inheritableApplications = clientApplicationApi.findByProductIdAndEnabled(product.getId(),
		        true);

		List<ApplicationModels.ApplicationOption> applicationOptionsModels = new ArrayList<>();
		for (Application app : inheritableApplications) {
			applicationOptionsModels.add(new ApplicationModels.ApplicationOption(app));
		}

		KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
		        .findByApplicationId(getApplicationId(session));
		Assert.notNull(kronosApplication, "kronosApplication was not found");

		KronosApplicationModel kronosApplicationModel = populateKronosApplicationModel(kronosApplication);
		SettingsModels.KronosApplicationUpsertModel model = new SettingsModels.KronosApplicationUpsertModel();
		model.setKronosApplication(kronosApplicationModel);
		model.setInheritableApplications(applicationOptionsModels);

		return model;
	}

	@PreAuthorize("hasAuthority('KRONOS_UPDATE_KRONOS_APPLICATION')")
	@RequestMapping(value = "/kronosapplication", method = RequestMethod.PUT)
	public SettingsModels.KronosApplicationModel updateKronosApplication(
	        @RequestBody SettingsModels.KronosApplicationModel model, HttpSession session) {
		Assert.notNull(model, "model is null");
		Assert.notNull(session, "session is null");

		KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
		        .findByApplicationId(getApplicationId(session));
		Assert.notNull(kronosApplication, "kronosApplication was not found");

		kronosApplication.setAllowCreateGatewayFromDifferentApp(model.isAllowCreateGatewayFromDifferentApp());
		kronosApplication.setLiveTelemetryStreamingEnabled(model.isLiveTelemetryStreamingEnabled());
		kronosApplication.setLiveTelemetryStreamingRetentionSecs(model.getLiveTelemetryStreamingRetentionSecs());
		kronosApplication.setDefaultSoftwareReleaseEmails(model.getDefaultSoftwareReleaseEmails());
		kronosApplication.setPersistTelemetry(model.getPersistTelemetry());
		kronosApplication.setIndexTelemetry(model.getIndexTelemetry());
		kronosApplication.setAllowGatewayTransfer(model.isAllowGatewayTransfer());
		kronosApplication.setTelemetryRetentionDays(model.getTelemetryRetentionDays());
		kronosApplication.setIotProviderLoopback(model.isIotProviderLoopback());

		kronosApplication = kronosApplicationService.update(kronosApplication, getAuthenticatedUser().getPri());

		return populateKronosApplicationModel(kronosApplication);
	}

	private KronosApplicationModel populateKronosApplicationModel(KronosApplication kronosApplication) {
		return new SettingsModels.KronosApplicationModel()
		        .withAllowCreateGatewayFromDifferentApp(kronosApplication.isAllowCreateGatewayFromDifferentApp())
		        .withLiveTelemetryStreamingEnabled(kronosApplication.isLiveTelemetryStreamingEnabled())
		        .withLiveTelemetryStreamingRetentionSecs(kronosApplication.getLiveTelemetryStreamingRetentionSecs())
		        .withDefaultSoftwareReleaseEmails(kronosApplication.getDefaultSoftwareReleaseEmails())
		        .withIndexTelemetry(kronosApplication.getIndexTelemetry())
		        .withPersistTelemetry(kronosApplication.getPersistTelemetry())
		        .withAllowGatewayTransfer(kronosApplication.isAllowGatewayTransfer())
		        .withTelemetryRetentionDays(kronosApplication.getTelemetryRetentionDays())
		        .withIotProviderLoopback(kronosApplication.isIotProviderLoopback());
	}

	private ConfigurationProperty findConfigurationProperty(List<ConfigurationProperty> properties,
	        ConfigurationPropertyCategory category, ConfigurationPropertyDataType dataType, String name) {
		ConfigurationProperty property = null;
		for (ConfigurationProperty existingProperty : properties) {
			if (category == existingProperty.getCategory() && dataType == existingProperty.getDataType()
			        && StringUtils.equals(existingProperty.getName(), name)) {
				property = existingProperty;
				break;
			}
		}

		return property;
	}
}
