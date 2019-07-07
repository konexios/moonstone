package com.arrow.pegasus.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.repo.ApplicationEngineSearchParams;
import com.arrow.pegasus.repo.ProductSearchParams;
import com.arrow.pegasus.repo.ZoneSearchParams;
import com.arrow.pegasus.repo.params.CompanySearchParams;
import com.arrow.pegasus.repo.params.PrivilegeSearchParams;
import com.arrow.pegasus.repo.params.SubscriptionSearchParams;
import com.arrow.pegasus.security.SecurityService;
import com.arrow.pegasus.service.AccessKeyService;
import com.arrow.pegasus.service.ApplicationEngineService;
import com.arrow.pegasus.service.ApplicationService;
import com.arrow.pegasus.service.AuthService;
import com.arrow.pegasus.service.CompanyService;
import com.arrow.pegasus.service.PrivilegeService;
import com.arrow.pegasus.service.ProductService;
import com.arrow.pegasus.service.RegionService;
import com.arrow.pegasus.service.RoleService;
import com.arrow.pegasus.service.SubscriptionService;
import com.arrow.pegasus.service.UserService;
import com.arrow.pegasus.service.ZoneService;
import com.arrow.pegasus.web.model.ApplicationEngineModels;
import com.arrow.pegasus.web.model.ApplicationModels;
import com.arrow.pegasus.web.model.CompanyModels;
import com.arrow.pegasus.web.model.PegasusModelUtil;
import com.arrow.pegasus.web.model.PrivilegeModels;
import com.arrow.pegasus.web.model.ProductModels;
import com.arrow.pegasus.web.model.RoleModels;
import com.arrow.pegasus.web.model.SubscriptionModels;
import com.arrow.pegasus.web.model.UserModels;
import com.arrow.pegasus.web.model.ZoneModels;
import com.arrow.pegasus.web.util.SearchParamsUtil;
import com.arrow.pegasus.webapi.WebApiAbstract;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public abstract class PegasusControllerAbstract extends WebApiAbstract {

	protected final static SimpleDateFormat CUSTOM_DATE_TIME = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	@Autowired
	private PegasusModelUtil pegasusModelUtil;
	@Autowired
	private ProductService productService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private ZoneService zoneService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AuthService authService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private UserService userService;
	@Autowired
	private ApplicationEngineService applicationEngineService;
	@Autowired
	private AccessKeyService accessKeyService;
	@Autowired
	private SearchParamsUtil searchParamsUtil;
	@Autowired
	private SecurityService securityService;

	public PegasusModelUtil getPegasusModelUtil() {
		return pegasusModelUtil;
	}

	protected ProductService getProductService() {
		return productService;
	}

	protected ApplicationService getApplicationService() {
		return applicationService;
	}

	public RegionService getRegionService() {
		return regionService;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}

	protected RoleService getRoleService() {
		return roleService;
	}

	protected PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	protected CompanyService getCompanyService() {
		return companyService;
	}

	protected AuthService getAuthService() {
		return authService;
	}

	protected SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public UserService getUserService() {
		return userService;
	}

	protected ApplicationEngineService getApplicationEngineService() {
		return applicationEngineService;
	}

	protected AccessKeyService getAccessKeyService() {
		return accessKeyService;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	/**
	 * @param companyId
	 * @param statuses
	 * @param includeUsersCompany
	 * @param platformAdmin
	 * @return
	 */
	protected List<String> getCompanyIds(String companyId, EnumSet<CompanyStatus> statuses, boolean includeUsersCompany,
	        boolean platformAdmin) {
		return getCompanyOptions(companyId, statuses, includeUsersCompany, platformAdmin).stream()
		        .map(CompanyModels.CompanyOption::getId).collect(Collectors.toList());
	}

	/**
	 * @param companyId
	 * @param statuses
	 * @param includeUsersCompany
	 * @param platformAdmin
	 * @return
	 */
	protected List<CompanyModels.CompanyOption> getCompanyOptions(String companyId, EnumSet<CompanyStatus> statuses,
	        boolean includeUsersCompany, boolean platformAdmin) {
		Assert.hasText(companyId, "companyId is empty");

		String method = "getCompanyOptions";
		logDebug(method, "companyId: %s, includeUsersCompany: %s, platformAdmin: %s", companyId, includeUsersCompany,
		        platformAdmin);

		// if platform administrator then allow all companies
		if (platformAdmin)
			statuses = EnumSet.allOf(CompanyStatus.class);

		CompanySearchParams params = new CompanySearchParams();
		params.setStatuses(statuses);

		// if not a platform administrator then filter by parent companyId
		if (!platformAdmin)
			params.addParentCompanyIds(companyId);

		// lookup companies
		List<Company> companies = companyService.getCompanyRepository().findCompanies(params);
		logDebug(method, "companies: %s", companies.size());

		// convert to models
		List<CompanyModels.CompanyOption> models = new ArrayList<>();

		// users company
		Company usersCompany = getCoreCacheService().findCompanyById(companyId);
		Assert.notNull(usersCompany, "Company not found! companyId=" + companyId);
		if (includeUsersCompany && !platformAdmin) {
			models.add(new CompanyModels.CompanyOption(usersCompany));
		}

		// add any child companies
		for (Company childCompany : companies)
			models.add(new CompanyModels.CompanyOption(childCompany));

		// sort by name
		models.sort(Comparator.comparing(CompanyModels.CompanyOption::getName));
		logDebug(method, "models: %s", models.size());

		return models;
	}

	/**
	 * @param companyId
	 * @param platformAdmin
	 * @return
	 */
	protected List<SubscriptionModels.SubscriptionOption> getSubscriptionOptions(String companyId,
	        boolean platformAdmin) {

		// search params
		SubscriptionSearchParams params = new SubscriptionSearchParams();
		if (!platformAdmin) {
			// always enforce user's companyId and child companyIds
			List<String> companyIds = getCompanyIds(companyId, EnumSet.of(CompanyStatus.Active), true, false);
			params.withCompanyIds(companyIds.toArray(new String[companyIds.size()]));
			params.setEnabled(true);
		}

		// lookup subscriptions
		List<Subscription> subscriptions = subscriptionService.getSubscriptionRepository().findSubscriptions(params);

		// convert to models
		List<SubscriptionModels.SubscriptionOption> subscriptionOptions = new ArrayList<>();
		for (Subscription subscription : subscriptions)
			subscriptionOptions.add(new SubscriptionModels.SubscriptionOption(subscription));

		// sort by name
		subscriptionOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return Collections.unmodifiableList(subscriptionOptions);
	}

	/**
	 * @param platformAdmin
	 * @return
	 */
	protected List<ProductModels.ProductOption> getProductOptions(boolean platformAdmin) {
		String method = "getProductOptions";
		logDebug(method, "platformAdmin: %s", platformAdmin);

		ProductSearchParams params = new ProductSearchParams();
		if (!platformAdmin) {
			params.setHidden(false);
			params.setEnabled(true);
		}

		// lookup products
		List<Product> products = productService.getProductRepository().findProducts(params);

		// convert to models
		List<ProductModels.ProductOption> productOptions = new ArrayList<>();
		for (Product product : products)
			productOptions.add(new ProductModels.ProductOption(product));

		// sort by product name
		productOptions.sort(Comparator.comparing(ProductModels.ProductOption::getName));

		return productOptions;
	}

	/**
	 * @param platformAdmin
	 * @return
	 */
	protected List<ProductModels.ProductOption> getParentProductOptions(boolean platformAdmin) {

		Boolean hidden = null;
		Boolean enabled = null;

		if (!platformAdmin) {
			hidden = false;
			enabled = true;
		}

		// lookup
		List<Product> products = productService.getProductRepository().findParentProducts(hidden, enabled);

		List<ProductModels.ProductOption> productOptions = new ArrayList<>();
		for (Product product : products)
			productOptions.add(new ProductModels.ProductOption(product));

		// sort
		productOptions.sort(Comparator.comparing(ProductModels.ProductOption::getName));

		return Collections.unmodifiableList(productOptions);
	}

	protected List<ApplicationModels.ProductFeatureOption> getProductFeatureOptions(String productId) {
		Assert.hasText(productId, "productId is empty");

		Product product = getCoreCacheService().findProductById(productId);
		Assert.notNull(product, "Product not found! productId: " + productId);

		List<ApplicationModels.ProductFeatureOption> productFeatureOptions = new ArrayList<>();
		if (product.getFeatures() != null && !product.getFeatures().isEmpty())
			for (ProductFeature productFeature : product.getFeatures())
				productFeatureOptions.add(new ApplicationModels.ProductFeatureOption(productFeature));

		if (!productFeatureOptions.isEmpty()) {
			productFeatureOptions.sort(Comparator.comparing(ApplicationModels.ProductFeatureOption::getName));
		}

		return Collections.unmodifiableList(productFeatureOptions);
	}

	/**
	 * @param parentProductId
	 * @param enabled
	 * @param hidden
	 * @param platformAdmin
	 * @return
	 */
	protected List<ProductModels.ProductOption> getProductExtensionOptions(String parentProductId, Boolean enabled,
	        Boolean hidden, boolean platformAdmin) {

		List<Product> products = new ArrayList<>();

		if (platformAdmin) {
			hidden = null;
			enabled = null;
		}

		if (!StringUtils.isEmpty(parentProductId))
			products = getProductService().getProductRepository().findProductExtensions(parentProductId, hidden,
			        enabled);
		else
			products = productService.getProductRepository().findProductExtensions(hidden, enabled);

		List<ProductModels.ProductOption> productOptions = new ArrayList<>();
		for (Product product : products)
			productOptions.add(new ProductModels.ProductOption(product));

		productOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return productOptions;
	}

	/**
	 * @param productId
	 * @param enabled
	 * @param hidden
	 * @param admin
	 * @return
	 */
	protected List<PrivilegeModels.PrivilegeOption> getPrivilegeOptions(String productId, boolean admin) {

		PrivilegeSearchParams params = new PrivilegeSearchParams();
		params.addProductIds(productId);

		if (!admin) {
			params.setEnabled(true);
			params.setHidden(false);
		}

		List<Privilege> privileges = privilegeService.getPrivilegeRepository().findPrivileges(params);

		List<PrivilegeModels.PrivilegeOption> privilegeOptions = new ArrayList<>();
		for (Privilege privilege : privileges)
			privilegeOptions.add(new PrivilegeModels.PrivilegeOption(privilege));

		privilegeOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return privilegeOptions;
	}

	// protected List<PrivilegeModels.RolePrivilegeOption>
	// getRolePrivilegeOptions(String productId, boolean enabled) {
	// Assert.notNull(enabled, "enabled is null");
	//
	// List<Privilege> privileges = null;
	//
	// if (!StringUtils.isEmpty(productId))
	// privileges =
	// privilegeService.getPrivilegeRepository().findByProductIdAndEnabled(productId,
	// enabled);
	// else
	// privileges =
	// privilegeService.getPrivilegeRepository().findByEnabled(enabled);
	//
	// List<PrivilegeModels.RolePrivilegeOption> privilegeOptions = new
	// ArrayList<>();
	// for (Privilege privilege : privileges)
	// privilegeOptions.add(new PrivilegeModels.RolePrivilegeOption(privilege));
	//
	// if (!privilegeOptions.isEmpty())
	// Collections.sort(privilegeOptions, new
	// Comparator<PrivilegeModels.RolePrivilegeOption>() {
	//
	// @Override
	// public int compare(PrivilegeModels.RolePrivilegeOption o1,
	// PrivilegeModels.RolePrivilegeOption o2) {
	// return o1.getName().compareTo(o2.getName());
	// }
	// });
	//
	// return Collections.unmodifiableList(privilegeOptions);
	// }

	// protected List<CompanyModels.CompanyOption>
	// getCompanyOptions(CompanyStatus companyStatus) {
	// Assert.notNull(companyStatus, "companyStatuses is null");
	//
	// // TODO need to enhance to support EnumSet
	// List<Company> companies =
	// companyService.getCompanyRepository().findByStatus(companyStatus);
	//
	// List<CompanyModels.CompanyOption> companyOptions = new ArrayList<>();
	// for (Company company : companies) {
	// companyOptions.add(new CompanyModels.CompanyOption(company));
	// }
	//
	// if (!companyOptions.isEmpty()) {
	// Collections.sort(companyOptions, new
	// Comparator<CompanyModels.CompanyOption>() {
	//
	// @Override
	// public int compare(CompanyModels.CompanyOption o1,
	// CompanyModels.CompanyOption o2) {
	// return o1.getName().compareToIgnoreCase(o2.getName());
	// }
	// });
	// }
	//
	// return Collections.unmodifiableList(companyOptions);
	// }

	// protected List<CompanyModels.CompanyOption>
	// getCompanyWithSubCompaniesOptions(String parentCompanyId,
	// CompanyStatus companyStatus) {
	//
	// Assert.notNull(companyStatus, "companyStatuses is null");
	// Assert.notNull(parentCompanyId, "parentCompanyId is null");
	//
	// // TODO need to enhance to support EnumSet
	// List<Company> companies = companyService.getCompanyRepository()
	// .findByIdOrParentCompanyIdAndStatus(parentCompanyId, parentCompanyId,
	// companyStatus);
	//
	// List<CompanyModels.CompanyOption> companyOptions = new ArrayList<>();
	// for (Company company : companies) {
	// companyOptions.add(new CompanyModels.CompanyOption(company));
	// }
	//
	// if (!companyOptions.isEmpty()) {
	// Collections.sort(companyOptions, new
	// Comparator<CompanyModels.CompanyOption>() {
	//
	// @Override
	// public int compare(CompanyModels.CompanyOption o1,
	// CompanyModels.CompanyOption o2) {
	// return o1.getName().compareToIgnoreCase(o2.getName());
	// }
	// });
	// }
	//
	// return Collections.unmodifiableList(companyOptions);
	// }

	/**
	 * @param productId
	 * @param companyId
	 * @param enabled
	 * @param admin
	 * @return
	 */
	protected List<ApplicationModels.ApplicationOption> getApplicationOptions(String productId, String companyId,
	        boolean enabled, boolean admin) {
		Assert.hasText(productId, "productId is empty");

		List<Application> applications = new ArrayList<>();

		if (admin)
			applications = applicationService.getApplicationRepository().findByProductIdOrProductExtensionId(productId,
			        null, new String[] {});
		else {
			Assert.hasText(companyId, "companyId is empty");

			Set<String> companyIds = new HashSet<>();
			// lookup related companies (MSP)
			for (Company company : companyService.getCompanyRepository()
			        .findByCompanyIdOrParentCompanyIdAndStatus(companyId, EnumSet.of(CompanyStatus.Active))) {
				companyIds.add(company.getId());
			}

			applications = applicationService.getApplicationRepository().findByProductIdOrProductExtensionId(productId,
			        true, companyIds.toArray(new String[companyIds.size()]));
		}

		List<ApplicationModels.ApplicationOption> applicationOptions = new ArrayList<>();
		for (Application application : applications)
			applicationOptions.add(new ApplicationModels.ApplicationOption(application));

		applicationOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return applicationOptions;
	}

	/**
	 * @param productId
	 * @param zoneId
	 * @param platformAdmin
	 * @return
	 */
	protected List<ApplicationEngineModels.ApplicationEngineOption> getApplicationEngineOptions(String productId,
	        String zoneId, boolean platformAdmin) {
		String method = "getApplicationEngineOptions";
		logDebug(method, "productId: %s, zoneId: %s", productId, zoneId);

		// search params
		ApplicationEngineSearchParams params = new ApplicationEngineSearchParams();
		params.addProductIds(productId);
		params.addZoneIds(zoneId);

		if (!platformAdmin)
			params.setEnabled(true);

		// lookup
		List<ApplicationEngine> applicationEngines = applicationEngineService.getApplicationEngineRepository()
		        .findApplicationEngines(params);

		// convert to models
		List<ApplicationEngineModels.ApplicationEngineOption> applicationEngineOptions = new ArrayList<>();
		for (ApplicationEngine applicationEngine : applicationEngines)
			applicationEngineOptions.add(new ApplicationEngineModels.ApplicationEngineOption(applicationEngine));

		// sort
		applicationEngineOptions.sort(Comparator.comparing(ApplicationEngineModels.ApplicationEngineOption::getName));

		return Collections.unmodifiableList(applicationEngineOptions);
	}

	protected List<RoleModels.RoleOption> getRoleOptions(String applicationId, boolean enabled) {
		Assert.notNull(enabled, "enabled is null");

		List<Role> roles = null;
		if (!StringUtils.isEmpty(applicationId))
			roles = roleService.getRoleRepository().findByApplicationIdAndEnabled(applicationId, enabled);
		else
			roles = roleService.getRoleRepository().findByEnabled(enabled);

		List<RoleModels.RoleOption> roleOptions = new ArrayList<>();
		for (Role role : roles)
			roleOptions.add(new RoleModels.RoleOption(role));

		if (!roleOptions.isEmpty()) {
			roleOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));
		}

		return Collections.unmodifiableList(roleOptions);
	}

	protected List<UserModels.UserOption> getUserOptions(String companyId, UserStatus userStatus) {
		Assert.notNull(userStatus, "userStatus is null");

		List<User> users = null;
		if (!StringUtils.isEmpty(companyId))
			users = userService.getUserRepository().findByCompanyIdAndStatus(companyId, userStatus);
		else
			users = userService.getUserRepository().findByStatus(userStatus);

		List<UserModels.UserOption> userOptions = new ArrayList<>();
		for (User user : users)
			userOptions.add(new UserModels.UserOption(user));

		if (!userOptions.isEmpty()) {
			userOptions.sort(Comparator.comparing(UserModels.UserOption::getName));
		}

		return Collections.unmodifiableList(userOptions);
	}

	protected List<ZoneModels.ZoneRegionOption> getZoneRegionOptions(boolean admin, boolean enabled) {

		List<Region> regions = null;
		if (admin) {
			regions = getRegionService().getRegionRepository().findAll();
		} else {
			regions = getRegionService().getRegionRepository().findByEnabled(enabled);
		}

		List<ZoneModels.ZoneRegionOption> regionOptions = new ArrayList<>();
		for (Region region : regions)
			regionOptions.add(new ZoneModels.ZoneRegionOption(region));

		if (!regionOptions.isEmpty()) {
			regionOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));
		}

		return Collections.unmodifiableList(regionOptions);
	}

	/**
	 * @param platformAdmin
	 * @return
	 */
	protected List<ApplicationModels.ApplicationRegionOption> getApplicationRegionOptions(boolean platformAdmin) {

		List<Region> regions = null;
		if (platformAdmin)
			regions = getRegionService().getRegionRepository().findAll();
		else
			regions = getRegionService().getRegionRepository().findByEnabled(true);

		List<ApplicationModels.ApplicationRegionOption> regionOptions = new ArrayList<>();
		for (Region region : regions)
			regionOptions.add(new ApplicationModels.ApplicationRegionOption(region));

		// sort
		regionOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return Collections.unmodifiableList(regionOptions);
	}

	private List<Zone> getZones(String regionId, boolean platformAdmin) {
		// search params
		ZoneSearchParams params = new ZoneSearchParams();
		params.addRegionIds(regionId);

		if (!platformAdmin) {
			params.setEnabled(true);
			params.setHidden(false);
		}

		// lookup
		return zoneService.getZoneRepository().findZones(params);
	}

	/**
	 * @param regionId
	 * @param platformAdmin
	 * @return
	 */
	protected List<ApplicationModels.ApplicationZoneOption> getApplicationZoneOptions(String regionId,
	        boolean platformAdmin) {

		// lookup
		List<Zone> zones = getZones(regionId, platformAdmin);

		// convert to models
		List<ApplicationModels.ApplicationZoneOption> zoneOptions = new ArrayList<>();
		for (Zone zone : zones)
			zoneOptions.add(new ApplicationModels.ApplicationZoneOption(zone));

		// sort
		zoneOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));

		return Collections.unmodifiableList(zoneOptions);
	}

	/**
	 * @param regionId
	 * @param platformAdmin
	 * @return
	 */
	protected List<ZoneModels.ZoneOption> getZoneOptions(String regionId, boolean platformAdmin) {

		// lookup
		List<Zone> zones = getZones(regionId, platformAdmin);

		// convert to models
		List<ZoneModels.ZoneOption> zoneOptions = new ArrayList<>();
		for (Zone zone : zones)
			zoneOptions.add(new ZoneModels.ZoneOption(zone));

		if (!zoneOptions.isEmpty()) {
			zoneOptions.sort(Comparator.comparing(CoreDefinitionModelOption::getName));
		}

		return Collections.unmodifiableList(zoneOptions);
	}

	protected SearchParamsUtil getSearchParamsUtil() {
		return searchParamsUtil;
	}

	protected boolean validateUser(String companyId) {
		List<String> companyIds = getCompanyIds(getAuthenticatedUser().getCompanyId(),
		        EnumSet.allOf(CompanyStatus.class), true, getAuthenticatedUser().isAdmin());
		return (getAuthenticatedUser().isAdmin() || companyIds.contains(companyId));
	}

	protected boolean isAdmin() {
		return getAuthenticatedUser().isAdmin();
	}
}
