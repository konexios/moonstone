package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.security.AuthType;
import com.arrow.pegasus.webapi.data.CoreCompanyModels;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class CompanyModels extends CoreCompanyModels {

	public static class CompanyFilterOptionsModel implements Serializable {
		private static final long serialVersionUID = -2496630435463781969L;

		private List<CompanyModels.CompanyOption> companyOptions;
		private CompanyStatus[] statusOptions;

		public CompanyFilterOptionsModel() {
		}

		public List<CompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public void setCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			this.companyOptions = companyOptions;
		}

		public CompanyFilterOptionsModel withCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			setCompanyOptions(companyOptions);

			return this;
		}

		public CompanyStatus[] getStatusOptions() {
			return statusOptions;
		}

		public void setStatusOptions(CompanyStatus[] statusOptions) {
			this.statusOptions = statusOptions;
		}

		public CompanyFilterOptionsModel withStatusOptions(CompanyStatus[] statusOptions) {
			setStatusOptions(statusOptions);

			return this;
		}
	}

	public static class CompanyListModel extends CoreDocumentModel {
		private static final long serialVersionUID = 3681721542157882710L;

		private String name;
		private String abbrName;
		private CompanyStatus status;
		private String contactName;
		private String billingContactName;
		private String parentCompanyName;

		public CompanyListModel(String id, String hid) {
			super(id, hid);
		}

		public CompanyListModel(Company company) {
			super(company.getId(), company.getHid());
			this.name = company.getName();
			this.abbrName = company.getAbbrName();
			this.status = company.getStatus();
			this.contactName = company.getContact() == null ? "" : company.getContact().fullName();
			this.billingContactName = company.getBillingContact() == null ? "" : company.getBillingContact().fullName();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public CompanyListModel withName(String name) {
			setName(name);

			return this;
		}

		public String getAbbrName() {
			return abbrName;
		}

		public void setAbbrName(String abbrName) {
			this.abbrName = abbrName;
		}

		public CompanyListModel withAbbrName(String abbrName) {
			setAbbrName(abbrName);

			return this;
		}

		public CompanyStatus getStatus() {
			return status;
		}

		public void setStatus(CompanyStatus status) {
			this.status = status;
		}

		public CompanyListModel withStatus(CompanyStatus status) {
			setStatus(status);

			return this;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public CompanyListModel withContactName(String contactName) {
			setContactName(contactName);

			return this;
		}

		public String getBillingContactName() {
			return billingContactName;
		}

		public void setBillingContactName(String billingContactName) {
			this.billingContactName = billingContactName;
		}

		public CompanyListModel withBillingContactName(String billingContactName) {
			setBillingContactName(billingContactName);

			return this;
		}

		public String getParentCompanyName() {
			return parentCompanyName;
		}

		public void setParentCompanyName(String parentCompanyName) {
			this.parentCompanyName = parentCompanyName;
		}

		public CompanyListModel withParentCompanyName(String parentCompanyName) {
			setParentCompanyName(parentCompanyName);
			return this;
		}
	}

	public static class CompanyModel extends ModelAbstract<CompanyModel> {
		private static final long serialVersionUID = -6970035975791505380L;

		private String name;
		private String abbrName;
		private CompanyStatus status;
		private String parentCompanyId;
		private AddressModel address;
		private ContactModel contact;

		public CompanyModel() {
		}

		@Override
		protected CompanyModel self() {
			return this;
		}

		public CompanyModel withName(String name) {
			setName(name);
			return self();
		}

		public CompanyModel withAbbrName(String abbrName) {
			setAbbrName(abbrName);
			return self();
		}

		public CompanyModel withStatus(CompanyStatus status) {
			setStatus(status);
			return self();
		}

		public CompanyModel withParentCompanyId(String parentCompanyId) {
			setParentCompanyId(parentCompanyId);
			return self();
		}

		public CompanyModel withAddress(AddressModel address) {
			setAddress(address);
			return self();
		}

		public CompanyModel withContact(ContactModel contact) {
			setContact(contact);
			return self();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAbbrName() {
			return abbrName;
		}

		public void setAbbrName(String abbrName) {
			this.abbrName = abbrName;
		}

		public CompanyStatus getStatus() {
			return status;
		}

		public void setStatus(CompanyStatus status) {
			this.status = status;
		}

		public String getParentCompanyId() {
			return parentCompanyId;
		}

		public void setParentCompanyId(String parentCompanyId) {
			this.parentCompanyId = parentCompanyId;
		}

		public AddressModel getAddress() {
			return address;
		}

		public void setAddress(AddressModel address) {
			this.address = address;
		}

		public ContactModel getContact() {
			return contact;
		}

		public void setContact(ContactModel contact) {
			this.contact = contact;
		}
	}

	public static class CompanyBillingModel extends ModelAbstract<CompanyBillingModel> {
		private static final long serialVersionUID = -5908967135522409732L;

		private AddressModel address;
		private ContactModel contact;

		@Override
		protected CompanyBillingModel self() {
			return this;
		}

		public CompanyBillingModel withAddress(AddressModel address) {
			setAddress(address);
			return this;
		}

		public CompanyBillingModel withContact(ContactModel contact) {
			setContact(contact);
			return this;
		}

		public AddressModel getAddress() {
			return address;
		}

		public void setAddress(AddressModel address) {
			this.address = address;
		}

		public ContactModel getContact() {
			return contact;
		}

		public void setContact(ContactModel contact) {
			this.contact = contact;
		}
	}

	public static class AuthLdapModel implements Serializable {
		private static final long serialVersionUID = -635993272629480440L;

		private String applicationName;
		private String applicationId;
		private String domain;
		private String url;

		public AuthLdapModel withApplicationName(String applicationName) {
			setApplicationName(applicationName);

			return this;
		}

		public AuthLdapModel withApplicationId(String applicationId) {
			setApplicationId(applicationId);

			return this;
		}

		public AuthLdapModel withDomain(String domain) {
			setDomain(domain);

			return this;
		}

		public AuthLdapModel withUrl(String url) {
			setUrl(url);

			return this;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public void setApplicationName(String applicationName) {
			this.applicationName = applicationName;
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	public static class AuthSamlModel implements Serializable {
		private static final long serialVersionUID = 5773113039266312362L;

		private String idp;
		private String firstNameAttr;
		private String lastNameAttr;
		private String emailAttr;

		public AuthSamlModel withIdp(String idp) {
			setIdp(idp);

			return this;
		}

		public AuthSamlModel withFirstNameAttr(String firstNameAttr) {
			setFirstNameAttr(firstNameAttr);

			return this;
		}

		public AuthSamlModel withLastNameAttr(String lastNameAttr) {
			setLastNameAttr(lastNameAttr);

			return this;
		}

		public AuthSamlModel withEmailAttr(String emailAttr) {
			setEmailAttr(emailAttr);

			return this;
		}

		public String getIdp() {
			return idp;
		}

		public void setIdp(String idp) {
			this.idp = idp;
		}

		public String getFirstNameAttr() {
			return firstNameAttr;
		}

		public void setFirstNameAttr(String firstNameAttr) {
			this.firstNameAttr = firstNameAttr;
		}

		public String getLastNameAttr() {
			return lastNameAttr;
		}

		public void setLastNameAttr(String lastNameAttr) {
			this.lastNameAttr = lastNameAttr;
		}

		public String getEmailAttr() {
			return emailAttr;
		}

		public void setEmailAttr(String emailAttr) {
			this.emailAttr = emailAttr;
		}
	}

	public static class CompanyAuthModel extends ModelAbstract<CompanyAuthModel> {
		private static final long serialVersionUID = 8879380901107214057L;

		private String companyId;
		private AuthType type;
		private AuthLdapModel ldap;
		private AuthSamlModel saml;
		private boolean enabled;

		public CompanyAuthModel() {
		}

		@Override
		protected CompanyAuthModel self() {
			return this;
		}

		public CompanyAuthModel withCompanyId(String companyId) {
			setCompanyId(companyId);

			return self();
		}

		public CompanyAuthModel withType(AuthType type) {
			setType(type);

			return self();
		}

		public CompanyAuthModel withLdap(AuthLdapModel ldap) {
			setLdap(ldap);

			return self();
		}

		public CompanyAuthModel withSaml(AuthSamlModel saml) {
			setSaml(saml);

			return self();
		}

		public CompanyAuthModel withEnabled(boolean enabled) {
			setEnabled(enabled);

			return self();
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public AuthType getType() {
			return type;
		}

		public void setType(AuthType type) {
			this.type = type;
		}

		public AuthLdapModel getLdap() {
			return ldap;
		}

		public void setLdap(AuthLdapModel ldap) {
			this.ldap = ldap;
		}

		public AuthSamlModel getSaml() {
			return saml;
		}

		public void setSaml(AuthSamlModel saml) {
			this.saml = saml;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class CompanyAuthUpsertModel implements Serializable {
		private static final long serialVersionUID = 5695368627796045398L;

		private CompanyAuthModel companyAuth;
		private List<ApplicationModels.ApplicationOption> ldapApplications;

		public CompanyAuthUpsertModel withCompanyAuth(CompanyAuthModel companyAuth) {
			setCompanyAuth(companyAuth);
			return this;
		}

		public CompanyAuthUpsertModel withLdapApplications(List<ApplicationModels.ApplicationOption> ldapApplications) {
			setLdapApplications(ldapApplications);
			return this;
		}

		public CompanyAuthModel getCompanyAuth() {
			return companyAuth;
		}

		public void setCompanyAuth(CompanyAuthModel companyAuth) {
			this.companyAuth = companyAuth;
		}

		public List<ApplicationModels.ApplicationOption> getLdapApplications() {
			return ldapApplications;
		}

		public void setLdapApplications(List<ApplicationModels.ApplicationOption> ldapApplications) {
			this.ldapApplications = ldapApplications;
		}
	}

	public static class CompanyAuthenticationModel extends ModelAbstract<CompanyAuthenticationModel> {
		private static final long serialVersionUID = 8582487803206396684L;

		private List<CompanyAuthModel> ldapAuths;
		private List<CompanyAuthModel> samlAuths;
		private PasswordPolicyModel passwordPolicy;
		private LoginPolicyModel loginPolicy;

		public CompanyAuthenticationModel() {
			ldapAuths = new ArrayList<>();
			samlAuths = new ArrayList<>();
			passwordPolicy = new PasswordPolicyModel();
			loginPolicy = new LoginPolicyModel();
		}

		@Override
		protected CompanyAuthenticationModel self() {
			return this;
		}

		public CompanyAuthenticationModel withLdapAuths(List<CompanyAuthModel> ldapAuths) {
			setLdapAuths(ldapAuths);

			return self();
		}

		public CompanyAuthenticationModel withSamlAuths(List<CompanyAuthModel> samlAuths) {
			setSamlAuths(samlAuths);

			return self();
		}

		public CompanyAuthenticationModel withPasswordPolicy(PasswordPolicyModel passwordPolicy) {
			setPasswordPolicy(passwordPolicy);
			return self();
		}

		public CompanyAuthenticationModel withLoginPolicy(LoginPolicyModel loginPolicy) {
			setLoginPolicy(loginPolicy);
			return self();
		}

		public List<CompanyAuthModel> getLdapAuths() {
			return ldapAuths;
		}

		public void setLdapAuths(List<CompanyAuthModel> ldapAuths) {
			this.ldapAuths = ldapAuths;
		}

		public List<CompanyAuthModel> getSamlAuths() {
			return samlAuths;
		}

		public void setSamlAuths(List<CompanyAuthModel> samlAuths) {
			this.samlAuths = samlAuths;
		}

		public PasswordPolicyModel getPasswordPolicy() {
			return passwordPolicy;
		}

		public void setPasswordPolicy(PasswordPolicyModel passwordPolicy) {
			this.passwordPolicy = passwordPolicy;
		}

		public LoginPolicyModel getLoginPolicy() {
			return loginPolicy;
		}

		public void setLoginPolicy(LoginPolicyModel loginPolicy) {
			this.loginPolicy = loginPolicy;
		}
	}

	public static class CompanyAuthOption extends ModelAbstract<CompanyAuthOption> {


		private static final long serialVersionUID = 1302818674602052624L;

		private String companyId;
		private AuthType type;
		private String provider;
		private boolean enabled;

		public CompanyAuthOption() {
		}

		@Override
		protected CompanyAuthOption self() {
			return this;
		}

		public CompanyAuthOption withCompanyId(String companyId) {
			setCompanyId(companyId);

			return self();
		}

		public CompanyAuthOption withType(AuthType type) {
			setType(type);

			return self();
		}

		public CompanyAuthOption withProvider(String providerName) {
			setProvider(providerName);
			return self();
		}


		public CompanyAuthOption withEnabled(boolean enabled) {
			setEnabled(enabled);

			return self();
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public AuthType getType() {
			return type;
		}

		public void setType(AuthType type) {
			this.type = type;
		}

		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class SubscriptionModel extends ModelAbstract<SubscriptionModel> {
		private static final long serialVersionUID = -4025427751814215286L;

		private String name;
		private String description;
		private Boolean enabled;
		private Instant startDate;
		private Instant endDate;

		@Override
		protected SubscriptionModel self() {
			return this;
		}

		public String getName() {
			return name;
		}

		public SubscriptionModel withName(String name) {
			setName(name);
			return self();
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public SubscriptionModel withDescription(String description) {
			setDescription(description);
			return self();
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Boolean getEnabled() {
			return enabled;
		}

		public SubscriptionModel withEnabled(Boolean enabled) {
			setEnabled(enabled);
			return self();
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		public Instant getStartDate() {
			return startDate;
		}

		public SubscriptionModel withStartDate(Instant startDate) {
			setStartDate(startDate);
			return self();
		}

		public void setStartDate(Instant startDate) {
			this.startDate = startDate;
		}

		public Instant getEndDate() {
			return endDate;
		}

		public SubscriptionModel withEndDate(Instant endDate) {
			setEndDate(endDate);
			return self();
		}

		public void setEndDate(Instant endDate) {
			this.endDate = endDate;
		}
	}

	public static class CompanyUpsertModel implements Serializable {
		private static final long serialVersionUID = 33369197480553791L;

		private CompanyModel company;
		private List<CompanyModels.CompanyOption> parentCompanyOptions;
		private List<CoreCompanyModels.CompanyStatusOption> statusOptions;

		public CompanyUpsertModel() {
		}

		public CompanyUpsertModel(CompanyModels.CompanyModel company,
		        List<CoreCompanyModels.CompanyStatusOption> statusOptions) {
			this.company = company;
			this.statusOptions = statusOptions;
		}

		public CompanyModel getCompany() {
			return company;
		}

		public void setCompany(CompanyModel company) {
			this.company = company;
		}

		public CompanyUpsertModel withCompany(CompanyModel company) {
			setCompany(company);

			return this;
		}

		public List<CompanyModels.CompanyOption> getParentCompanyOptions() {
			return parentCompanyOptions;
		}

		public void setParentCompanyOptions(List<CompanyModels.CompanyOption> parentCompanyOptions) {
			this.parentCompanyOptions = parentCompanyOptions;
		}

		public CompanyUpsertModel withParentCompanyOptions(List<CompanyModels.CompanyOption> parentCompanyOptions) {
			setParentCompanyOptions(parentCompanyOptions);

			return this;
		}

		public List<CoreCompanyModels.CompanyStatusOption> getStatusOptions() {
			return statusOptions;
		}

		public void setStatusOptions(List<CoreCompanyModels.CompanyStatusOption> statusOptions) {
			this.statusOptions = statusOptions;
		}

		public CompanyUpsertModel withStatusOptions(List<CoreCompanyModels.CompanyStatusOption> statusOptions) {
			setStatusOptions(statusOptions);

			return this;
		}
	}

	public static class ApplicationModel extends ApplicationModelAbstract<ApplicationModel> {
		private static final long serialVersionUID = -8221721507862395699L;

		@Override
		protected ApplicationModel self() {
			return this;
		}
	}

	public static class AccessKeyModel extends AccessKeyModelAbstract<AccessKeyModel> {
		private static final long serialVersionUID = -3119455753209570700L;

		public AccessKeyModel() {}

		public AccessKeyModel(AccessKey accessKey) {
			super(accessKey);
		}
		
		@Override
		protected AccessKeyModel self() {
			return this;
		}
	}
}
