package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Subscription;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.ModelAbstract;

public class SettingsModels {
	public static class SettingsModel implements Serializable {
		private static final long serialVersionUID = 5781153188980206144L;

		private CompanyModel company;
		private SubscriptionModel subscription;
		private ProductModel product;
		private ApplicationModel application;
		private List<ConfigurationPropertyModel> configurations;
		private List<String> productFeatures = new ArrayList<>();

		public SettingsModel(Application application) {
			this.company = new CompanyModel(application.getRefCompany());
			this.subscription = new SubscriptionModel(application.getRefSubscription());
			this.product = new ProductModel(application.getRefProduct());
			this.application = new ApplicationModel(application);
			this.configurations = ConfigurationPropertyModel.fromList(application.getConfigurations(), false);
			this.productFeatures = application.getProductFeatures(); 
		}

		public CompanyModel getCompany() {
			return company;
		}

		public SubscriptionModel getSubscription() {
			return subscription;
		}

		public ProductModel getProduct() {
			return product;
		}

		public ApplicationModel getApplication() {
			return application;
		}

		public List<ConfigurationPropertyModel> getConfigurations() {
			return configurations;
		}
		
		public List<String> getProductFeatures() {
			return productFeatures;
		}
	}

	public static class ApplicationModel implements Serializable {
		private static final long serialVersionUID = 636116767360047517L;

		private String name;
		private String description;
		private String regionName;
		private String zoneName;
		private boolean apiSigningRequired;
		private String code;
		private String applicationId;
		private String applicationHid;

		public ApplicationModel(Application application) {
			this.name = application.getName();
			this.description = application.getDescription();
			if (application.getRefZone() != null) {
				if (application.getRefZone().getRefRegion() != null) {
					this.regionName = application.getRefZone().getRefRegion().getName();
				}
				this.zoneName = application.getRefZone().getName();
			}
			this.apiSigningRequired = application.getApiSigningRequired() == YesNoInherit.INHERIT
			        ? application.getRefProduct().isApiSigningRequired()
			        : (application.getApiSigningRequired() == YesNoInherit.YES);
			this.code = application.getCode();
			this.applicationId = application.getId();
			this.applicationHid = application.getHid();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getRegionName() {
			return regionName;
		}

		public String getZoneName() {
			return zoneName;
		}

		public boolean isApiSigningRequired() {
			return apiSigningRequired;
		}

		public String getCode() {
			return code;
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public String getApplicationHid() {
			return applicationHid;
		}

		public void setApplicationHid(String applicationHid) {
			this.applicationHid = applicationHid;
		}
	}

	public static class ConfigurationPropertyModel implements Serializable {
		private static final long serialVersionUID = 663890823934522391L;

		private ConfigurationPropertyDataType dataType;
		private ConfigurationPropertyCategory category;
		private String name;
		private Object value;
		private boolean readonly;

		public static List<ConfigurationPropertyModel> fromList(List<ConfigurationProperty> properties,
		        boolean readonly) {
			List<ConfigurationPropertyModel> models = new ArrayList<>();
			if (properties != null) {
				for (ConfigurationProperty property : properties) {
					if (property == null || property.getCategory().name().equals("Gateway"))
						continue;
					models.add(new ConfigurationPropertyModel(property, readonly));
				}
			}
			return models;
		}

		public ConfigurationPropertyModel() {
		}

		public ConfigurationPropertyModel(ConfigurationProperty property, boolean readonly) {
			this.dataType = property.getDataType();
			this.category = property.getCategory();
			this.name = property.getName();
			this.value = property.getDataType() == ConfigurationPropertyDataType.Json ? property.jsonValue()
			        : property.getValue();
			this.readonly = readonly;
		}

		public ConfigurationPropertyDataType getDataType() {
			return dataType;
		}

		public void setDataType(ConfigurationPropertyDataType dataType) {
			this.dataType = dataType;
		}

		public ConfigurationPropertyCategory getCategory() {
			return category;
		}

		public void setCategory(ConfigurationPropertyCategory category) {
			this.category = category;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public boolean isReadonly() {
			return readonly;
		}

		public void setReadonly(boolean readonly) {
			this.readonly = readonly;
		}

		public String jsonValue(String jsonClass) {
			String json = JsonUtils.toJson(this.getValue());
			if (!StringUtils.isEmpty(json) && !StringUtils.isEmpty(jsonClass)) {
				// make sure the json can be deserialized to the object of the
				// class passed
				try {
					Object object = JsonUtils.fromJson(json, Class.forName(jsonClass));
					return JsonUtils.toJson(object);
				} catch (ClassNotFoundException e) {
					throw new AcsLogicalException("Invalid jsonClass: " + jsonClass);
				}
			} else {
				return json;
			}
		}

		public String getValue(ConfigurationProperty existingProperty) {
			if (existingProperty.getDataType() == ConfigurationPropertyDataType.Json) {
				return this.jsonValue(existingProperty.getJsonClass());
			} else {
				return this.getValue().toString();
			}
		}
	}

	public static class CompanyModel implements Serializable {
		private static final long serialVersionUID = -7278774647141591410L;

		private String name;
		private String abbrName;

		public CompanyModel(Company company) {
			this.name = company.getName();
			this.abbrName = company.getAbbrName();
		}

		public String getName() {
			return name;
		}

		public String getAbbrName() {
			return abbrName;
		}
	}

	public static class SubscriptionModel implements Serializable {
		private static final long serialVersionUID = 1574796311598930403L;

		private String name;
		private String description;
		private String startDate;
		private String endDate;

		public SubscriptionModel(Subscription subscription) {
			this.name = subscription.getName();
			this.description = subscription.getDescription();
			this.startDate = subscription.getStartDate().toString();
			this.endDate = subscription.getEndDate().toString();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getStartDate() {
			return startDate;
		}

		public String getEndDate() {
			return endDate;
		}
	}

	public static class ProductModel implements Serializable {
		private static final long serialVersionUID = -7563909311264198847L;

		private String name;
		private String description;
		private boolean apiSigningRequired;

		public ProductModel(Product product) {
			this.name = product.getName();
			this.description = product.getDescription();
			this.apiSigningRequired = product.isApiSigningRequired();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public boolean isApiSigningRequired() {
			return apiSigningRequired;
		}
	}

	public static class KronosApplicationModel extends ModelAbstract<KronosApplicationModel> implements Serializable {
		private static final long serialVersionUID = 1244556286683378543L;

		private boolean allowCreateGatewayFromDifferentApp;
		private moonstone.acs.client.model.YesNoInherit persistTelemetry;
		private moonstone.acs.client.model.YesNoInherit indexTelemetry;
		private boolean liveTelemetryStreamingEnabled;
		private long liveTelemetryStreamingRetentionSecs;
		private String defaultSoftwareReleaseEmails;
		private boolean allowGatewayTransfer;
		private int telemetryRetentionDays;
		private boolean iotProviderLoopback;

		@Override
		protected KronosApplicationModel self() {
			return this;
		}

		public KronosApplicationModel withAllowCreateGatewayFromDifferentApp(
		        boolean allowCreateGatewayFromDifferentApp) {
			setAllowCreateGatewayFromDifferentApp(allowCreateGatewayFromDifferentApp);

			return self();
		}

		public KronosApplicationModel withPersistTelemetry(moonstone.acs.client.model.YesNoInherit persistTelemetry) {
			setPersistTelemetry(persistTelemetry);

			return self();
		}

		public KronosApplicationModel withIndexTelemetry(moonstone.acs.client.model.YesNoInherit indexTelemetry) {
			setIndexTelemetry(indexTelemetry);

			return self();
		}

		public KronosApplicationModel withLiveTelemetryStreamingEnabled(boolean liveTelemetryStreamingEnabled) {
			setLiveTelemetryStreamingEnabled(liveTelemetryStreamingEnabled);

			return self();
		}

		public KronosApplicationModel withLiveTelemetryStreamingRetentionSecs(
		        long liveTelemetryStreamingRetentionSecs) {
			setLiveTelemetryStreamingRetentionSecs(liveTelemetryStreamingRetentionSecs);

			return self();
		}

		public KronosApplicationModel withDefaultSoftwareReleaseEmails(String defaultSoftwareReleaseEmails) {
			setDefaultSoftwareReleaseEmails(defaultSoftwareReleaseEmails);

			return self();
		}

		public KronosApplicationModel withAllowGatewayTransfer(boolean allowGatewayTransfer) {
			setAllowGatewayTransfer(allowGatewayTransfer);

			return self();
		}

		public KronosApplicationModel withTelemetryRetentionDays(int telemetryRetentionDays) {
			setTelemetryRetentionDays(telemetryRetentionDays);

			return self();
		}

		public KronosApplicationModel withIotProviderLoopback(boolean iotProviderLoopback) {
			setIotProviderLoopback(iotProviderLoopback);

			return self();
		}

		public boolean isAllowCreateGatewayFromDifferentApp() {
			return allowCreateGatewayFromDifferentApp;
		}

		public void setAllowCreateGatewayFromDifferentApp(boolean allowCreateGatewayFromDifferentApp) {
			this.allowCreateGatewayFromDifferentApp = allowCreateGatewayFromDifferentApp;
		}

		public moonstone.acs.client.model.YesNoInherit getPersistTelemetry() {
			return persistTelemetry;
		}

		public void setPersistTelemetry(moonstone.acs.client.model.YesNoInherit persistTelemetry) {
			this.persistTelemetry = persistTelemetry;
		}

		public moonstone.acs.client.model.YesNoInherit getIndexTelemetry() {
			return indexTelemetry;
		}

		public void setIndexTelemetry(moonstone.acs.client.model.YesNoInherit indexTelemetry) {
			this.indexTelemetry = indexTelemetry;
		}

		public boolean isLiveTelemetryStreamingEnabled() {
			return liveTelemetryStreamingEnabled;
		}

		public void setLiveTelemetryStreamingEnabled(boolean liveTelemetryStreamingEnabled) {
			this.liveTelemetryStreamingEnabled = liveTelemetryStreamingEnabled;
		}

		public long getLiveTelemetryStreamingRetentionSecs() {
			return liveTelemetryStreamingRetentionSecs;
		}

		public void setLiveTelemetryStreamingRetentionSecs(long liveTelemetryStreamingRetentionSecs) {
			this.liveTelemetryStreamingRetentionSecs = liveTelemetryStreamingRetentionSecs;
		}

		public String getDefaultSoftwareReleaseEmails() {
			return defaultSoftwareReleaseEmails;
		}

		public void setDefaultSoftwareReleaseEmails(String defaultSoftwareReleaseEmails) {
			this.defaultSoftwareReleaseEmails = defaultSoftwareReleaseEmails;
		}

		public boolean isAllowGatewayTransfer() {
			return allowGatewayTransfer;
		}

		public void setAllowGatewayTransfer(boolean allowGatewayTransfer) {
			this.allowGatewayTransfer = allowGatewayTransfer;
		}

		public int getTelemetryRetentionDays() {
			return telemetryRetentionDays;
		}

		public void setTelemetryRetentionDays(int telemetryRetentionDays) {
			this.telemetryRetentionDays = telemetryRetentionDays;
		}

		public boolean isIotProviderLoopback() {
			return iotProviderLoopback;
		}

		public void setIotProviderLoopback(boolean iotProviderLoopback) {
			this.iotProviderLoopback = iotProviderLoopback;
		}
	}

	public static class KronosApplicationUpsertModel implements Serializable {
		private static final long serialVersionUID = -4744513297297941528L;

		private KronosApplicationModel kronosApplication;
		private List<ApplicationModels.ApplicationOption> inheritableApplications;

		public KronosApplicationModel getKronosApplication() {
			return kronosApplication;
		}

		public void setKronosApplication(KronosApplicationModel kronosApplication) {
			this.kronosApplication = kronosApplication;
		}

		public List<ApplicationModels.ApplicationOption> getInheritableApplications() {
			return inheritableApplications;
		}

		public void setInheritableApplications(List<ApplicationModels.ApplicationOption> inheritableApplications) {
			this.inheritableApplications = inheritableApplications;
		}
	}
}
