package com.arrow.pegasus.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.pegasus.data.profile.Address;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserAuth;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.AuthLdap;
import com.arrow.pegasus.data.security.AuthSaml;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.security.Crypto;
import com.arrow.pegasus.security.CryptoClient128Impl;
import com.arrow.pegasus.security.CryptoClientImpl;
import com.arrow.pegasus.service.CryptoService;
import com.arrow.pegasus.web.model.CompanyModels.AuthLdapModel;
import com.arrow.pegasus.web.model.CompanyModels.AuthSamlModel;
import com.arrow.pegasus.web.model.UserModels.UserAccountModel;
import com.arrow.pegasus.web.model.UserModels.UserAuthenticationUpsert;
import com.arrow.pegasus.web.model.UserModels.UserProfileModel;
import com.arrow.pegasus.webapi.data.PegasusCoreModelUtil;

@Component
public class PegasusModelUtil extends PegasusCoreModelUtil {

	@Autowired
	private CryptoService cryptoService;

	public String toDecryptedLogin(User user) {
		if (user == null)
			return null;

		String result = null;
		try {
			result = cryptoService.getCrypto().internalDecrypt(user.getLogin());
		} catch (Throwable e) {
			System.out.println("-----> user: " + user.getId() + " login: " + user.getLogin());

			throw new AcsLogicalException("Unable to decrypt user's login. User Id: " + user.getId(), e);
		}

		return result;
	}

	public Product toProduct(ProductModels.ProductModel model) {

		return toProduct(model, new Product());
	}

	public Product toProduct(ProductModels.ProductModel model, Product product) {

		if (model == null || product == null)
			return null;

		product.setName(model.getName());
		product.setDescription(model.getDescription());
		product.setSystemName(model.getSystemName());
		product.setApiSigningRequired(model.isApiSigningRequired());
		product.setEnabled(model.isEnabled());
		product.setHidden(model.isHidden());
		product.setParentProductId(model.getParentProductId());
		product.setConfigurations(toConfigurationProperty(model.getConfigurations()));
		product.setFeatures(toProductFeatures(model.getFeatures()));

		return product;
	}

	public List<ConfigurationProperty> toConfigurationProperty(List<ConfigurationModel> models) {

		if (models == null || models.isEmpty())
			return Collections.emptyList();

		List<ConfigurationProperty> configurations = new ArrayList<>();

		for (ConfigurationModel model : models) {
			ConfigurationProperty configurationProperty = new ConfigurationProperty();
			configurationProperty.setDataType(model.getDataType());
			configurationProperty.setCategory(model.getCategory());
			configurationProperty.setName(model.getName());
			configurationProperty.setValue(model.getValue());
			configurationProperty.setJsonClass(model.getJsonClass());
			configurations.add(configurationProperty);
		}

		return configurations;
	}

	public List<ProductFeature> toProductFeatures(List<ProductFeature> models) {

		if (models == null || models.isEmpty())
			return Collections.emptyList();

		List<ProductFeature> features = new ArrayList<>();

		for (ProductFeature model : models)
			features.add(model);

		return features;
	}

	public CompanyModels.CompanyModel toCompanyModel(Company company) {
		if (company == null)
			return null;

		CompanyModels.CompanyModel model = new CompanyModels.CompanyModel().withAbbrName(company.getAbbrName())
				.withAddress(toAddressModel(company.getAddress())).withContact(toContactModel(company.getContact()))
				.withHid(company.getHid()).withId(company.getId()).withName(company.getName())
				.withParentCompanyId(company.getParentCompanyId()).withStatus(company.getStatus());

		return model;
	}

	public Company toCompany(CompanyModels.CompanyModel model) {
		return toCompany(model, new Company());
	}

	public Company toCompany(CompanyModels.CompanyModel model, Company company) {
		if (model == null)
			return null;

		if (company == null)
			company = new Company();

		company.setName(model.getName());
		company.setAbbrName(model.getAbbrName());
		company.setStatus(model.getStatus());
		company.setParentCompanyId(model.getParentCompanyId());
		company.setAddress(toAddress(model.getAddress()));
		company.setContact(toContact(model.getContact()));

		return company;
	}

	public CompanyModels.CompanyAuthModel toCompanyAuthModel(Auth auth) {
		if (auth == null)
			return null;

		CompanyModels.CompanyAuthModel model = new CompanyModels.CompanyAuthModel().withId(auth.getId())
				.withHid(auth.getHid()).withCompanyId(auth.getCompanyId()).withType(auth.getType())
				.withEnabled(auth.isEnabled());

		switch (auth.getType()) {
		case LDAP:
			model.withLdap(new AuthLdapModel().withApplicationId(auth.getLdap().getApplicationId())
					.withDomain(auth.getLdap().getDomain()).withUrl(auth.getLdap().getUrl()));
			break;
		case SAML:
			model.withSaml(new AuthSamlModel().withIdp(auth.getSaml().getIdp())
					.withEmailAttr(auth.getSaml().getEmailAttr()).withFirstNameAttr(auth.getSaml().getFirstNameAttr())
					.withLastNameAttr(auth.getSaml().getLastNameAttr()));
			break;
		default:
			throw new AcsLogicalException("Unsupported type! type=" + auth.getType());
		}

		return model;
	}

	public Auth toAuth(CompanyModels.CompanyAuthModel model) {
		return toAuth(model, new Auth());
	}

	public Auth toAuth(CompanyModels.CompanyAuthModel model, Auth auth) {
		if (model == null)
			return null;

		if (auth == null)
			auth = new Auth();

		auth.setCompanyId(model.getCompanyId());
		auth.setEnabled(model.isEnabled());
		auth.setHid(model.getHid());
		auth.setId(model.getId());
		auth.setLdap(toAuthLdap(model.getLdap()));
		auth.setSaml(toAuthSaml(model.getSaml()));
		auth.setType(model.getType());

		return auth;
	}

	public AuthLdap toAuthLdap(CompanyModels.AuthLdapModel model) {
		return toAuthLdap(model, new AuthLdap());
	}

	public AuthLdap toAuthLdap(CompanyModels.AuthLdapModel model, AuthLdap ldap) {
		if (model == null)
			return null;

		if (ldap == null)
			ldap = new AuthLdap();

		ldap.setApplicationId(model.getApplicationId());
		ldap.setDomain(model.getDomain());
		ldap.setUrl(model.getUrl());

		return ldap;
	}

	public AuthSaml toAuthSaml(CompanyModels.AuthSamlModel model) {
		return toAuthSaml(model, new AuthSaml());
	}

	public AuthSaml toAuthSaml(CompanyModels.AuthSamlModel model, AuthSaml saml) {
		if (model == null)
			return null;

		if (saml == null)
			saml = new AuthSaml();

		saml.setEmailAttr(model.getEmailAttr());
		saml.setFirstNameAttr(model.getFirstNameAttr());
		saml.setLastNameAttr(model.getLastNameAttr());
		saml.setIdp(model.getIdp());

		return saml;
	}

	public Subscription toSubscription(SubscriptionModels.SubscriptionModel model) {
		return toSubscription(model, new Subscription());
	}

	public Subscription toSubscription(SubscriptionModels.SubscriptionModel model, Subscription subscription) {

		if (model == null || subscription == null)
			return null;

		subscription.setName(model.getName());
		subscription.setDescription(model.getDescription());
		subscription.setEnabled(model.isEnabled());
		subscription.setCompanyId(model.getCompanyId());
		subscription.setContact(populateContact(model.getContact()));
		subscription.setBillingContact(populateContact(model.getBillingContact()));

		return subscription;
	}

	public Application toApplication(ApplicationModels.ApplicationModel model) {
		return toApplication(model, new Application());
	}

	public Application toApplication(ApplicationModels.ApplicationModel model, Application application) {

		if (model == null || application == null)
			return null;

		application.setName(model.getName());
		application.setDescription(model.getDescription());
		application.setZoneId(model.getZoneId());
		application.setEnabled(model.isEnabled());
		application.setCompanyId(model.getCompanyId());
		application.setProductId(model.getProductId());
		application.setSubscriptionId(model.getSubscriptionId());
		application.setApiSigningRequired(model.getApiSigningRequired());
		application.setApplicationEngineId(model.getApplicationEngineId());
		application.setDefaultSamlEntityId(model.getDefaultSamlEntityId());
		application.setCode(model.getCode());
		application.setProductExtensionIds(model.getProductExtensionIds());
		application.setProductFeatures(model.getProductFeatures());
		application.setConfigurations(toConfigurationProperty(model.getConfigurations()));

		return application;
	}

	public User toUser(UserModels.UserModel model) {

		return toUser(model, new User());
	}

	public User toUser(UserModels.UserModel model, User user) {

		if (model == null || user == null)
			return null;

		// user
		if (StringUtils.isEmpty(user.getId())) {
			user.setLogin(model.getLogin());
			user.setPassword(user.getLogin());
		}

		user.setCompanyId(model.getCompanyId());
		user.setStatus(model.getStatus());
		user.setAdmin(model.isAdmin());

		// contact
		user.setContact(new Contact());
		user.getContact().setCell(model.getCell());
		user.getContact().setEmail(model.getEmail());
		user.getContact().setFax(model.getFax());
		user.getContact().setFirstName(model.getFirstName());
		user.getContact().setMiddleName(model.getMiddleName());
		user.getContact().setLastName(model.getLastName());
		user.getContact().setHome(model.getHome());
		user.getContact().setOffice(model.getOffice());
		user.getContact().setMonitorExt(model.getExtension());

		// address
		user.setAddress(new Address());
		user.getAddress().setAddress1(model.getAddress1());
		user.getAddress().setAddress2(model.getAddress2());
		user.getAddress().setCity(model.getCity());
		user.getAddress().setState(model.getState());
		user.getAddress().setZip(model.getPostalCode());
		user.getAddress().setCountry(model.getCountry());

		// roles
		user.setRoleIds(model.getRoleIds());

		// non-editable properties
		user.getContact().setSipUri("sip:" + model.getEmail());

		user.setParentUserId(model.getParentUserId());

		return user;
	}

	public Role toRole(RoleModels.RoleModel model) {
		return toRole(model, new Role());
	}

	public Role toRole(RoleModels.RoleModel model, Role role) {

		if (model == null || role == null)
			return null;

		role.setName(model.getName());
		role.setDescription(model.getDescription());
		role.setProductId(model.getProductId());
		role.setApplicationId(model.getApplicationId());
		role.setEnabled(model.isEnabled());
		role.setEditable(model.isEditable());
		role.setHidden(model.isHidden());
		role.setPrivilegeIds(model.getPrivilegeIds());

		return role;
	}

	public Privilege toPrivilege(PrivilegeModels.PrivilegeModel model) {
		return toPrivilege(model, new Privilege());
	}

	public Privilege toPrivilege(PrivilegeModels.PrivilegeModel model, Privilege privilege) {

		if (model == null || privilege == null)
			return null;

		privilege.setName(model.getName());
		privilege.setDescription(model.getDescription());
		privilege.setProductId(model.getProductId());
		privilege.setSystemName(model.getSystemName());
		privilege.setEnabled(model.isEnabled());
		privilege.setHidden(model.isHidden());
		privilege.setCategory(model.getCategory());

		return privilege;
	}

	public Region toRegion(RegionModels.RegionModel model) {
		return toRegion(model, new Region());
	}

	public Region toRegion(RegionModels.RegionModel model, Region region) {

		if (model == null || region == null)
			return null;

		region.setName(model.getName());
		region.setDescription(model.getDescription());
		region.setEnabled(model.isEnabled());

		return region;
	}

	public Zone toZone(ZoneModels.ZoneModel model) {
		return toZone(model, new Zone());
	}

	public Zone toZone(ZoneModels.ZoneModel model, Zone zone) {

		if (model == null || zone == null)
			return null;

		zone.setName(model.getName());
		zone.setDescription(model.getDescription());
		zone.setSystemName(model.getSystemName());
		zone.setRegionId(model.getRegionId());
		zone.setEnabled(model.isEnabled());
		zone.setHidden(model.isHidden());

		return zone;
	}

	public UserAccountModel toUserAccountModel(User user) {
		// decrypt user's login
		String decryptedLogin = toDecryptedLogin(user);

		List<String> roles = user.getRefRoles().stream().map(DocumentAbstract::getId).collect(Collectors.toList());

		return new UserAccountModel().withLogin(decryptedLogin).withCompanyId(user.getCompanyId())
				.withAdmin(user.isAdmin()).withStatus(user.getStatus()).withRoleIds(roles);
	}

	public User toUser(UserAccountModel model, User user) {

		if (!StringUtils.isEmpty(model.getLogin())) {
			Crypto crypto = cryptoService.getCrypto();
			user.setHashedLogin(crypto.internalHash(model.getLogin().toLowerCase()));
			user.setLogin(crypto.internalEncrypt(model.getLogin().toLowerCase()));
		}

		user.setAdmin(model.isAdmin());
		user.setStatus(model.getStatus());
		user.setCompanyId(model.getCompanyId());
		user.setRoleIds(model.getRoleIds());

		return user;
	}

	public UserProfileModel toUserProfileModel(User user) {

		final UserProfileModel userProfileModel = new UserProfileModel().withCompanyId(user.getCompanyId());

		Address address = user.getAddress();
		if (address != null) {
			userProfileModel.withAddress1(address.getAddress1()).withAddress2(address.getAddress2())
					.withState(address.getState()).withCity(address.getCity()).withCountry(address.getCountry())
					.withPostCode(address.getZip());
		}

		Contact contact = user.getContact();
		if (contact != null) {
			userProfileModel.withExtension(contact.getMonitorExt()).withFirstName(contact.getFirstName())
					.withLastName(contact.getLastName()).withMiddleName(contact.getMiddleName())
					.withOffice(contact.getOffice()).withFax(contact.getFax()).withCell(contact.getCell())
					.withHome(contact.getHome()).withEmail(contact.getEmail());
		}

		return userProfileModel;
	}

	public User toUserFromProfile(UserModels.UserProfileModel model, User user) {

		if (model == null || user == null)
			return null;

		if (!StringUtils.isEmpty(model.getCompanyId())) {
			user.setCompanyId(model.getCompanyId());
		}

		if (!StringUtils.isEmpty(model.getFirstName())) {
			user.getContact().setFirstName(model.getFirstName());
		}

		if (!StringUtils.isEmpty(model.getLastName())) {
			user.getContact().setLastName(model.getLastName());
		}

		if (!StringUtils.isEmpty(model.getEmail())) {
			user.getContact().setEmail(model.getEmail());
		}

		user.getContact().setMiddleName(model.getMiddleName());
		user.getContact().setCell(model.getCell());
		user.getContact().setFax(model.getFax());
		user.getContact().setHome(model.getHome());
		user.getContact().setOffice(model.getOffice());
		user.getContact().setMonitorExt(model.getExtension());

		if (user.getAddress() == null) {
			user.setAddress(new Address());
		}

		user.getAddress().setAddress1(model.getAddress1());
		user.getAddress().setAddress2(model.getAddress2());
		user.getAddress().setCity(model.getCity());
		user.getAddress().setState(model.getState());
		user.getAddress().setZip(model.getPostalCode());
		user.getAddress().setCountry(model.getCountry());

		user.setParentUserId(model.getParentUserId());

		return user;
	}

	public UserAuthenticationUpsert toUserAuthenticationUpsert(User user, List<Auth> authorizations,
			List<Auth> authOptions) {

		// decrypt user's login
		String decryptedLogin = toDecryptedLogin(user);

		List<CompanyModels.CompanyAuthOption> authOptionModels = authOptions.stream().map(this::mapAuthToOptionModel)
				.collect(Collectors.toList());

		List<UserModels.UserAuthenticationModel> authentications = user.getAuths().stream().map(userAuth -> {

			Auth auth = authorizations.stream()
					.filter(authentication -> authentication.getId().equals(userAuth.getRefId())).findFirst()
					.orElse(null);

			if (auth == null)
				return null;

			return toUserAuthenticationModel(auth, userAuth.getPrincipal(), userAuth.isEnabled());
		}).filter(Objects::nonNull).collect(Collectors.toList());

		return new UserAuthenticationUpsert(authentications, authOptionModels, user, decryptedLogin);
	}

	public UserModels.UserAuthenticationModel toUserAuthenticationModel(Auth auth, String principal, boolean enabled) {

		switch (auth.getType()) {

		case LDAP:
			return new UserModels.UserAuthenticationModel().withAuthId(auth.getId()).withAuthHid(auth.getHid())
					.withType(auth.getType()).withPrincipal(principal).withProvider(auth.getLdap().getDomain())
					.withEnabled(enabled);

		case SAML:
			return new UserModels.UserAuthenticationModel().withAuthId(auth.getId()).withAuthHid(auth.getHid())
					.withType(auth.getType()).withPrincipal(principal).withProvider(auth.getSaml().getIdp())
					.withEnabled(enabled);

		default:
			throw new AcsLogicalException("Unsupported type! type=" + auth.getType());
		}
	}

	private CompanyModels.CompanyAuthOption mapAuthToOptionModel(Auth auth) {

		final CompanyModels.CompanyAuthOption companyAuthOption = new CompanyModels.CompanyAuthOption()
				.withId(auth.getId()).withHid(auth.getHid()).withType(auth.getType()).withEnabled(auth.isEnabled());

		switch (auth.getType()) {

		case LDAP:
			return companyAuthOption.withProvider(auth.getLdap().getDomain());

		case SAML:
			return companyAuthOption.withProvider(auth.getSaml().getIdp());

		default:
			throw new AcsLogicalException("Unsupported type! type=" + auth.getType());
		}
	}

	public UserAuth toUserAuthFromModel(UserModels.UserAuthenticationModel model) {

		UserAuth userAuth = new UserAuth();
		userAuth.setPrincipal(model.getPrincipal());
		userAuth.setType(model.getType());
		userAuth.setRefId(model.getProvider());
		userAuth.setEnabled(model.isEnabled());

		return userAuth;
	}

	public AccessKeyModelAbstract<? extends AccessKeyModelAbstract<?>> toAccessKeyModel(AccessKey accessKey,
			Application application, AccessKeyModelAbstract<? extends AccessKeyModelAbstract<?>> accessKeyModel) {

		String rawApiKey = cryptoService.decrypt(application.getId(), accessKey.getEncryptedApiKey());
		String rawSecretKey = cryptoService.decrypt(application.getId(), accessKey.getEncryptedSecretKey());

		CryptoClientImpl cryptoClient256 = new CryptoClientImpl();
		CryptoClient128Impl cryptoClient128 = new CryptoClient128Impl();

		accessKeyModel.withCompanyId(accessKey.getCompanyId()).withName(accessKey.getName())
				.withAes128ApiKey(cryptoClient128.internalEncrypt(rawApiKey))
				.withAes128SecretKey(cryptoClient128.internalEncrypt(rawSecretKey))
				.withAes256ApiKey(cryptoClient256.internalEncrypt(rawApiKey))
				.withAes256SecretKey(cryptoClient256.internalEncrypt(rawSecretKey))
				.withExpiration(String.valueOf(accessKey.getExpiration())).withOwnerDisplayName(accessKey.getName());

		accessKey = toEncryptedAccessKeyFields(application, accessKey);

		List<AccessKeyModelAbstract.AccessPrivilegeModel> accessPrivilegeModels = accessKey.getPrivileges().stream()
				.map(accessPrivilege -> new AccessKeyModelAbstract.AccessPrivilegeModel(accessPrivilege.getLevel(),
						accessPrivilege.getPri()))
				.collect(Collectors.toList());

		accessKeyModel.withAccessPrivilege(accessPrivilegeModels).withRawApiKey(accessKey.getEncryptedApiKey())
				.withRawSecretKey(accessKey.getEncryptedSecretKey());

		return accessKeyModel;
	}

	public AccessKey toAccessKey(AccessKeyModelAbstract<? extends AccessKeyModelAbstract<?>> model,
			AccessKey accessKey) {

		if (model == null || accessKey == null) {
			return null;
		}

		accessKey.setName(model.getName());
		accessKey.setCompanyId(model.getCompanyId());

		accessKey.setPrivileges(model.getPrivileges().stream().map(priModel -> {
			AccessPrivilege pri = new AccessPrivilege();
			pri.setLevel(priModel.getLevel());
			pri.setPri(priModel.getPri());
			return pri;
		}).collect(Collectors.toList()));

		return accessKey;
	}

	public AccessKey toEncryptedAccessKeyFields(Application application, AccessKey accKey) {
		if (accKey == null)
			return accKey;

		try {
			if (application != null && !accKey.isOwner(application)) {
				accKey.setEncryptedApiKey(cryptoService.decrypt(application.getId(), accKey.getEncryptedApiKey()));
				accKey.setEncryptedSecretKey(
						cryptoService.decrypt(application.getId(), accKey.getEncryptedSecretKey()));
			} else {
				accKey.setEncryptedApiKey(cryptoService.decrypt(accKey.getEncryptedApiKey()));
				accKey.setEncryptedSecretKey(cryptoService.decrypt(accKey.getEncryptedSecretKey()));

			}
		} catch (Throwable e) {
			throw new AcsLogicalException(
					"Unable to decrypt accessKey encrypted fields. AccessKey Id: " + accKey.getId(), e);
		}

		return accKey;
	}
}
