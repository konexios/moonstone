package com.arrow.pegasus.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.MiramontiTenant;
import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.LoginPolicy;
import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;

@Service("pegasusMiramontiService")
public class MiramontiService extends BaseServiceAbstract {

	final static String COMPANY_NAME_FORMAT = "EW2018-%s";
	final static String COMPANY_ABBR_FORMAT = "EW2018-%s";
	final static String SUBSCRIPTION_NAME_FORMAT = "EW2018-%s";
	final static String SUBSCRIPTION_DESCRIPTION_FORMAT = "EW2018-%s";
	final static Instant SUBSCRIPTION_START_DATE = Instant.parse("2018-01-01T00:00:00.000Z");
	final static Instant SUBSCRIPTION_END_DATE = Instant.parse("2028-01-01T00:00:00.000Z");
	final static String FIRST_NAME_FORMAT = "EW2018-%s";
	final static String LAST_NAME = "User";
	final static String EMAIL_FORMAT = "ew2018-%s@arrowconnect.io";
	final static String APPLICATION_NAME_FORMAT = "EW2018-%s";
	final static String APPLICATION_DESCRIPTION_FORMAT = "EW2018-%s";
	final static String FEATURE_FWM = "FIRMWARE_MANAGEMENT";
	final static String KRONOS_ADMIN_ROLE_NAME = "Arrow Connect Admin";
	final static String APOLLO_USER_ROLE_NAME = "Dashboard User";
	final static String[] APOLLO_USER_PRIVS = new String[] { "APOLLO_ACCESS", "APOLLO_ASSIGN_FAVORITE_BOARD",
			"APOLLO_ASSIGN_USER_DEFAULT_BOARD", "APOLLO_CREATE_BOARD", "APOLLO_DELETE_BOARD", "APOLLO_READ_BOARD",
			"APOLLO_UPDATE_BOARD" };
	final static String RHEA_APPLICATION_NAME = "Arrow Connect FM";
	final static String RHEA_USER_ROLE_NAME = "Rhea User";
	final static String PEGASUS_APPLICATION_NAME = "Pegasus";
	final static String PEGASUS_TENANT_ADMIN_ROLE_NAME = "Tenant Admin";

	@Autowired
	private CompanyService companyService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordService passwordService;

	private Company miramontiCompany;
	private Product kronosProduct;
	private Product apolloProduct;
	private Product rheaProduct;
	private List<String> kronosAdminPrivIds = new ArrayList<>();
	private List<String> apolloUserPrivIds = new ArrayList<>();
	private PasswordPolicy passwordPolicy, localPasswordPolicy;
	private LoginPolicy loginPolicy;
	private Application rheaApplication;
	private Role rheaUserRole;
	private Application pegasusApplication;

	// private Role tenantAdminRole;
	private boolean initialized = false;

	// @PostConstruct
	synchronized void lazyInit() {
		if (initialized)
			return;

		miramontiCompany = companyService.getCompanyRepository().findByName(CoreConstant.Events.MIRAMONTI_COMPANY_NAME);
		Assert.notNull(miramontiCompany, "Company not found: " + CoreConstant.Events.MIRAMONTI_COMPANY_NAME);
		kronosProduct = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
		Assert.notNull(kronosProduct, "Kronos Product not found");
		apolloProduct = getCoreCacheService().findProductBySystemName(ProductSystemNames.APOLLO);
		Assert.notNull(apolloProduct, "Apollo Product not found");
		rheaProduct = getCoreCacheService().findProductBySystemName(ProductSystemNames.RHEA);
		Assert.notNull(rheaProduct, "Rhea Product not found");
		rheaApplication = getCoreCacheService().findApplicationByName(RHEA_APPLICATION_NAME);
		Assert.notNull(rheaApplication, "Rhea Application not found");
		rheaUserRole = roleService.getRoleRepository().findFirstByNameAndApplicationId(RHEA_USER_ROLE_NAME,
				rheaApplication.getId());
		pegasusApplication = getCoreCacheService().findApplicationByName(PEGASUS_APPLICATION_NAME);
		Assert.notNull(pegasusApplication, "Pegasus Application not found");

		// tenantAdminRole =
		// roleService.getRoleRepository().findByNameAndApplicationId(PEGASUS_TENANT_ADMIN_ROLE_NAME,
		// pegasusApplication.getId());

		for (Privilege priv : privilegeService.getPrivilegeRepository().findByProductIdAndEnabled(kronosProduct.getId(),
				true)) {
			kronosAdminPrivIds.add(priv.getId());
		}

		for (String privName : APOLLO_USER_PRIVS) {
			Privilege priv = privilegeService.getPrivilegeRepository().findBySystemName(privName);
			Assert.notNull(priv, "Privilege not found: " + privName);
			apolloUserPrivIds.add(priv.getId());
		}

		// password policy
		passwordPolicy = new PasswordPolicy();
		passwordPolicy.setAllowWhitespace(false);
		passwordPolicy.setHistorical(3);
		passwordPolicy.setMaxLength(64);
		passwordPolicy.setMinDigit(1);
		passwordPolicy.setMinLength(12);
		passwordPolicy.setMinLowerCase(1);
		passwordPolicy.setMinSpecial(1);
		passwordPolicy.setMinUpperCase(1);

		// local password policy
		localPasswordPolicy = new PasswordPolicy();
		localPasswordPolicy.setAllowWhitespace(false);
		localPasswordPolicy.setMaxLength(16);
		localPasswordPolicy.setMinLength(16);
		localPasswordPolicy.setMinDigit(1);
		localPasswordPolicy.setMinLowerCase(1);
		localPasswordPolicy.setMinSpecial(1);
		localPasswordPolicy.setMinUpperCase(1);

		// login policy
		loginPolicy = new LoginPolicy();
		loginPolicy.setLockTimeoutSecs(1800);
		loginPolicy.setMaxFailedLogins(10);

		initialized = true;
	}

	public MiramontiTenant createCompany(String number, String zoneId, String applicationEngineId) {
		lazyInit();

		String method = "createCompany";
		Assert.hasText(number, "number is empty");
		MiramontiTenant result = new MiramontiTenant();

		// validate
		Zone zone = getCoreCacheService().findZoneById(zoneId);
		Assert.notNull(zone, "invalid zoneId: " + zoneId);
		ApplicationEngine applicationEngine = getCoreCacheService().findApplicationEngineById(applicationEngineId);
		Assert.notNull(applicationEngine, "invalid applicationEngineId: " + applicationEngineId);

		// dup check
		String abbrName = String.format(COMPANY_ABBR_FORMAT, number);
		Company existing = companyService.getCompanyRepository().findByAbbrName(abbrName);
		Assert.isNull(existing, "Company already exists: " + abbrName);

		// create company
		Company company = new Company();
		company.setName(String.format(COMPANY_NAME_FORMAT, number));
		company.setAbbrName(abbrName);
		company.setParentCompanyId(miramontiCompany.getId());
		company.setPasswordPolicy(passwordPolicy);
		company.setLoginPolicy(loginPolicy);
		company.setStatus(CompanyStatus.Active);
		logInfo(method, "creating company: %s", company.getName());
		companyService.create(company, CoreConstant.ADMIN_USER);
		logInfo(method, "company created %s --> %s", company.getName(), company.getId());
		result.setCompanyId(company.getId());

		// create subscription
		Subscription subscription = new Subscription();
		subscription.setName(String.format(SUBSCRIPTION_NAME_FORMAT, number));
		subscription.setDescription(String.format(SUBSCRIPTION_DESCRIPTION_FORMAT, number));
		subscription.setCompanyId(company.getId());
		subscription.setStartDate(SUBSCRIPTION_START_DATE);
		subscription.setEndDate(SUBSCRIPTION_END_DATE);
		subscription.setEnabled(true);
		subscription.setContact(contact(number));
		subscription.setBillingContact(contact(number));
		logInfo(method, "creating subscription: %s", subscription.getName());
		subscriptionService.create(subscription, CoreConstant.ADMIN_USER);
		logInfo(method, "subscription created %s --> %s", subscription.getName(), subscription.getId());
		result.setSubscriptionId(subscription.getId());

		// create Application
		Application application = new Application();
		application.setName(String.format(APPLICATION_NAME_FORMAT, number));
		application.setDescription(String.format(APPLICATION_DESCRIPTION_FORMAT, number));
		application.setZoneId(zone.getId());
		application.setCompanyId(company.getId());
		application.setSubscriptionId(subscription.getId());
		application.setApiSigningRequired(YesNoInherit.NO);
		application.setApplicationEngineId(applicationEngine.getId());
		application.setEnabled(true);
		application.setProductId(kronosProduct.getId());
		application.setProductFeatures(Collections.singletonList(FEATURE_FWM));
		application.setProductExtensionIds(Collections.singletonList(apolloProduct.getId()));
		logInfo(method, "creating application: %s", application.getName());
		applicationService.create(application, CoreConstant.ADMIN_USER);
		logInfo(method, "application created %s --> %s", application.getName(), application.getId());
		result.setApplicationId(application.getId());

		// create kronosAdmin role
		Role kronosAdmin = new Role();
		kronosAdmin.setName(KRONOS_ADMIN_ROLE_NAME);
		kronosAdmin.setDescription(KRONOS_ADMIN_ROLE_NAME);
		kronosAdmin.setProductId(kronosProduct.getId());
		kronosAdmin.setApplicationId(application.getId());
		kronosAdmin.setEditable(false);
		kronosAdmin.setEnabled(true);
		kronosAdmin.setPrivilegeIds(kronosAdminPrivIds);
		logInfo(method, "creating role: %s", kronosAdmin.getName());
		roleService.create(kronosAdmin, CoreConstant.ADMIN_USER);
		logInfo(method, "role created %s --> %s", kronosAdmin.getName(), kronosAdmin.getId());

		// create apolloUser role
		Role apolloUserRole = new Role();
		apolloUserRole.setName(APOLLO_USER_ROLE_NAME);
		apolloUserRole.setDescription(APOLLO_USER_ROLE_NAME);
		apolloUserRole.setProductId(apolloProduct.getId());
		apolloUserRole.setApplicationId(application.getId());
		apolloUserRole.setEditable(false);
		apolloUserRole.setEnabled(true);
		apolloUserRole.setPrivilegeIds(apolloUserPrivIds);
		logInfo(method, "creating role: %s", apolloUserRole.getName());
		roleService.create(apolloUserRole, CoreConstant.ADMIN_USER);
		logInfo(method, "role created %s --> %s", apolloUserRole.getName(), apolloUserRole.getId());

		// create user
		String login = String.format(EMAIL_FORMAT, number);
		String password = passwordService.generateRandomPassword(localPasswordPolicy);
		logInfo(method, "password: %s", password);
		User user = new User();
		user.setContact(contact(number));
		user.setCompanyId(company.getId());
		user.setLogin(login);
		user.setPassword(password);
		user.setAdmin(false);

		// user.setRoleIds(Arrays.asList(new String[] { kronosAdmin.getId(),
		// apolloUser.getId(), rheaUserRole.getId(),
		// tenantAdminRole.getId() }));

		// TAM - new requirement - no tenant admin role
		user.setRoleIds(
				Arrays.asList(new String[] { kronosAdmin.getId(), apolloUserRole.getId(), rheaUserRole.getId() }));

		logInfo(method, "creating user: %s", user.getLogin());
		userService.create(user, CoreConstant.ADMIN_USER);
		logInfo(method, "user created %s --> %s", login, user.getId());
		result.setUserId(user.getId());
		result.setLogin(login);
		result.setEncryptedPassword(getCryptoService().encrypt(password));

		return result;
	}

	private Contact contact(String number) {
		Contact result = new Contact();
		result.setFirstName(String.format(FIRST_NAME_FORMAT, number));
		result.setLastName(LAST_NAME);
		result.setEmail(String.format(EMAIL_FORMAT, number));
		return result;
	}
}
