package com.arrow.pegasus.web.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;
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
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.arrow.pegasus.repo.params.SubscriptionSearchParams;
import com.arrow.pegasus.security.CryptoClient128Impl;
import com.arrow.pegasus.security.CryptoClientImpl;
import com.arrow.pegasus.web.model.AccessKeyModelAbstract;
import com.arrow.pegasus.web.model.CompanyModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;
import com.arrow.pegasus.web.model.SubscriptionModels;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;
import com.arrow.pegasus.webapi.data.KeyValueOption;

@RestController
@RequestMapping("/api/pegasus/subscriptions")
public class SubscriptionController extends PegasusControllerAbstract {

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{enabled}/options", method = RequestMethod.GET)
	public List<SubscriptionModels.SubscriptionOption> options(@PathVariable Boolean enabled) {
		return getSubscriptionOptions(null, enabled);
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	public SubscriptionModels.SubscriptionFilterOptions filterOptions() {

		User authenticatedUser = getAuthenticatedUser();

		// company options
		List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		// enabled options
		List<KeyValueOption> enabledOptions = new ArrayList<>();
		enabledOptions.add(new KeyValueOption("true", "Yes"));
		enabledOptions.add(new KeyValueOption("false", "No"));
		enabledOptions.add(new KeyValueOption("all", "All"));

		return new SubscriptionModels.SubscriptionFilterOptions().withCompanyOptions(companyOptions)
				.withEnabledOptions(enabledOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_SUBSCRIPTION')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.SubscriptionSearchResult find(
			@RequestBody SearchFilterModels.SubscriptionSearchFilter searchFilter) {

		User authenticatedUser = getAuthenticatedUser();

		// search params
		SubscriptionSearchParams params = new SubscriptionSearchParams();
		if (authenticatedUser.isAdmin()) {
			params.withCompanyIds(searchFilter.getCompanyIds());
		} else {
			// always enforce user's companyId and child companyIds
			List<String> companyIds = getCompanyIds(authenticatedUser.getCompanyId(),
					EnumSet.allOf(CompanyStatus.class), true, false);
			params.withCompanyIds(companyIds.toArray(new String[companyIds.size()]));
		}

		if (!searchFilter.getEnabled().equalsIgnoreCase("all")) {
			params.setEnabled(Boolean.valueOf(searchFilter.getEnabled()));
		}
		params.setName(searchFilter.getName());

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// lookup
		Page<Subscription> subscriptionPage = getSubscriptionService().getSubscriptionRepository()
				.findSubscriptions(pageRequest, params);

		// convert to visual model
		Page<SubscriptionModels.SubscriptionList> result = null;
		List<SubscriptionModels.SubscriptionList> subscriptionModels = new ArrayList<>();
		for (Subscription subscription : subscriptionPage) {
			getSubscriptionService().populateRefs(subscription);
			subscriptionModels.add(new SubscriptionModels.SubscriptionList(subscription, subscription.getRefCompany()));
		}
		result = new PageImpl<>(subscriptionModels, pageRequest, subscriptionPage.getTotalElements());

		return new SearchResultModels.SubscriptionSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_SUBSCRIPTION')")
	@RequestMapping(value = "/{id}/subscription")
	public SubscriptionModels.SubscriptionUpsert subscription(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		Subscription subscription = new Subscription();
		String startDate = null;
		String endDate = null;

		// default to active only
		EnumSet<CompanyStatus> companyStatuses = EnumSet.of(CompanyStatus.Active);

		if (!id.equalsIgnoreCase("new")) {
			subscription = getSubscriptionService().getSubscriptionRepository().findById(id).orElse(null);
			Assert.notNull(subscription, "subscription is null");
			Assert.isTrue(validateUser(subscription.getCompanyId()),
					"The user must be an administrator or the user's tenant or sub-tenants must match the tenant associated to the subscription.");
			subscription = getSubscriptionService().populateRefs(subscription);

			startDate = getPegasusModelUtil().toISOLocalDateString(subscription.getStartDate());
			endDate = getPegasusModelUtil().toISOLocalDateString(subscription.getEndDate());
		}

		User authenticatedUser = getAuthenticatedUser();

		// company options
		List<CompanyOption> companyOptions = null;
		if (authenticatedUser.isAdmin())
			companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(), EnumSet.allOf(CompanyStatus.class),
					true, authenticatedUser.isAdmin());
		else {
			// can't select own tenant
			companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(), companyStatuses, false,
					authenticatedUser.isAdmin());

			if (subscription.getRefCompany() != null
					&& (subscription.getRefCompany().getStatus() != CompanyStatus.Active
							|| subscription.getCompanyId().equals(authenticatedUser.getCompanyId())))
				companyOptions.add(new CompanyModels.CompanyOption(subscription.getRefCompany()));
		}

		// sort company options by name
		companyOptions.sort(Comparator.comparing(CompanyModels.CompanyOption::getName, String.CASE_INSENSITIVE_ORDER));

		return new SubscriptionModels.SubscriptionUpsert(
				new SubscriptionModels.SubscriptionModel(subscription, startDate, endDate), companyOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_SUBSCRIPTION')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public SubscriptionModels.SubscriptionModel create(@RequestBody SubscriptionModels.SubscriptionModel model) {
		Assert.notNull(model, "subscription is null");
		Assert.hasText(model.getStartDate(), "startDate is empty");
		Assert.hasText(model.getEndDate(), "endDate is empty");

		Subscription subscription = getPegasusModelUtil().toSubscription(model);

		LocalDate startDate = getPegasusModelUtil().toISOLocalDate(model.getStartDate());
		LocalDate endDate = getPegasusModelUtil().toISOLocalDate(model.getEndDate());

		subscription.setStartDate(startDate.atStartOfDay().toInstant(ZoneOffset.UTC));
		subscription.setEndDate(endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));

		subscription = getSubscriptionService().create(subscription, getUserId());

		return new SubscriptionModels.SubscriptionModel(subscription,
				getPegasusModelUtil().toISOLocalDateString(subscription.getStartDate()),
				getPegasusModelUtil().toISOLocalDateString(subscription.getEndDate()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_SUBSCRIPTION')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public SubscriptionModels.SubscriptionModel update(@RequestBody SubscriptionModels.SubscriptionModel model) {
		Assert.notNull(model, "subscription is null");
		Assert.hasText(model.getStartDate(), "startDate is empty");
		Assert.hasText(model.getEndDate(), "endDate is empty");

		Subscription subscription = getCoreCacheService().findSubscriptionById(model.getId());
		Assert.notNull(subscription, "subscription not found :: subscriptionId=[" + model.getId() + "]");

		subscription = getPegasusModelUtil().toSubscription(model, subscription);

		LocalDate startDate = getPegasusModelUtil().toISOLocalDate(model.getStartDate());
		LocalDate endDate = getPegasusModelUtil().toISOLocalDate(model.getEndDate());

		subscription.setStartDate(startDate.atStartOfDay().toInstant(ZoneOffset.UTC));
		subscription.setEndDate(endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));

		subscription = getSubscriptionService().update(subscription, getUserId());

		return new SubscriptionModels.SubscriptionModel(subscription,
				getPegasusModelUtil().toISOLocalDateString(subscription.getStartDate()),
				getPegasusModelUtil().toISOLocalDateString(subscription.getEndDate()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_SUBSCRIPTION_APPLICATIONS')")
	@RequestMapping(value = "{id}/applications", method = RequestMethod.GET)
	public List<SubscriptionModels.ApplicationModel> applications(@PathVariable String id) {
		Assert.hasText(id, "subscriptionId is null");

		Subscription subscription = getCoreCacheService().findSubscriptionById(id);
		Assert.notNull(subscription, "subscription not found :: subscriptionId=[" + id + "]");

		Assert.isTrue(validateUser(subscription.getCompanyId()),
				"User must be an admin or authenticated user's companyId must match the companyId associated to the subscription");

		List<Application> applications = getApplicationService().getApplicationRepository().findBySubscriptionId(id);
		List<SubscriptionModels.ApplicationModel> applicationModels = new ArrayList<>();

		applications.forEach(appl -> {
			appl = getCoreCacheHelper().populateApplication(appl);

			List<String> productExtensionNames = new ArrayList<>();
			appl.getRefProductExtensions().forEach(pe -> productExtensionNames.add(pe.getName()));

			applicationModels.add(new SubscriptionModels.ApplicationModel().withId(appl.getId()).withHid(appl.getHid())
					.withName(appl.getName()).withDescription(appl.getDescription()).withCode(appl.getCode())
					.withEnabled(appl.isEnabled()).withProductName(appl.getRefProduct().getName())
					.withProductExtensionNames(productExtensionNames));
		});

		return applicationModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_SUBSCRIPTION_ACCESS_KEYS')")
	@RequestMapping(value = "{subscriptionId}/accesskeys", method = RequestMethod.POST)
	public SearchResultModels.AccessKeySearchResult<SubscriptionModels.AccessKeyModel> accessKeys(
			@PathVariable String subscriptionId,
			@RequestBody(required = false) SearchFilterModels.AccessKeySearchFilter searchFilter) {

		Assert.hasText(subscriptionId, "subscriptionId is null");

		Subscription subscription = getCoreCacheService().findSubscriptionById(subscriptionId);
		Assert.notNull(subscription, "subscription not found :: subscriptionId=[" + subscriptionId + "]");

		Assert.isTrue(validateUser(subscription.getCompanyId()),
				"User must be an admin or authenticated user's companyId must match the companyId associated to the subscription");

		if (searchFilter == null) {
			searchFilter = new SearchFilterModels.AccessKeySearchFilter();
		}

		AccessKeySearchParams params = getSearchParamsUtil().buildAccessKeyParams(searchFilter);
		params.addSubscriptionIds(subscriptionId);
		params.addPri(subscription.getPri());

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		Page<AccessKey> accessKeyPage = getAccessKeyService().getAccessKeyRepository().findAccessKeys(pageRequest,
				params);

		// convert to visual model
		List<SubscriptionModels.AccessKeyModel> accessKeyModels = new ArrayList<>();

		Application application = null;

		long currentTime = Instant.now().toEpochMilli();
		for (AccessKey accKey : accessKeyPage) {
			if (!StringUtils.isEmpty(accKey.getApplicationId())) {
				application = getCoreCacheService().findApplicationById(accKey.getApplicationId());
			}

			accKey = getPegasusModelUtil().toEncryptedAccessKeyFields(application, accKey);

			final long expTimestamp = accKey.getExpiration().toEpochMilli();
			accessKeyModels.add(new SubscriptionModels.AccessKeyModel(accKey).withCompanyId(accKey.getCompanyId())
					.withName(accKey.getName()).withRawApiKey(accKey.getEncryptedApiKey().substring(0, 9).concat("..."))
					.withExpiration(String.valueOf(accKey.getExpiration().toEpochMilli()))
					.withOwnerDisplayName(subscription.getName()).withIsExpired(expTimestamp < currentTime));
		}

		Page<SubscriptionModels.AccessKeyModel> result = new PageImpl<>(accessKeyModels, pageRequest,
				accessKeyPage.getTotalElements());

		return new SearchResultModels.AccessKeySearchResult<>(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_SUBSCRIPTION_ACCESS_KEYS')")
	@RequestMapping(value = "{subscriptionId}/accesskey/{accessKeyId}", method = RequestMethod.GET)
	public SubscriptionModels.AccessKeyModel accessKey(@PathVariable String subscriptionId,
			@PathVariable("accessKeyId") String accessKeyId) {

		Assert.hasText(subscriptionId, "subscriptionId is null");
		Assert.hasText(accessKeyId, "accessKeyId is null");

		Subscription subscription = getCoreCacheService().findSubscriptionById(subscriptionId);

		Assert.isTrue(validateUser(subscription.getCompanyId()),
				"User must be an admin or the subscriptionId user's companyId must match the passed in subscriptionId");

		AccessKey accessKey = getAccessKeyService().getAccessKeyRepository().findById(accessKeyId).orElse(null);
		Assert.notNull(accessKey, "AccessKey not found");

		Application application = null;
		if (!StringUtils.isEmpty(accessKey.getApplicationId())) {
			application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
		}

		String rawApiKey = getCryptoService().decrypt(accessKey.getEncryptedApiKey());
		String rawSecretKey = getCryptoService().decrypt(accessKey.getEncryptedSecretKey());

		List<AccessKeyModelAbstract.AccessPrivilegeModel> accessPrivilegeModels = accessKey.getPrivileges().stream()
				.map(accessPrivilege -> new AccessKeyModelAbstract.AccessPrivilegeModel(accessPrivilege.getLevel(),
						accessPrivilege.getPri()))
				.collect(Collectors.toList());

		CryptoClientImpl cryptoClient256 = new CryptoClientImpl();
		CryptoClient128Impl cryptoClient128 = new CryptoClient128Impl();

		return new SubscriptionModels.AccessKeyModel(accessKey).withCompanyId(accessKey.getCompanyId())
				.withName(accessKey.getName()).withRawApiKey(rawApiKey).withRawSecretKey(rawSecretKey)
				.withExpiration(String.valueOf(accessKey.getExpiration())).withAccessPrivilege(accessPrivilegeModels)
				.withAes128ApiKey(cryptoClient128.internalEncrypt(rawApiKey))
				.withAes128SecretKey(cryptoClient128.internalEncrypt(rawSecretKey))
				.withAes256ApiKey(cryptoClient256.internalEncrypt(rawApiKey))
				.withAes256SecretKey(cryptoClient256.internalEncrypt(rawSecretKey));
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_SUBSCRIPTION_ACCESS_KEY')")
	@RequestMapping(value = "{subscriptionId}/accesskey", method = RequestMethod.POST)
	public List<SubscriptionModels.AccessKeyModel> createAccessKey(@RequestBody SubscriptionModels.AccessKeyModel model,
			@PathVariable String subscriptionId) {

		throw new NotImplementedException();
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_SUBSCRIPTION_ACCESS_KEY')")
	@RequestMapping(value = "{subscriptionId}/accesskey", method = RequestMethod.PUT)
	public SubscriptionModels.AccessKeyModel updateAccessKey(@RequestBody SubscriptionModels.AccessKeyModel model,
			@PathVariable String subscriptionId) {

		Assert.notNull(subscriptionId, "subscriptionId is null");
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

		SubscriptionModels.AccessKeyModel accessKeyModel = new SubscriptionModels.AccessKeyModel(accessKey);

		return (SubscriptionModels.AccessKeyModel) getPegasusModelUtil().toAccessKeyModel(accessKey, application,
				accessKeyModel);
	}
}
