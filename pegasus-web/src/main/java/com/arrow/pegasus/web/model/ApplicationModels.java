package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import org.apache.commons.lang3.StringUtils;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.webapi.data.CoreApplicationModels;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.KeyValueOption;

public class ApplicationModels extends CoreApplicationModels {

	public static class ProductFeatureOption implements Serializable {
		private static final long serialVersionUID = 3481782276121119141L;

		private String name;
		private String value;

		public ProductFeatureOption(ProductFeature productFeature) {
			this.name = productFeature.getName();
			this.value = productFeature.getSystemName();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public static class ApplicationRegionOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -6612347553765967042L;

		public ApplicationRegionOption(Region region) {
			super(region.getId(), region.getHid(), region.getName());
		}
	}

	public static class ApplicationZoneOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -6612347553765967042L;

		public ApplicationZoneOption(Zone zone) {
			super(zone.getId(), zone.getHid(), zone.getName());
		}
	}

	public static class ApplicationFilterOptions implements Serializable {
		private static final long serialVersionUID = -526313989625279350L;

		private List<CompanyModels.CompanyOption> companyOptions;
		private List<SubscriptionModels.SubscriptionOption> subscriptionOptions;
		private List<RegionModels.RegionOption> regionOptions;
		private List<ZoneModels.ZoneOption> zoneOptions;
		private List<ProductModels.ProductOption> productOptions;
		// TODO productExtensions
		private List<YesNoInherit> apiSigningRequiredOptions;
		private List<KeyValueOption> enabledOptions;

		public ApplicationFilterOptions() {
		}

		public List<CompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public void setCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			this.companyOptions = companyOptions;
		}

		public ApplicationFilterOptions withCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			setCompanyOptions(companyOptions);

			return this;
		}

		public List<SubscriptionModels.SubscriptionOption> getSubscriptionOptions() {
			return subscriptionOptions;
		}

		public void setSubscriptionOptions(List<SubscriptionModels.SubscriptionOption> subscriptionOptions) {
			this.subscriptionOptions = subscriptionOptions;
		}

		public ApplicationFilterOptions withSubscriptionOptions(
		        List<SubscriptionModels.SubscriptionOption> subscriptionOptions) {
			setSubscriptionOptions(subscriptionOptions);

			return this;
		}

		public List<RegionModels.RegionOption> getRegionOptions() {
			return regionOptions;
		}

		public void setRegionOptions(List<RegionModels.RegionOption> regionOptions) {
			this.regionOptions = regionOptions;
		}

		public ApplicationFilterOptions withRegionOptions(List<RegionModels.RegionOption> regionOptions) {
			setRegionOptions(regionOptions);

			return this;
		}

		public List<ZoneModels.ZoneOption> getZoneOptions() {
			return zoneOptions;
		}

		public void setZoneOptions(List<ZoneModels.ZoneOption> zoneOptions) {
			this.zoneOptions = zoneOptions;
		}

		public ApplicationFilterOptions withZoneOptions(List<ZoneModels.ZoneOption> zoneOptions) {
			setZoneOptions(zoneOptions);

			return this;
		}

		public List<ProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}

		public void setProductOptions(List<ProductModels.ProductOption> productOptions) {
			this.productOptions = productOptions;
		}

		public ApplicationFilterOptions withProductOptions(List<ProductModels.ProductOption> productOptions) {
			setProductOptions(productOptions);

			return this;
		}

		public List<YesNoInherit> getApiSigningRequiredOptions() {
			return apiSigningRequiredOptions;
		}

		public void setApiSigningRequiredOptions(List<YesNoInherit> apiSigningRequiredOptions) {
			this.apiSigningRequiredOptions = apiSigningRequiredOptions;
		}

		public ApplicationFilterOptions withApiSigningRequired(List<YesNoInherit> apiSigningRequiredOptions) {
			setApiSigningRequiredOptions(apiSigningRequiredOptions);

			return this;
		}

		public List<KeyValueOption> getEnabledOptions() {
			return enabledOptions;
		}

		public void setEnabledOptions(List<KeyValueOption> enabledOptions) {
			this.enabledOptions = enabledOptions;
		}

		public ApplicationFilterOptions withEnabledOptions(List<KeyValueOption> enabledOptions) {
			setEnabledOptions(enabledOptions);

			return this;
		}
	}

	public static class ApplicationList extends CoreDocumentModel {
		private static final long serialVersionUID = -4769265841623675456L;

		private String name;
		private String code;
		private String description;
		private String companyName;
		private String productName;
		private String regionName;
		private String zoneName;
		private boolean enabled;
		private YesNoInherit apiSigningRequired;
		private boolean pendingVaultLogin = true;
		private List<String> productExtensionNames = new ArrayList<>();
		private List<String> productFeatureNames = new ArrayList<>();

		public ApplicationList(Application application, Company company, Product product, Region region, Zone zone,
		        List<String> productExtensionNames, List<String> productFeatureNames) {
			super(application.getId(), application.getHid());

			this.name = application.getName();
			this.code = application.getCode();
			this.description = application.getDescription();
			this.companyName = company != null ? company.getName() : "UNKNOWN (" + application.getCompanyId() + ")";
			this.productName = product != null ? product.getName() : "UNKNOWN (" + application.getProductId() + ")";
			this.regionName = region != null ? region.getName() : "UNKNOWN";
			this.zoneName = zone != null ? zone.getName() : "UNKNOWN (" + application.getZoneId() + ")";
			this.enabled = application.isEnabled();
			this.apiSigningRequired = application.getApiSigningRequired();
			this.pendingVaultLogin = (StringUtils.isEmpty(application.getVaultId()) ? true : false);
			if (productExtensionNames != null)
				this.productExtensionNames = productExtensionNames;
			if (productFeatureNames != null)
				this.productFeatureNames = productFeatureNames;
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public String getCompanyName() {
			return companyName;
		}

		public String getProductName() {
			return productName;
		}

		public String getRegionName() {
			return regionName;
		}

		public String getZoneName() {
			return zoneName;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public YesNoInherit getApiSigningRequired() {
			return apiSigningRequired;
		}

		public boolean isPendingVaultLogin() {
			return pendingVaultLogin;
		}

		public List<String> getProductExtensionNames() {
			return productExtensionNames;
		}

		public List<String> getProductFeatureNames() {
			return productFeatureNames;
		}
	}

	public static class ApplicationModel extends CoreDocumentModel {
		private static final long serialVersionUID = -5258859535787386492L;

		private String name;
		private String description;
		private String regionId;
		private String zoneId;
		private boolean enabled;
		private String companyId;
		private String productId;
		private String subscriptionId;
		private YesNoInherit apiSigningRequired;
		private String applicationEngineId;
		private String defaultSamlEntityId;
		private String code;
		private boolean pendingVaultLogin = true;
		private List<String> productExtensionIds = new ArrayList<>();
		private List<String> productFeatures = new ArrayList<>();
		private List<ConfigurationModel> configurations;

		// TODO configurations

		public ApplicationModel() {
			super(null, null);
		}

		/**
		 * @param application
		 * @param region
		 * @param configurations
		 */
		public ApplicationModel(Application application, Region region, List<ConfigurationModel> configurations) {
			super(application.getId(), application.getHid());
			this.name = application.getName();
			this.description = application.getDescription();
			if (region != null)
				this.regionId = region.getId();
			this.zoneId = application.getZoneId();
			this.enabled = application.isEnabled();
			this.companyId = application.getCompanyId();
			this.productId = application.getProductId();
			this.subscriptionId = application.getSubscriptionId();
			this.apiSigningRequired = application.getApiSigningRequired();
			this.applicationEngineId = application.getApplicationEngineId();
			this.defaultSamlEntityId = application.getDefaultSamlEntityId();
			this.code = application.getCode();
			this.pendingVaultLogin = (StringUtils.isEmpty(application.getVaultId()) ? true : false);
			this.productExtensionIds = application.getProductExtensionIds();
			this.productFeatures = application.getProductFeatures();

			this.configurations = new ArrayList<>();
			if (configurations != null) {
				this.configurations.addAll(configurations);
			}
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getZoneId() {
			return zoneId;
		}

		public String getRegionId() {
			return regionId;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public String getCompanyId() {
			return companyId;
		}

		public String getProductId() {
			return productId;
		}

		public String getSubscriptionId() {
			return subscriptionId;
		}

		public YesNoInherit getApiSigningRequired() {
			return apiSigningRequired;
		}

		public String getApplicationEngineId() {
			return applicationEngineId;
		}

		public String getDefaultSamlEntityId() {
			return defaultSamlEntityId;
		}

		public String getCode() {
			return code;
		}

		public boolean isPendingVaultLogin() {
			return pendingVaultLogin;
		}

		public List<String> getProductExtensionIds() {
			return productExtensionIds;
		}

		public List<String> getProductFeatures() {
			return productFeatures;
		}

		public List<ConfigurationModel> getConfigurations() {
			return configurations;
		}
	}

	public static class ApplicationUpsert implements Serializable {
		private static final long serialVersionUID = 4011808850842089551L;

		private ApplicationModel application;
		private List<CompanyModels.CompanyOption> companyOptions;
		private List<ProductModels.ProductOption> parentProductOptions;
		private List<ProductModels.ProductOption> productExtensionOptions;
		private List<ApplicationModels.ProductFeatureOption> productFeatureOptions;
		private List<SubscriptionModels.SubscriptionOption> subscriptionOptions;
		private List<ApplicationEngineModels.ApplicationEngineOption> applicationEngineOptions;
		private List<ApplicationModels.ApiSigningRequiredOption> apiSigningRequiredOptions;
		private List<ApplicationModels.ApplicationRegionOption> regionOptions;
		private List<ApplicationModels.ApplicationZoneOption> zoneOptions;
		private ConfigurationPropertyCategory[] categoryOptions;
		private ConfigurationPropertyDataType[] dataTypeOptions;

		private ApplicationUpsert() {
			companyOptions = new ArrayList<>();
			parentProductOptions = new ArrayList<>();
			productExtensionOptions = new ArrayList<>();
			productFeatureOptions = new ArrayList<>();
			subscriptionOptions = new ArrayList<>();
			applicationEngineOptions = new ArrayList<>();
			apiSigningRequiredOptions = new ArrayList<>();
			regionOptions = new ArrayList<>();
			zoneOptions = new ArrayList<>();
		}

		public ApplicationUpsert(ApplicationModel application, List<CompanyModels.CompanyOption> companyOptions,
		        List<ProductModels.ProductOption> parentProductOptions,
		        List<ProductModels.ProductOption> productExtensionOptions,
		        List<ApplicationModels.ProductFeatureOption> productFeatureOptions,
		        List<SubscriptionModels.SubscriptionOption> subscriptionOptions,
		        List<ApplicationEngineModels.ApplicationEngineOption> applicationEngineOptions,
		        List<ApplicationModels.ApiSigningRequiredOption> apiSigningRequiredOptions,
		        List<ApplicationModels.ApplicationRegionOption> regionOptions,
		        List<ApplicationModels.ApplicationZoneOption> zoneOptions,
		        ConfigurationPropertyCategory[] categoryOptions, ConfigurationPropertyDataType[] dataTypeOptions) {
			this();
			this.application = application;
			if (companyOptions != null)
				this.companyOptions = companyOptions;
			if (parentProductOptions != null)
				this.parentProductOptions = parentProductOptions;
			if (productExtensionOptions != null)
				this.productExtensionOptions = productExtensionOptions;
			if (productFeatureOptions != null)
				this.productFeatureOptions = productFeatureOptions;
			if (subscriptionOptions != null)
				this.subscriptionOptions = subscriptionOptions;
			if (applicationEngineOptions != null)
				this.applicationEngineOptions = applicationEngineOptions;
			if (apiSigningRequiredOptions != null)
				this.apiSigningRequiredOptions = apiSigningRequiredOptions;
			if (regionOptions != null)
				this.regionOptions = regionOptions;
			if (zoneOptions != null)
				this.zoneOptions = zoneOptions;
			if (categoryOptions != null)
				this.categoryOptions = categoryOptions;
			if (dataTypeOptions != null)
				this.dataTypeOptions = dataTypeOptions;
		}

		public ApplicationModel getApplication() {
			return application;
		}

		public List<CompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public List<ProductModels.ProductOption> getParentProductOptions() {
			return parentProductOptions;
		}

		public List<ProductModels.ProductOption> getProductExtensionOptions() {
			return productExtensionOptions;
		}

		public List<ApplicationModels.ProductFeatureOption> getProductFeatureOptions() {
			return productFeatureOptions;
		}

		public List<SubscriptionModels.SubscriptionOption> getSubscriptionOptions() {
			return subscriptionOptions;
		}

		public List<ApplicationEngineModels.ApplicationEngineOption> getApplicationEngineOptions() {
			return applicationEngineOptions;
		}

		public List<ApplicationModels.ApiSigningRequiredOption> getApiSigningRequiredOptions() {
			return apiSigningRequiredOptions;
		}

		public List<ApplicationModels.ApplicationRegionOption> getRegionOptions() {
			return regionOptions;
		}

		public List<ApplicationModels.ApplicationZoneOption> getZoneOptions() {
			return zoneOptions;
		}

		public ConfigurationPropertyCategory[] getCategoryOptions() {
			return categoryOptions;
		}

		public ConfigurationPropertyDataType[] getDataTypeOptions() {
			return dataTypeOptions;
		}
	}

	public static class ApplicationVaultLogin implements Serializable {
		private static final long serialVersionUID = -2080675050868302310L;

		private String applicationId;
		private String adminToken;

		public ApplicationVaultLogin() {
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public String getAdminToken() {
			return adminToken;
		}

		public void setAdminToken(String adminToken) {
			this.adminToken = adminToken;
		}
	}

	public static class AccessKeyModel extends AccessKeyModelAbstract<AccessKeyModel> {
		private static final long serialVersionUID = -629933197479551267L;

		private String applicationId;

		@Override
		protected AccessKeyModel self() {
			return this;
		}

		public AccessKeyModel withApplicationId(String applicationId) {
			setApplicationId(applicationId);
			return self();
		}

		public AccessKeyModel() {
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public AccessKeyModel(AccessKey accessKey) {
			super(accessKey);
		}
	}
}
