package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.pegasus.data.profile.Address;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.LoginPolicy;
import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.AuthLdap;
import com.arrow.pegasus.data.security.AuthSaml;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.service.AccessKeyService;
import com.arrow.pegasus.service.ApplicationEngineService;
import com.arrow.pegasus.service.ApplicationService;
import com.arrow.pegasus.service.AuthService;
import com.arrow.pegasus.service.CompanyService;
import com.arrow.pegasus.service.CryptoService;
import com.arrow.pegasus.service.PrivilegeService;
import com.arrow.pegasus.service.ProductService;
import com.arrow.pegasus.service.RoleService;
import com.arrow.pegasus.service.SubscriptionService;
import com.arrow.pegasus.service.UserService;

import moonstone.acs.client.model.AccessKeyModel;
import moonstone.acs.client.model.AddressModel;
import moonstone.acs.client.model.ApplicationModel;
import moonstone.acs.client.model.AuthLdapModel;
import moonstone.acs.client.model.AuthModel;
import moonstone.acs.client.model.AuthSamlModel;
import moonstone.acs.client.model.CompanyModel;
import moonstone.acs.client.model.ConfigurationPropertyModel;
import moonstone.acs.client.model.ContactModel;
import moonstone.acs.client.model.CreateApplicationModel;
import moonstone.acs.client.model.CreateCompanyModel;
import moonstone.acs.client.model.CreateSubscriptionModel;
import moonstone.acs.client.model.LoginPolicyModel;
import moonstone.acs.client.model.PasswordPolicyModel;
import moonstone.acs.client.model.PrivilegeModel;
import moonstone.acs.client.model.ProductModel;
import moonstone.acs.client.model.RegionModel;
import moonstone.acs.client.model.RoleModel;
import moonstone.acs.client.model.SubscriptionModel;
import moonstone.acs.client.model.UserModel;
import moonstone.acs.client.model.ZoneModel;

public abstract class BaseApiAbstract extends ApiAbstract {

	@Autowired
	private AccessKeyService accessKeyService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private UserService userService;
	@Autowired
	private CryptoService cryptoService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AuthService authService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ApplicationEngineService applicationEngineService;

	protected String getProductSystemName() {
		return ProductSystemNames.PEGASUS;
	}

	protected CompanyService getCompanyService() {
		return companyService;
	}

	protected SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	protected UserService getUserService() {
		return userService;
	}

	protected ApplicationService getApplicationService() {
		return applicationService;
	}

	protected ProductService getProductService() {
		return productService;
	}

	public AuthService getAuthService() {
		return authService;
	}

	protected PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	protected RoleService getRoleService() {
		return roleService;
	}

	protected ApplicationEngineService getApplicationEngineService() {
		return applicationEngineService;
	}

	protected AccessKey getValidatedAccessKey(String productSystemName) {
		AccessKey accessKey = getAccessKey();
		if (accessKey.getApplicationId() != null && !validateProduct(accessKey.getApplicationId(), productSystemName)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected ApplicationModel toApplicationModel(Application application) {
		Assert.notNull(application, "application is null");
		ApplicationModel result = new ApplicationModel();
		result.setApiSigningRequired(toYesNoInheritModel(application.getApiSigningRequired()));
		result.setCode(application.getCode());
		result.setCompanyHid(getHid(application.getRefCompany(), application.getCompanyId()));
		result.setCreatedBy(application.getCreatedBy());
		result.setCreatedDate(application.getCreatedDate());
		result.setDefaultSamlEntityId(application.getDefaultSamlEntityId());
		result.setDescription(application.getDescription());
		result.setEnabled(application.isEnabled());
		result.setHid(application.getHid());
		result.setLastModifiedBy(application.getLastModifiedBy());
		result.setLastModifiedDate(application.getLastModifiedDate());
		result.setName(application.getName());
		result.setPri(application.getPri());
		result.setProductHid(getHid(application.getRefProduct(), application.getProductId()));
		result.setSubscriptionHid(getHid(application.getRefSubscription(), application.getSubscriptionId()));
		result.setZoneHid(getHid(application.getRefZone(), application.getZoneId()));
		result.setZoneSystemName(getSystemName(application.getRefZone(), application.getZoneId()));
		return result;
	}

	protected Application fromCreateApplicationModel(CreateApplicationModel model) {
		return populateApplication(new Application(), model);
	}

	protected Application populateApplication(Application application, CreateApplicationModel model) {
		Assert.notNull(model, "model is null");
		if (application == null) {
			application = new Application();
		}
		// apiSigningRequired
		application.setApiSigningRequired(fromYesNoInheritModel(model.getApiSigningRequired()));
		// productId
		Assert.hasText(model.getProductHid(), "productHid is empty");
		Product product = getCoreCacheService().findProductByHid(model.getProductHid());
		Assert.notNull(product, "product is not found");
		application.setProductId(product.getId());
		// applicationEngineId
		if (StringUtils.isNotEmpty(model.getApplicationEngineHid())) {
			ApplicationEngine applicationEngine = getCoreCacheService()
					.findApplicationEngineByHid(model.getApplicationEngineHid());
			Assert.notNull(applicationEngine, "applicationEngine is not found");
			Assert.isTrue(applicationEngine.getProductId().equals(product.getId()), "productId mismatched");
			application.setApplicationEngineId(applicationEngine.getId());
		} else {
			application.setApplicationEngineId(null);
		}
		// code
		if (!StringUtils.isEmpty(model.getCode()))
			application.setCode(model.getCode());
		// companyId
		if (application.getCompanyId() == null) {
			Assert.hasText(model.getCompanyHid(), "companyHid is empty");
			Company company = getCoreCacheService().findCompanyByHid(model.getCompanyHid());
			Assert.notNull(company, "company is not found");
			application.setCompanyId(company.getId());
		}
		// configurations
		if (model.getConfigurations() != null) {
			application.setConfigurations(model.getConfigurations().stream().map(this::fromConfigurationPropertyModel)
					.collect(Collectors.toList()));
		}
		// defaultSamlEntityId
		application.setDefaultSamlEntityId(model.getDefaultSamlEntityId());
		// description
		Assert.hasText(model.getDescription(), "description is empty");
		application.setDescription(model.getDescription());
		// enabled
		application.setEnabled(model.isEnabled());
		// name
		Assert.hasText(model.getName(), "name is empty");
		application.setName(model.getName());
		// productExtensionIds
		if (model.getProductExtensionHids() != null) {
			application.setProductExtensionIds(model.getProductExtensionHids().stream().map(hid -> {
				Product prod = getCoreCacheService().findProductByHid(hid);
				Assert.notNull(prod, "product is not found");
				Assert.isTrue(product.getId().equals(prod.getParentProductId()), "productId mismatched");
				return prod.getId();
			}).collect(Collectors.toList()));
		}
		// subscriptionId
		Assert.hasText(model.getSubscriptionHid(), "subscriptionHid is empty");
		Subscription subscription = getCoreCacheService().findSubscriptionByHid(model.getSubscriptionHid());
		Assert.notNull(subscription, "subscription is not found");
		Assert.isTrue(subscription.getCompanyId().equals(application.getCompanyId()), "companyId mismatched");
		application.setSubscriptionId(subscription.getId());
		// zoneId
		Assert.hasText(model.getZoneHid(), "zoneHid is empty");
		Zone zone = getCoreCacheService().findZoneByHid(model.getZoneHid());
		Assert.notNull(zone, "zone is not found");
		application.setZoneId(zone.getId());
		return application;
	}

	protected ConfigurationProperty fromConfigurationPropertyModel(ConfigurationPropertyModel model) {
		Assert.notNull(model, "configurationPropertyModel is null");
		ConfigurationProperty result = new ConfigurationProperty();
		if (model.getCategory() != null) {
			result.setCategory(ConfigurationPropertyCategory.valueOf(model.getCategory().name()));
		}
		if (model.getDataType() != null) {
			result.setDataType(ConfigurationPropertyDataType.valueOf(model.getDataType().name()));
		}
		result.setJsonClass(model.getJsonClass());
		result.setName(model.getName());
		result.setValue(model.getValue());
		return result;
	}

	protected SubscriptionModel toSubscriptionModel(Subscription subscription) {
		Assert.notNull(subscription, "subscription is null");
		SubscriptionModel result = new SubscriptionModel();
		if (subscription.getBillingContact() != null) {
			result.setBillingContact(toContactModel(subscription.getBillingContact()));
		}
		result.setCompanyHid(getHid(subscription.getRefCompany(), subscription.getCompanyId()));
		if (subscription.getContact() != null) {
			result.setContact(toContactModel(subscription.getContact()));
		}
		result.setCreatedBy(subscription.getCreatedBy());
		result.setCreatedDate(subscription.getCreatedDate());
		result.setDescription(subscription.getDescription());
		result.setEnabled(subscription.isEnabled());
		result.setEndDate(subscription.getEndDate().toString());
		result.setHid(subscription.getHid());
		result.setLastModifiedBy(subscription.getLastModifiedBy());
		result.setLastModifiedDate(subscription.getLastModifiedDate());
		result.setName(subscription.getName());
		result.setPri(subscription.getPri());
		result.setStartDate(subscription.getStartDate().toString());
		return result;
	}

	protected Subscription fromCreateSubscriptionModel(CreateSubscriptionModel model) {
		return populateSubscription(new Subscription(), model);
	}

	protected Subscription populateSubscription(Subscription subscription, CreateSubscriptionModel model) {
		Assert.notNull(model, "model is null");
		if (subscription == null) {
			subscription = new Subscription();
		}
		// billing contact
		if (model.getBillingContact() != null) {
			subscription.setBillingContact(fromContactModel(model.getBillingContact()));
		}
		// companyId
		if (StringUtils.isEmpty(subscription.getCompanyId())) {
			Assert.hasText(model.getCompanyHid(), "companyHid is empty");
			Company company = getCoreCacheService().findCompanyByHid(model.getCompanyHid());
			Assert.notNull(company, "company is not found");
			subscription.setCompanyId(company.getId());
		}
		// contact
		if (model.getContact() != null) {
			subscription.setContact(fromContactModel(model.getContact()));
		}
		// description
		Assert.hasText(model.getDescription(), "description is empty");
		subscription.setDescription(model.getDescription());
		// enabled
		subscription.setEnabled(model.isEnabled());
		// endDate
		Assert.notNull(model.getEndDate(), "endDate is empty");
		subscription.setEndDate(model.getEndDate());
		// name
		Assert.hasText(model.getName(), "name is empty");
		subscription.setName(model.getName());
		// startDate
		Assert.notNull(model.getStartDate(), "startDate is empty");
		subscription.setStartDate(model.getStartDate());
		Assert.isTrue(subscription.getEndDate().isAfter(subscription.getStartDate()), "startDate is after endDate");
		return subscription;
	}

	protected CompanyModel toCompanyModel(Company company) {
		Assert.notNull(company, "company is null");
		CompanyModel result = new CompanyModel();
		result.setAbbrName(company.getAbbrName());
		if (company.getAddress() != null) {
			result.setAddress(toAddressModel(company.getAddress()));
		}
		if (company.getBillingAddress() != null) {
			result.setBillingAddress(toAddressModel(company.getBillingAddress()));
		}
		if (company.getContact() != null) {
			result.setBillingContact(toContactModel(company.getContact()));
		}
		result.setCreatedBy(company.getCreatedBy());
		result.setCreatedDate(company.getCreatedDate());
		result.setHid(company.getHid());
		result.setLastModifiedBy(company.getLastModifiedBy());
		result.setLastModifiedDate(company.getLastModifiedDate());
		if (company.getLoginPolicy() != null) {
			result.setLoginPolicy(toLoginPolicyModel(company.getLoginPolicy()));
		}
		result.setName(company.getName());
		if (company.getPasswordPolicy() != null) {
			result.setPasswordPolicy(toPasswordPolicyModel(company.getPasswordPolicy()));
		}
		result.setPri(company.getPri());
		if (company.getStatus() != null) {
			result.setStatus(toCompanyStatusModel(company.getStatus()));
		}
		if (StringUtils.isNotBlank(company.getParentCompanyId())) {
			Company parentCompany = getCoreCacheService().findCompanyById(company.getParentCompanyId());
			Assert.notNull(parentCompany, "parent company is not found");
			result.setParentCompanyHid(parentCompany.getHid());
		}
		return result;
	}

	protected Company fromCreateCompanyModel(CreateCompanyModel model) {
		return populateCompany(new Company(), model);
	}

	protected Company populateCompany(Company company, CreateCompanyModel model) {
		Assert.notNull(model, "CreateCompanyModel is null");
		if (company == null) {
			company = new Company();
		}
		// abbrName
		Assert.hasText(model.getAbbrName(), "abbrName is empty");
		Company companyByAbbrName = getCompanyService().getCompanyRepository().findByAbbrName(model.getAbbrName());
		Assert.isTrue(
				companyByAbbrName == null
						|| companyByAbbrName != null && companyByAbbrName.getId().equals(company.getId()),
				"abbrName already exists");
		company.setAbbrName(model.getAbbrName());
		// address
		if (model.getAddress() != null) {
			company.setAddress(fromAddressModel(model.getAddress()));
		}
		// billing address
		if (model.getBillingAddress() != null) {
			company.setBillingAddress(fromAddressModel(model.getBillingAddress()));
		}
		// billing contact
		if (model.getBillingContact() != null) {
			company.setBillingContact(fromContactModel(model.getBillingContact()));
		}
		// contact
		if (model.getContact() != null) {
			company.setContact(fromContactModel(model.getContact()));
		}
		// login policy
		if (model.getLoginPolicy() != null) {
			company.setLoginPolicy(fromLoginPolicyModel(model.getLoginPolicy()));
		}
		// name
		Assert.hasText(model.getName(), "name is empty");
		company.setName(model.getName());
		// parent company
		if (StringUtils.isEmpty(company.getParentCompanyId())) {
			Assert.hasText(model.getParentCompanyHid(), "parentCompanyHid is empty");
			Company parentCompany = getCoreCacheService().findCompanyByHid(model.getParentCompanyHid());
			Assert.notNull(parentCompany, "parentCompany is not found");
			company.setParentCompanyId(parentCompany.getId());
		}
		// password policy
		company.setPasswordPolicy(fromPasswordPolicyModel(model.getPasswordPolicy()));
		// status
		company.setStatus(fromCompanyStatus(model.getStatus()));
		return company;
	}

	protected LoginPolicy fromLoginPolicyModel(LoginPolicyModel model) {
		Assert.notNull(model, "LoginPolicyModel is null");
		LoginPolicy result = new LoginPolicy();
		result.setLockTimeoutSecs(model.getLockTimeoutSecs());
		result.setMaxFailedLogins(model.getMaxFailedLogins());
		return result;
	}

	protected PasswordPolicy fromPasswordPolicyModel(PasswordPolicyModel model) {
		Assert.notNull(model, "PasswordPolicyModel is null");
		PasswordPolicy result = new PasswordPolicy();
		result.setAllowWhitespace(model.isAllowWhitespace());
		result.setHistorical(model.getHistorical());
		result.setMaxLength(model.getMaxLength());
		result.setMinDigit(model.getMinDigit());
		result.setMinLength(model.getMinLength());
		result.setMinLowerCase(model.getMinLowerCase());
		result.setMinSpecial(model.getMinSpecial());
		result.setMinUpperCase(model.getMinUpperCase());
		return result;
	}

	protected CompanyStatus fromCompanyStatus(moonstone.acs.client.model.CompanyStatus model) {
		Assert.notNull(model, "CompanyStatus is null");
		return CompanyStatus.valueOf(model.name());
	}

	protected AccessKeyModel toAccessKeyModel(AccessKey accessKey) {
		Assert.notNull(accessKey, "accessKey is null");
		accessKey = getCoreCacheHelper().populateAccessKey(accessKey);
		AccessKeyModel result = new AccessKeyModel();
		result.setApiKey(cryptoService.decrypt(accessKey.getEncryptedApiKey()));
		result.setClassName(accessKey.getClass().getCanonicalName());
		result.setPri(accessKey.getPri());
		result.setSecretKey(cryptoService.decrypt(accessKey.getEncryptedSecretKey()));
		return result;
	}

	protected AccessKey fromAccessKeyModel(AccessKeyModel model) {
		// TODO
		throw new NotImplementedException("TODO");
	}

	protected <T extends UserModel> T toUserModel(T model, User user) {
		String method = "toUserModel";
		Assert.notNull(model, "model is null");
		Assert.notNull(user, "user is null");
		T result = model;
		result.setCompanyHid(getHid(user.getRefCompany(), user.getCompanyId()));
		if (user.getContact() != null) {
			result.setContact(toContactModel(user.getContact()));
		}
		result.setCreatedBy(user.getCreatedBy());
		result.setCreatedDate(user.getCreatedDate());
		result.setHid(user.getHid());
		result.setLastModifiedBy(user.getLastModifiedBy());
		result.setLastModifiedDate(user.getLastModifiedDate());
		try {
			result.setLogin(cryptoService.getCrypto().internalDecrypt(user.getLogin()));
		} catch (Exception e) {
			result.setLogin(user.getLogin());
			logError(method, "login=%s %s", user.getLogin(), e);
		}
		result.setPri(user.getPri());
		if (user.getRefRoles() != null && !user.getRefRoles().isEmpty()) {
			result.setRoleHids(populateHids(user.getRefRoles().stream()));
		} else if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
			result.setRoleHids(
					populateHids(user.getRoleIds().stream().map(id -> getCoreCacheService().findRoleById(id))));
		}
		if (user.getStatus() != null) {
			result.setStatus(toUserStatusModel(user.getStatus()));
		}
		return result;
	}

	protected UserModel toUserModel(User user) {
		return toUserModel(new UserModel(), user);
	}

	protected User fromUserModel(UserModel model) {
		Assert.notNull(model, "UserModel is null");
		Assert.hasText(model.getCompanyHid(), "companyHid is empty");
		Company company = getCoreCacheService().findCompanyByHid(model.getCompanyHid());
		Assert.notNull(company, "company is not found");

		User user = new User();
		user.setCompanyId(company.getId());
		user.setContact(fromContactModel(model.getContact()));
		if (model.getAddress() != null)
			user.setAddress(fromAddressModel(model.getAddress()));
		user.setLogin(model.getLogin());
		user.setStatus(fromUserStatusModel(model.getStatus()));
		if (model.getRoleHids() != null) {
			user.setRoleIds(populateIds(model.getRoleHids().stream().map(this::getValidatedRole)));
		}
		return user;
	}

	protected ContactModel toContactModel(Contact contact) {
		Assert.notNull(contact, "contact is null");
		ContactModel result = new ContactModel();
		result.setCell(contact.getCell());
		result.setEmail(contact.getEmail());
		result.setFax(contact.getFax());
		result.setFirstName(contact.getFirstName());
		result.setHome(contact.getHome());
		result.setLastName(contact.getLastName());
		result.setMonitorExt(contact.getMonitorExt());
		result.setOffice(contact.getOffice());
		result.setSipUri(contact.getSipUri());
		return result;
	}

	protected Contact fromContactModel(ContactModel model) {
		return populateContact(new Contact(), model);
	}

	protected Contact populateContact(Contact contact, ContactModel model) {
		Assert.notNull(model, "ContactModel is null");
		if (contact == null) {
			contact = new Contact();
		}
		if (StringUtils.isNotBlank(model.getFirstName())) {
			contact.setFirstName(model.getFirstName());
		}
		Assert.hasText(contact.getFirstName(), "firstName is empty");
		if (StringUtils.isNotBlank(model.getLastName())) {
			contact.setLastName(model.getLastName());
		}
		Assert.hasText(contact.getLastName(), "lastName is empty");
		if (StringUtils.isNotBlank(model.getEmail())) {
			contact.setEmail(model.getEmail());
		}
		Assert.hasText(contact.getEmail(), "email is empty");
		if (StringUtils.isNotBlank(model.getCell())) {
			contact.setCell(model.getCell());
		}
		if (StringUtils.isNotBlank(model.getFax())) {
			contact.setFax(model.getFax());
		}
		if (StringUtils.isNotBlank(model.getHome())) {
			contact.setHome(model.getHome());
		}
		if (StringUtils.isNotBlank(model.getMonitorExt())) {
			contact.setMonitorExt(model.getMonitorExt());
		}
		if (StringUtils.isNotBlank(model.getOffice())) {
			contact.setOffice(model.getOffice());
		}
		if (StringUtils.isNotBlank(model.getSipUri())) {
			contact.setSipUri(model.getSipUri());
		}
		return contact;
	}

	protected AddressModel toAddressModel(Address address) {
		Assert.notNull(address, "address is null");
		AddressModel result = new AddressModel();
		result.setAddress1(address.getAddress1());
		result.setAddress2(address.getAddress2());
		result.setCity(address.getCity());
		result.setCountry(address.getCountry());
		result.setState(address.getState());
		result.setZip(address.getZip());
		return result;
	}

	protected Address fromAddressModel(AddressModel model) {
		Assert.notNull(model, "AddressModel is null");
		Address result = new Address();
		result.setAddress1(model.getAddress1());
		result.setAddress2(model.getAddress2());
		result.setCity(model.getCity());
		result.setCountry(model.getCountry());
		result.setState(model.getState());
		result.setZip(model.getZip());
		return result;
	}

	protected AccessKeyService getAccessKeyService() {
		return accessKeyService;
	}

	protected AccessKey validateRootAccess() {
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		for (Company company : companyService.getCompanyRepository()
				.findRootCompaniesByStatus(EnumSet.of(CompanyStatus.Active))) {
			if (accessKey.isOwner(company)) {
				return accessKey;
			}
		}
		throw new NotAuthorizedException();
	}

	protected AccessKey validateApplicationOwner() {
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!accessKey.isOwner(accessKey.getRefApplication())) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected PasswordPolicyModel toPasswordPolicyModel(PasswordPolicy passwordPolicy) {
		Assert.notNull(passwordPolicy, "passwordPolicy is null");
		PasswordPolicyModel result = new PasswordPolicyModel();
		result.setAllowWhitespace(passwordPolicy.isAllowWhitespace());
		result.setHistorical(passwordPolicy.getHistorical());
		result.setMaxLength(passwordPolicy.getMaxLength());
		result.setMinDigit(passwordPolicy.getMinDigit());
		result.setMinLength(passwordPolicy.getMinLength());
		result.setMinLowerCase(passwordPolicy.getMinLowerCase());
		result.setMinSpecial(passwordPolicy.getMinSpecial());
		result.setMinUpperCase(passwordPolicy.getMinUpperCase());
		return result;
	}

	protected LoginPolicyModel toLoginPolicyModel(LoginPolicy loginPolicy) {
		Assert.notNull(loginPolicy, "loginPolicy is null");
		LoginPolicyModel result = new LoginPolicyModel();
		result.setLockTimeoutSecs(loginPolicy.getLockTimeoutSecs());
		result.setMaxFailedLogins(loginPolicy.getMaxFailedLogins());
		return result;
	}

	protected ProductModel toProductModel(Product product) {
		Assert.notNull(product, "product is null");
		ProductModel result = new ProductModel();
		result.setApiSigningRequired(product.isApiSigningRequired());
		result.setCreatedBy(product.getCreatedBy());
		result.setCreatedDate(product.getCreatedDate());
		result.setDescription(product.getDescription());
		result.setEnabled(product.isEnabled());
		result.setHid(product.getHid());
		result.setLastModifiedBy(product.getLastModifiedBy());
		result.setLastModifiedDate(product.getLastModifiedDate());
		result.setName(product.getName());
		result.setPri(product.getPri());
		result.setSystemName(product.getSystemName());
		if (StringUtils.isNotBlank(product.getParentProductId())) {
			Product parentProduct = getCoreCacheService().findProductById(product.getParentProductId());
			Assert.notNull(parentProduct, "parentProduct is null");
			result.setParentProductHid(parentProduct.getHid());
		}
		return result;
	}

	protected RoleModel toRoleModel(Role role) {
		Assert.notNull(role, "role is null");

		RoleModel result = new RoleModel();
		result.setCreatedBy(role.getCreatedBy());
		result.setCreatedDate(role.getCreatedDate());
		result.setDescription(role.getDescription());
		result.setEditable(role.isEditable());
		result.setEnabled(role.isEnabled());
		result.setHid(role.getHid());
		result.setLastModifiedBy(role.getLastModifiedBy());
		result.setLastModifiedDate(role.getLastModifiedDate());
		result.setName(role.getName());
		result.setPri(role.getPri());

		if (role.getRefApplication() != null)
			result.setRefApplication(toApplicationModel(role.getRefApplication()));

		if (role.getRefProduct() != null)
			result.setRefProduct(toProductModel(role.getRefProduct()));

		if (role.getRefPrivileges() != null && !role.getRefPrivileges().isEmpty()) {
			result.setPrivilegeHids(populateHids(role.getRefPrivileges().stream()));
		} else if (role.getPrivilegeIds() != null && !role.getPrivilegeIds().isEmpty()) {
			result.setPrivilegeHids(populateHids(
					role.getPrivilegeIds().stream().map(id -> getCoreCacheService().findPrivilegeById(id))));
		}

		result.setApplicationHid(getHid(role.getRefApplication(), role.getApplicationId()));

		result.setProductHid(getHid(role.getRefProduct(), role.getProductId()));

		return result;
	}

	protected Role fromRoleModel(RoleModel model) {
		Assert.notNull(model, "RoleModel is null");
		Role result = new Role();
		Application application = getCoreCacheService().findApplicationByHid(model.getApplicationHid());
		Assert.notNull(application, "application is not found");
		result.setApplicationId(application.getId());
		Product product = getCoreCacheService().findProductByHid(model.getProductHid());
		Assert.notNull(product, "product is not found");
		result.setProductId(product.getId());
		result.setDescription(model.getDescription());
		result.setEditable(model.isEditable());
		result.setEnabled(model.isEnabled());
		result.setName(model.getName());
		if (model.getPrivilegeHids() != null && !model.getPrivilegeHids().isEmpty()) {
			result.setPrivilegeIds(populateIds(model.getPrivilegeHids().stream().map(this::getValidatedPrivilege)));
		}
		return result;
	}

	protected PrivilegeModel toPrivilegeModel(Privilege priv) {
		Assert.notNull(priv, "priv is null");

		PrivilegeModel result = new PrivilegeModel();
		result.setCreatedBy(priv.getCreatedBy());
		result.setCreatedDate(priv.getCreatedDate());
		result.setDescription(priv.getDescription());
		result.setEnabled(priv.isEnabled());
		result.setHid(priv.getHid());
		result.setLastModifiedBy(priv.getLastModifiedBy());
		result.setLastModifiedDate(priv.getLastModifiedDate());
		result.setName(priv.getName());
		result.setPri(priv.getPri());
		result.setProductHid(getHid(priv.getRefProduct(), priv.getProductId()));
		result.setSystemName(priv.getSystemName());

		return result;
	}

	protected Privilege fromPrivilegeModel(PrivilegeModel model) {
		return populatePrivilege(new Privilege(), model);
	}

	protected Privilege populatePrivilege(Privilege privilege, PrivilegeModel model) {
		Assert.notNull(model, "model is null");
		if (privilege == null) {
			privilege = new Privilege();
		}
		privilege.setEnabled(model.isEnabled());
		if (StringUtils.isNotBlank(model.getProductHid())) {
			Product product = getCoreCacheService().findProductByHid(model.getProductHid());
			Assert.notNull(product, "product is not found");
			privilege.setProductId(product.getId());
		}
		Assert.hasText(privilege.getProductId(), "product is empty");
		if (StringUtils.isNotBlank(model.getDescription())) {
			privilege.setDescription(model.getDescription());
		}
		Assert.hasText(privilege.getDescription(), "description is empty");
		if (StringUtils.isNotBlank(model.getName())) {
			privilege.setName(model.getName());
		}
		Assert.hasText(privilege.getName(), "name is empty");
		if (StringUtils.isNotBlank(model.getSystemName())) {
			privilege.setSystemName(model.getSystemName());
		}
		Assert.hasText(privilege.getSystemName(), "systemName is empty");
		return privilege;
	}

	protected void checkPrivileges(List<String> privilegeIds) {
		privilegeIds.stream().forEach(privilegeId -> {
			Privilege privilege = getCoreCacheService().findPrivilegeById(privilegeId);
			Assert.notNull(privilege, "privilege with Id: " + privilegeId + " was not found");
		});
	}

	protected moonstone.acs.client.model.UserStatus toUserStatusModel(
			com.arrow.pegasus.data.profile.UserStatus status) {
		Assert.notNull(status, "status is null");
		return moonstone.acs.client.model.UserStatus.valueOf(status.name());
	}

	protected com.arrow.pegasus.data.profile.UserStatus fromUserStatusModel(
			moonstone.acs.client.model.UserStatus model) {
		Assert.notNull(model, "model is null");
		return com.arrow.pegasus.data.profile.UserStatus.valueOf(model.name());
	}

	protected moonstone.acs.client.model.CompanyStatus toCompanyStatusModel(
			com.arrow.pegasus.data.profile.CompanyStatus status) {
		Assert.notNull(status, "status is null");
		return moonstone.acs.client.model.CompanyStatus.valueOf(status.name());
	}

	protected moonstone.acs.client.model.YesNoInherit toYesNoInheritModel(
			com.arrow.pegasus.data.YesNoInherit yesNoInherit) {
		Assert.notNull(yesNoInherit, "yesNoInherit is null");
		return moonstone.acs.client.model.YesNoInherit.valueOf(yesNoInherit.name());
	}

	protected com.arrow.pegasus.data.YesNoInherit fromYesNoInheritModel(moonstone.acs.client.model.YesNoInherit model) {
		Assert.notNull(model, "yesNoInherit is null");
		return com.arrow.pegasus.data.YesNoInherit.valueOf(model.name());
	}

	protected List<String> populateIds(Stream<? extends DefinitionCollectionAbstract> stream) {
		if (stream == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(stream.filter(item -> item != null && item.isEnabled()).map(item -> item.getId())
				.collect(Collectors.toSet()));
	}

	protected List<String> populateHids(Stream<? extends DefinitionCollectionAbstract> stream) {
		if (stream == null) {
			return new ArrayList<>();
		}
		return stream.filter(item -> item != null).map(item -> item.getHid())
				.collect(Collectors.toCollection(ArrayList::new));
	}

	protected Role getValidatedRole(String hid) {
		Role role = getCoreCacheService().findRoleByHid(hid);
		Assert.notNull(role, "role is not found");
		Assert.isTrue(role.isEnabled(), "role is disabled");
		return role;
	}

	protected Privilege getValidatedPrivilege(String hid) {
		Privilege privilege = getCoreCacheService().findPrivilegeByHid(hid);
		Assert.notNull(privilege, "privilege is not found");
		Assert.isTrue(privilege.isEnabled(), "privilege is disabled");
		return privilege;
	}

	protected void checkProduct(String productId) {
		Assert.hasText(productId, "productId is empty");
		Product product = getCoreCacheService().findProductById(productId);
		Assert.notNull(product, "product not found");
	}

	protected void checkZone(String zoneId) {
		if (zoneId != null) {
			Zone zone = getCoreCacheService().findZoneById(zoneId);
			Assert.notNull(zone, "zone not found");
		}
	}

	protected void checkApplication(String applicationId) {
		Assert.hasText(applicationId, "applicationId is empty");
		Application application = getCoreCacheService().findApplicationById(applicationId);
		Assert.notNull(application, "application not found");
	}

	protected void checkCompany(String companyId) {
		Assert.hasText(companyId, "companyId is empty");
		Company company = getCoreCacheService().findCompanyById(companyId);
		Assert.notNull(company, "company not found");
	}

	protected Auth getValidatedAuth(String authId) {
		Assert.hasText(authId, "authId is empty");
		Auth auth = getCoreCacheService().findAuthById(authId);
		Assert.notNull(auth, "auth not found");
		return auth;
	}

	protected void checkRole(String roleId) {
		Assert.hasText(roleId, "roleId is empty");
		Role role = getCoreCacheService().findRoleById(roleId);
		Assert.notNull(role, "role not found");
	}

	protected User getValidatedUser(String userId) {
		Assert.hasText(userId, "userId is empty");
		User user = getCoreCacheService().findUserById(userId);
		Assert.notNull(user, "user not found");
		return user;
	}

	protected boolean hasPrivilege(Predicate<Company> checkPrivilege, Company company) {
		Assert.notNull(checkPrivilege, "checkPrivilege is null");
		String method = "hasPrivilege";
		logDebug(method, "checkPrivilege: %s, company: %s", checkPrivilege.toString(),
				(company == null ? "null" : company.getName()));

		if (company == null) {
			return false;
		}
		boolean result = checkPrivilege.test(company);
		if (!result) {
			String parentCompanyId = company.getParentCompanyId();
			if (StringUtils.isNotBlank(parentCompanyId)) {
				Company parentCompany = getCoreCacheService().findCompanyById(parentCompanyId);
				result = hasPrivilege(checkPrivilege, parentCompany);
			}
		}

		logDebug(method, "checkPrivilege: %s, company: %s, result: %s", checkPrivilege.toString(),
				(company == null ? "null" : company.getName()), result);
		return result;
	}

	private String getHid(Application ref, String id) {
		Application application = getDocument(ref, id,
				(applicationId) -> getCoreCacheService().findApplicationById(applicationId));
		return application != null ? application.getHid() : null;
	}

	private String getHid(Subscription ref, String id) {
		Subscription subscription = getDocument(ref, id,
				(subscriptionId) -> getCoreCacheService().findSubscriptionById(subscriptionId));
		return subscription != null ? subscription.getHid() : null;
	}

	private String getHid(Product ref, String id) {
		Product product = getDocument(ref, id, (productId) -> getCoreCacheService().findProductById(productId));
		return product != null ? product.getHid() : null;
	}

	private String getHid(Company ref, String id) {
		Company company = getDocument(ref, id, (companyId) -> getCoreCacheService().findCompanyById(companyId));
		return company != null ? company.getHid() : null;
	}

	private String getHid(Zone ref, String id) {
		Zone zone = getDocument(ref, id, (zoneId) -> getCoreCacheService().findZoneById(zoneId));
		return zone != null ? zone.getHid() : null;
	}

	private <T extends DocumentAbstract> T getDocument(T ref, String id, Function<String, T> findById) {
		T document = null;
		if (ref != null) {
			document = ref;
		} else if (findById != null && id != null) {
			document = findById.apply(id);
		}
		return document;
	}

	private String getHid(Region ref, String id) {
		Region region = getDocument(ref, id, (regionId) -> getCoreCacheService().findRegionById(regionId));
		return region != null ? region.getHid() : null;
	}

	protected RegionModel toRegionModel(Region region) {
		Assert.notNull(region, "region is null");

		RegionModel result = new RegionModel();
		result.setCreatedBy(region.getCreatedBy());
		result.setCreatedDate(region.getCreatedDate());
		result.setDescription(region.getDescription());
		result.setEnabled(region.isEnabled());
		result.setHid(region.getHid());
		result.setLastModifiedBy(region.getLastModifiedBy());
		result.setLastModifiedDate(region.getLastModifiedDate());
		result.setName(region.getName());
		result.setPri(region.getPri());

		return result;
	}

	protected ZoneModel toZoneModel(Zone zone) {
		Assert.notNull(zone, "zone is null");

		ZoneModel result = new ZoneModel();
		result.setCreatedBy(zone.getCreatedBy());
		result.setCreatedDate(zone.getCreatedDate());
		result.setDescription(zone.getDescription());
		result.setEnabled(zone.isEnabled());
		result.setHid(zone.getHid());
		result.setLastModifiedBy(zone.getLastModifiedBy());
		result.setLastModifiedDate(zone.getLastModifiedDate());
		result.setName(zone.getName());
		result.setPri(zone.getPri());
		result.setRegionHid(getHid(zone.getRefRegion(), zone.getRegionId()));

		if (zone.getRefRegion() != null)
			result.setRefRegion(toRegionModel(zone.getRefRegion()));

		result.setSystemName(zone.getSystemName());

		return result;
	}

	protected AuthModel toAuthModel(Auth auth) {
		Assert.notNull(auth, "auth is null");

		AuthModel result = new AuthModel();
		result.setCreatedBy(auth.getCreatedBy());
		result.setCreatedDate(auth.getCreatedDate());
		result.setHid(auth.getHid());
		result.setLastModifiedBy(auth.getLastModifiedBy());
		result.setLastModifiedDate(auth.getLastModifiedDate());
		result.setPri(auth.getPri());
		result.setEnabled(auth.isEnabled());

		if (auth.getCompanyId() != null) {
			Company company = getCoreCacheService().findCompanyById(auth.getCompanyId());
			if (company != null) {
				result.setCompanyHid(company.getHid());
			}
		}

		if (auth.getType() != null) {
			result.setType(toAuthTypeModel(auth.getType()));
		}

		if (auth.getLdap() != null) {
			result.setLdap(toAuthLdapModel(auth.getLdap()));
		}

		if (auth.getSaml() != null) {
			result.setSaml(toAuthSamlModel(auth.getSaml()));
		}

		return result;
	}

	private AuthSamlModel toAuthSamlModel(AuthSaml authSaml) {
		Assert.notNull(authSaml, "authSaml is null");

		AuthSamlModel result = new AuthSamlModel();
		result.setEmailAttr(authSaml.getEmailAttr());
		result.setFirstNameAttr(authSaml.getFirstNameAttr());
		result.setIdp(authSaml.getIdp());
		result.setLastNameAttr(authSaml.getLastNameAttr());

		return result;
	}

	protected AuthLdapModel toAuthLdapModel(AuthLdap authLdap) {
		Assert.notNull(authLdap, "authLdap is null");

		AuthLdapModel result = new AuthLdapModel();
		result.setDomain(authLdap.getDomain());
		result.setUrl(authLdap.getUrl());

		if (authLdap.getApplicationId() != null) {
			Application app = getCoreCacheService().findApplicationById(authLdap.getApplicationId());
			if (app != null) {
				result.setApplicationHid(app.getHid());
			}
		}

		return result;
	}

	protected moonstone.acs.client.model.AuthType toAuthTypeModel(com.arrow.pegasus.data.security.AuthType type) {
		Assert.notNull(type, "AuthType is null");
		return moonstone.acs.client.model.AuthType.valueOf(type.name());
	}

	private String getSystemName(Zone ref, String id) {
		Zone zone = getDocument(ref, id, (zoneId) -> getCoreCacheService().findZoneById(zoneId));
		return zone != null ? zone.getSystemName() : null;
	}

}
