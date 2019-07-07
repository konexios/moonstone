package com.arrow.pegasus.web.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.arrow.pegasus.repo.params.SubscriptionSearchParams;
import com.arrow.pegasus.security.CryptoClient128Impl;
import com.arrow.pegasus.security.CryptoClientImpl;
import com.arrow.pegasus.web.model.AccessKeyModelAbstract;
import com.arrow.pegasus.web.model.ApplicationEngineModels;
import com.arrow.pegasus.web.model.ApplicationModels;
import com.arrow.pegasus.web.model.CompanyModels;
import com.arrow.pegasus.web.model.ConfigurationModel;
import com.arrow.pegasus.web.model.ProductModels;
import com.arrow.pegasus.web.model.RegionModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;
import com.arrow.pegasus.web.model.SubscriptionModels;
import com.arrow.pegasus.web.model.ZoneModels;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.KeyValueOption;

@RestController
@RequestMapping("/api/pegasus/applications")
public class ApplicationController extends PegasusControllerAbstract {

	/**
	 * <b>findSubscriptions</b> method limits the subscription options to only
	 * subscriptions for the passed in <code>companyId</code> and it's parent
	 * company subscriptions if it has a parent company
	 * 
	 * @param companyId
	 * @param platformAdmin
	 * @return
	 */
	private List<SubscriptionModels.SubscriptionOption> findSubscriptions(String companyId, boolean platformAdmin) {
		Assert.hasText(companyId, "companyId is empty");

		// Company selfCompany =
		// getCoreCacheService().findCompanyById(companyId);
		// Assert.notNull(selfCompany, "Company not found! companyId=" +
		// companyId);
		//
		// Set<String> companyIdSet = new HashSet<>();
		// companyIdSet.add(selfCompany.getId());
		// if (!StringUtils.isEmpty(selfCompany.getParentCompanyId()))
		// companyIdSet.add(selfCompany.getParentCompanyId());

		// search params
		SubscriptionSearchParams params = new SubscriptionSearchParams();
		params.withCompanyIds(new String[] { companyId });
		if (!platformAdmin)
			params.setEnabled(true);

		// lookup subscriptions
		List<Subscription> subscriptions = getSubscriptionService().getSubscriptionRepository()
				.findSubscriptions(params);

		// convert to models
		List<SubscriptionModels.SubscriptionOption> subscriptionOptions = new ArrayList<>();
		for (Subscription subscription : subscriptions)
			subscriptionOptions.add(new SubscriptionModels.SubscriptionOption(subscription));

		// sort by name
		subscriptionOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return Collections.unmodifiableList(subscriptionOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{companyId}/subscriptions", method = RequestMethod.GET)
	public List<SubscriptionModels.SubscriptionOption> subscriptionOptions(@PathVariable String companyId) {

		return findSubscriptions(companyId, getAuthenticatedUser().isAdmin());
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{parentProductId}/productExtensions", method = RequestMethod.GET)
	public List<ProductModels.ProductOption> productExtensionOptions(@PathVariable String parentProductId) {
		return getProductExtensionOptions(parentProductId, true, false, getAuthenticatedUser().isAdmin());
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{productId}/productFeatures", method = RequestMethod.GET)
	public List<ApplicationModels.ProductFeatureOption> productFeatureOptions(@PathVariable String productId) {
		return getProductFeatureOptions(productId);
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	public ApplicationModels.ApplicationFilterOptions filterOptions() {

		User authenticatedUser = getAuthenticatedUser();

		// company options
		List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		// subscription options
		List<Subscription> subscriptions = new ArrayList<>();
		if (authenticatedUser.isAdmin()) {
			subscriptions = getSubscriptionService().getSubscriptionRepository().findAll();
		} else {
			subscriptions = getSubscriptionService().getSubscriptionRepository()
					.findByCompanyIdAndEnabled(authenticatedUser.getCompanyId(), true);
		}

		List<SubscriptionModels.SubscriptionOption> subscriptionOptions = new ArrayList<>();
		for (Subscription subscription : subscriptions) {
			subscriptionOptions.add(new SubscriptionModels.SubscriptionOption(subscription));
		}

		// region options
		List<Region> regions = new ArrayList<>();
		if (authenticatedUser.isAdmin())
			regions = getRegionService().getRegionRepository().findAll();
		else
			regions = getRegionService().getRegionRepository().findByEnabled(true);

		List<RegionModels.RegionOption> regionOptions = new ArrayList<>();
		for (Region region : regions) {
			regionOptions.add(new RegionModels.RegionOption(region));
		}

		// initially empty
		List<ZoneModels.ZoneOption> zoneOptions = new ArrayList<>();

		List<ProductModels.ProductOption> productOptions = new ArrayList<>();
		if (authenticatedUser.isAdmin()) {
			// TODO revisit, design approach to get products for a non-admin
			for (Product product : getProductService().getProductRepository().findAll()) {
				productOptions.add(new ProductModels.ProductOption(product));
			}
		}

		// api signing required options
		List<YesNoInherit> apiSigningRequiredOptions = Arrays.asList(YesNoInherit.values());

		// enabled options
		List<KeyValueOption> enabledOptions = new ArrayList<>();
		enabledOptions.add(new KeyValueOption("true", "Yes"));
		enabledOptions.add(new KeyValueOption("false", "No"));
		enabledOptions.add(new KeyValueOption("all", "All"));

		return new ApplicationModels.ApplicationFilterOptions().withCompanyOptions(companyOptions)
				.withSubscriptionOptions(subscriptionOptions).withRegionOptions(regionOptions)
				.withZoneOptions(zoneOptions).withProductOptions(productOptions)
				.withApiSigningRequired(apiSigningRequiredOptions).withEnabledOptions(enabledOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_APPLICATION')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.ApplicationSearchResult find(
			@RequestBody SearchFilterModels.ApplicationSearchFilter searchFilter) {

		User authenticatedUser = getAuthenticatedUser();

		// search params
		ApplicationSearchParams params = new ApplicationSearchParams();
		if (authenticatedUser.isAdmin()) {
			params.withCompanyIds(searchFilter.getCompanyIds());
		} else {
			// always enforce user's companyId and child companyIds
			List<String> companyIds = getCompanyIds(authenticatedUser.getCompanyId(),
					EnumSet.allOf(CompanyStatus.class), true, false);
			params.withCompanyIds(companyIds.toArray(new String[companyIds.size()]));
		}

		if (authenticatedUser.isAdmin()) {
			params.withSubscriptionIds(searchFilter.getSubscriptionIds());

			if (!ArrayUtils.isEmpty(searchFilter.getRegionIds())) {
				// TODO need to lookup zone with 1 or more regionIds
				// params.withZoneIds(searchFilter.getZoneIds());
			}
			params.withProductIds(searchFilter.getProductIds());
			params.withProductExtensionIds(searchFilter.getProductExtensionIds());
			params.withApiSigningRequired(searchFilter.getApiSigningRequired());
		}

		if (!searchFilter.getEnabled().equalsIgnoreCase("all")) {
			params.setEnabled(Boolean.valueOf(searchFilter.getEnabled()));
		}
		params.setName(searchFilter.getName());
		params.setCode(searchFilter.getCode());

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// lookup
		Page<Application> applicationPage = getApplicationService().getApplicationRepository()
				.findApplications(pageRequest, params);

		// convert to visual model
		Page<ApplicationModels.ApplicationList> result = null;
		List<ApplicationModels.ApplicationList> applicationModels = new ArrayList<>();
		for (Application application : applicationPage) {
			application = getCoreCacheHelper().populateApplication(application);

			// product extensions
			List<String> productExtensionNames = new ArrayList<>();
			for (Product productExtension : application.getRefProductExtensions())
				productExtensionNames.add(productExtension.getName());

			// product features
			List<String> productFeatureNames = new ArrayList<>();
			if (application.getProductFeatures() != null)
				for (String productFeatureSystemName : application.getProductFeatures()) {
					for (ProductFeature pf : application.getRefProduct().getFeatures()) {
						if (productFeatureSystemName.equals(pf.getSystemName())) {
							productFeatureNames.add(pf.getName());
							break;
						}
					}
				}

			applicationModels.add(new ApplicationModels.ApplicationList(application, application.getRefCompany(),
					application.getRefProduct(),
					(application.getRefZone() != null ? application.getRefZone().getRefRegion() : null),
					application.getRefZone(), productExtensionNames, productFeatureNames));
		}
		result = new PageImpl<>(applicationModels, pageRequest, applicationPage.getTotalElements());

		return new SearchResultModels.ApplicationSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_APPLICATION')")
	@RequestMapping(value = "/{id}/application")
	public ApplicationModels.ApplicationUpsert application(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		User authenticatedUser = getAuthenticatedUser();

		Application application = new Application();
		// default to active only
		EnumSet<CompanyStatus> companyStatuses = EnumSet.of(CompanyStatus.Active);

		// if exists then load application
		if (!id.equalsIgnoreCase("new")) {
			application = getApplicationService().getApplicationRepository().findById(id).orElse(null);
			Assert.notNull(application, "application is null");
			application = getApplicationService().populate(application);
		}

		// company options
		List<CompanyOption> companyOptions = null;
		if (authenticatedUser.isAdmin())
			companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(), EnumSet.allOf(CompanyStatus.class),
					true, authenticatedUser.isAdmin());
		else {
			// can't select own tenant, per demo user can select own tenant
			companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(), companyStatuses, true,
					authenticatedUser.isAdmin());

			// if (application.getRefCompany() != null &&
			// (application.getRefCompany().getStatus() != CompanyStatus.Active
			// ||
			// application.getCompanyId().equals(authenticatedUser.getCompanyId())))
			// companyOptions.add(new
			// CompanyModels.CompanyOption(application.getRefCompany()));
		}

		// sort company options by name
		companyOptions.sort(Comparator.comparing(CompanyModels.CompanyOption::getName, String.CASE_INSENSITIVE_ORDER));

		// subscription options
		List<SubscriptionModels.SubscriptionOption> subscriptionOptions = new ArrayList<>();
		if (!StringUtils.isEmpty(application.getCompanyId()))
			subscriptionOptions = findSubscriptions(application.getCompanyId(), authenticatedUser.isAdmin());

		// product options
		List<ProductModels.ProductOption> parentProductOptions = getParentProductOptions(authenticatedUser.isAdmin());

		// product extension options
		List<ProductModels.ProductOption> productExtensionOptions = new ArrayList<>();
		if (!StringUtils.isEmpty(application.getProductId()))
			productExtensionOptions = getProductExtensionOptions(application.getProductId(), true, false,
					authenticatedUser.isAdmin());

		// product feature options
		List<ApplicationModels.ProductFeatureOption> productFeatureOptions = null;
		if (!StringUtils.isEmpty(application.getProductId())) {
			productFeatureOptions = new ArrayList<>();
			for (ProductFeature productFeature : application.getRefProduct().getFeatures())
				productFeatureOptions.add(new ApplicationModels.ProductFeatureOption(productFeature));
		}

		// region & zone options
		List<ApplicationModels.ApplicationRegionOption> regionOptions = getApplicationRegionOptions(
				authenticatedUser.isAdmin());

		Region region = null;
		if (!StringUtils.isEmpty(application.getZoneId())) {
			Zone zone = getCoreCacheService().findZoneById(application.getZoneId());
			Assert.notNull(zone, "zone is null");

			region = getCoreCacheService().findRegionById(zone.getRegionId());
			Assert.notNull(region, "region is null");
		}

		// zone options
		List<ApplicationModels.ApplicationZoneOption> zoneOptions = null;
		if (region != null)
			zoneOptions = getApplicationZoneOptions(region.getId(), authenticatedUser.isAdmin());
		else if (regionOptions != null && regionOptions.size() > 0)
			zoneOptions = getApplicationZoneOptions(regionOptions.get(0).getId(), authenticatedUser.isAdmin());

		// application engine options
		List<ApplicationEngineModels.ApplicationEngineOption> applicationEngineOptions = getApplicationEngineOptions(
				application.getProductId(), application.getZoneId(), authenticatedUser.isAdmin());

		// api signing required options
		List<ApplicationModels.ApiSigningRequiredOption> apiSigningRequiredOptions = new ArrayList<>();
		for (YesNoInherit yni : YesNoInherit.values())
			apiSigningRequiredOptions.add(new ApplicationModels.ApiSigningRequiredOption(yni));

		// configurations
		List<ConfigurationModel> configurationModels = getApplicationConfigurationModel(application);

		return new ApplicationModels.ApplicationUpsert(
				new ApplicationModels.ApplicationModel(application, region, configurationModels), companyOptions,
				parentProductOptions, productExtensionOptions, productFeatureOptions, subscriptionOptions,
				applicationEngineOptions, apiSigningRequiredOptions, regionOptions, zoneOptions,
				ConfigurationPropertyCategory.values(), ConfigurationPropertyDataType.values());
	}

	private List<ConfigurationModel> getApplicationConfigurationModel(Application application) {
		List<ConfigurationModel> configurationModels = new ArrayList<>();
		for (ConfigurationProperty cp : application.getConfigurations()) {
			configurationModels.add(new ConfigurationModel(cp));
		}
		return configurationModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_APPLICATION')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ApplicationModels.ApplicationModel create(@RequestBody ApplicationModels.ApplicationModel model) {

		Assert.notNull(model, "application is null");

		Application application = getPegasusModelUtil().toApplication(model);

		application = getApplicationService().create(application, getUserId());

		return new ApplicationModels.ApplicationModel(application, null, null);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_APPLICATION')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public ApplicationModels.ApplicationModel update(@RequestBody ApplicationModels.ApplicationModel model) {
		Assert.notNull(model, "product is null");

		Application application = getCoreCacheService().findApplicationById(model.getId());
		Assert.notNull(application, "application not found :: applicationId=[" + model.getId() + "]");

		application = getPegasusModelUtil().toApplication(model, application);

		application = getApplicationService().update(application, getUserId());

		Assert.hasText(application.getZoneId(), "zoneId is empty");

		Zone zone = getCoreCacheService().findZoneById(application.getZoneId());
		Assert.notNull(zone, "zone is null");

		Region region = getCoreCacheService().findRegionById(zone.getRegionId());
		Assert.notNull(region, "region is null");

		List<ConfigurationModel> configurationModels = getApplicationConfigurationModel(application);

		return new ApplicationModels.ApplicationModel(application, region, configurationModels);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_APPLICATION_VAULT_LOGIN')")
	@RequestMapping(path = "/create/vaultlogin", method = RequestMethod.PUT)
	public ApplicationModels.ApplicationModel createVaultLogin(
			@RequestBody ApplicationModels.ApplicationVaultLogin model) {
		Assert.notNull(model, "applicationVaultModel is null");

		Application application = getCoreCacheService().findApplicationById(model.getApplicationId());
		Assert.notNull(application, "application not found :: applicationId=[" + model.getApplicationId() + "]");

		// create vault login
		getApplicationService().createVaultLogin(application, model.getAdminToken());

		// re-lookup application to get the vaultId
		application = getCoreCacheService().findApplicationById(model.getApplicationId());

		// check and create application owner key
		getApplicationService().checkCreateOwnerKey(application, getUserId());

		Zone zone = getCoreCacheService().findZoneById(application.getZoneId());
		Assert.notNull(zone, "zone is null");

		Region region = getCoreCacheService().findRegionById(zone.getRegionId());
		Assert.notNull(region, "region is null");

		List<ConfigurationModel> configurationModels = getApplicationConfigurationModel(application);

		return new ApplicationModels.ApplicationModel(application, region, configurationModels);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_APPLICATION_ACCESS_KEYS')")
	@RequestMapping(value = "{applicationId}/accesskeys", method = RequestMethod.POST)
	public SearchResultModels.AccessKeySearchResult<ApplicationModels.AccessKeyModel> accessKeys(
			@PathVariable String applicationId,
			@RequestBody(required = false) SearchFilterModels.AccessKeySearchFilter searchFilter) {

		Assert.hasText(applicationId, "applicationId is null");

		Application application = getCoreCacheService().findApplicationById(applicationId);
		Assert.isTrue(validateUser(application.getCompanyId()),
				"User must be an admin or the authenticated user's applicationId must match the passed in companyId");

		if (searchFilter == null) {
			searchFilter = new SearchFilterModels.AccessKeySearchFilter();
		}

		AccessKeySearchParams params = getSearchParamsUtil().buildAccessKeyParams(searchFilter);
		params.addApplicationIds(applicationId);
		if (searchFilter.getRelationEntityType()
				.equals(SearchFilterModels.AccessKeySearchFilter.RelationEntityType.MY_KEYS)) {
			params.addPri(application.getPri());
		}

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));
		Page<AccessKey> accessKeyPage = getAccessKeyService().getAccessKeyRepository().findAccessKeys(pageRequest,
				params);

		// convert to visual model
		List<ApplicationModels.AccessKeyModel> accessKeyModels = new ArrayList<>();

		long currentTime = Instant.now().toEpochMilli();
		for (AccessKey accKey : accessKeyPage) {

			if (!StringUtils.isEmpty(accKey.getApplicationId())) {
				application = getCoreCacheService().findApplicationById(accKey.getApplicationId());
			}

			accKey = getPegasusModelUtil().toEncryptedAccessKeyFields(application, accKey);
			final long expTimestamp = accKey.getExpiration().toEpochMilli();
			accessKeyModels.add(new ApplicationModels.AccessKeyModel(accKey).withCompanyId(accKey.getCompanyId())
					.withName(accKey.getName()).withRawApiKey(accKey.getEncryptedApiKey().substring(0, 9).concat("..."))
					.withExpiration(String.valueOf(accKey.getExpiration().toEpochMilli()))
					.withOwnerDisplayName(application.getName()).withIsExpired(expTimestamp < currentTime));
		}

		Page<ApplicationModels.AccessKeyModel> result = new PageImpl<>(accessKeyModels, pageRequest,
				accessKeyPage.getTotalElements());

		return new SearchResultModels.AccessKeySearchResult<>(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_APPLICATION_ACCESS_KEYS')")
	@RequestMapping(value = "{applicationId}/accesskey/{accessKeyId}", method = RequestMethod.GET)
	public ApplicationModels.AccessKeyModel accessKey(@PathVariable String applicationId,
			@PathVariable("accessKeyId") String accessKeyId) {
		Assert.hasText(applicationId, "applicationId is null");
		Assert.hasText(accessKeyId, "accessKeyId is null");

		Application application = getCoreCacheService().findApplicationById(applicationId);

		Assert.isTrue(validateUser(application.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		AccessKey accessKey = getAccessKeyService().getAccessKeyRepository().findById(accessKeyId).orElse(null);
		Assert.notNull(accessKey, "AccessKey not found");

		String rawApiKey = getCryptoService().decrypt(accessKey.isOwner(application) ? null : application.getId(),
				accessKey.getEncryptedApiKey());
		String rawSecretKey = getCryptoService().decrypt(accessKey.isOwner(application) ? null : application.getId(),
				accessKey.getEncryptedSecretKey());

		List<AccessKeyModelAbstract.AccessPrivilegeModel> accessPrivilegeModels = accessKey.getPrivileges().stream()
				.map(accessPrivilege -> new AccessKeyModelAbstract.AccessPrivilegeModel(accessPrivilege.getLevel(),
						accessPrivilege.getPri()))
				.collect(Collectors.toList());

		CryptoClientImpl cryptoClient256 = new CryptoClientImpl();
		CryptoClient128Impl cryptoClient128 = new CryptoClient128Impl();

		return new ApplicationModels.AccessKeyModel(accessKey).withCompanyId(accessKey.getCompanyId())
				.withName(accessKey.getName()).withExpiration(String.valueOf(accessKey.getExpiration()))
				.withAccessPrivilege(accessPrivilegeModels).withRawApiKey(rawApiKey).withRawSecretKey(rawSecretKey)
				.withAes128ApiKey(cryptoClient128.internalEncrypt(rawApiKey))
				.withAes128SecretKey(cryptoClient128.internalEncrypt(rawSecretKey))
				.withAes256ApiKey(cryptoClient256.internalEncrypt(rawApiKey))
				.withAes256SecretKey(cryptoClient256.internalEncrypt(rawSecretKey));
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_APPLICATION_ACCESS_KEY')")
	@RequestMapping(value = "{applicationId}/accesskey", method = RequestMethod.POST)
	public ApplicationModels.AccessKeyModel createAccessKey(@RequestBody ApplicationModels.AccessKeyModel model,
			@PathVariable String applicationId) {

		Assert.notNull(applicationId, "applicationId is null");

		AccessKey accessKey = getPegasusModelUtil().toAccessKey(model, new AccessKey());

		Application application = getApplicationService().getApplicationRepository().findById(applicationId)
				.orElse(null);
		Assert.notNull(application, "application not found");

		accessKey = getAccessKeyService().create(application.getCompanyId(),
				application != null ? application.getSubscriptionId() : null, applicationId, accessKey.getName(),
				accessKey.getExpiration(), accessKey.getPrivileges(), false, getUserId());

		if (!StringUtils.isEmpty(accessKey.getApplicationId())) {
			application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
		}

		ApplicationModels.AccessKeyModel accessKeyModel = new ApplicationModels.AccessKeyModel(accessKey);

		return (ApplicationModels.AccessKeyModel) getPegasusModelUtil().toAccessKeyModel(accessKey, application,
				accessKeyModel);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_APPLICATION_ACCESS_KEY')")
	@RequestMapping(value = "{applicationId}/accesskey", method = RequestMethod.PUT)
	public ApplicationModels.AccessKeyModel updateAccessKey(@RequestBody ApplicationModels.AccessKeyModel model,
			@PathVariable("applicationId") String applicationId) {

		Assert.notNull(applicationId, "applicationId is null");
		Assert.notNull(model.getId(), "accessKeyId is null");

		AccessKey accessKey = getCoreCacheService().findAccessKeyById(model.getId());
		Assert.notNull(accessKey, "accessKey not found :: accessKeyId=[" + accessKey.getId() + "]");

		LocalDate expDate = getPegasusModelUtil().toISOLocalDate(model.getExpiration());
		accessKey.setExpiration(expDate.atStartOfDay().toInstant(ZoneOffset.UTC));
		accessKey = getPegasusModelUtil().toAccessKey(model, accessKey);

		accessKey = getAccessKeyService().update(accessKey, getUserId());

		Application application = null;
		if (!StringUtils.isEmpty(accessKey.getApplicationId())) {
			application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
		}

		ApplicationModels.AccessKeyModel accessKeyModel = new ApplicationModels.AccessKeyModel(accessKey);

		return (ApplicationModels.AccessKeyModel) getPegasusModelUtil().toAccessKeyModel(accessKey, application,
				accessKeyModel);
	}
}