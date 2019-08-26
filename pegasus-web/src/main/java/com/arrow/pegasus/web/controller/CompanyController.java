package com.arrow.pegasus.web.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
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

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.AuthLdap;
import com.arrow.pegasus.data.security.AuthSaml;
import com.arrow.pegasus.data.security.AuthType;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.arrow.pegasus.repo.params.CompanySearchParams;
import com.arrow.pegasus.security.CryptoClient128Impl;
import com.arrow.pegasus.security.CryptoClientImpl;
import com.arrow.pegasus.web.model.AccessKeyModelAbstract;
import com.arrow.pegasus.web.model.ApplicationModels;
import com.arrow.pegasus.web.model.CompanyModels;
import com.arrow.pegasus.web.model.CompanyModels.AuthLdapModel;
import com.arrow.pegasus.web.model.CompanyModels.AuthSamlModel;
import com.arrow.pegasus.web.model.CompanyModels.CompanyAuthModel;
import com.arrow.pegasus.web.model.CompanyModels.CompanyFilterOptionsModel;
import com.arrow.pegasus.web.model.CompanyModels.CompanyUpsertModel;
import com.arrow.pegasus.web.model.LoginPolicyModel;
import com.arrow.pegasus.web.model.PasswordPolicyModel;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;
import com.arrow.pegasus.web.model.SearchResultModels.AccessKeySearchResult;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;

import moonstone.acs.AcsLogicalException;

@RestController
@RequestMapping("/api/pegasus/companies")
public class CompanyController extends PegasusControllerAbstract {

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public List<CompanyModels.CompanyOption> options() {
		String method = "options";
		logDebug(method, "...");

		User authenticatedUser = getAuthenticatedUser();

		return getCompanyOptions(authenticatedUser.getCompanyId(), EnumSet.of(CompanyStatus.Active), true,
				authenticatedUser.isAdmin());
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	public CompanyModels.CompanyFilterOptionsModel filterOptions() {
		String method = "filterOptions";
		logDebug(method, "...");

		User authenticatedUser = getAuthenticatedUser();

		List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		// company status options
		CompanyStatus[] statusOptions = CompanyStatus.values();

		// convert to model
		CompanyFilterOptionsModel model = new CompanyModels.CompanyFilterOptionsModel()
				.withCompanyOptions(companyOptions).withStatusOptions(statusOptions);

		return model;
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.CompanySearchResult find(
			@RequestBody SearchFilterModels.CompanySearchFilter searchFilter) {

		User authenticatedUser = getAuthenticatedUser();

		// search params
		CompanySearchParams params = new CompanySearchParams();
		params.setStatuses(searchFilter.getStatuses() != null && searchFilter.getStatuses().length > 0
				? EnumSet.copyOf(Arrays.asList(searchFilter.getStatuses()))
				: EnumSet.noneOf(CompanyStatus.class));
		params.setName(searchFilter.getName());
		params.setAbbrName(searchFilter.getAbbrName());

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		Page<Company> companyPage = null;
		if (authenticatedUser.isAdmin()) {
			params.addParentCompanyIds(searchFilter.getParentCompanyIds());
			// lookup
			companyPage = getCompanyService().getCompanyRepository().findCompanies(pageRequest, params);
		} else {
			// lookup
			companyPage = getCompanyService().getCompanyRepository().findByCompanyIdOrParentCompanyId(pageRequest,
					authenticatedUser.getCompanyId(), params);
		}

		// convert to visual model
		Page<CompanyModels.CompanyListModel> result = null;
		List<CompanyModels.CompanyListModel> companyModels = new ArrayList<>();
		for (Company company : companyPage) {
			company = getCoreCacheHelper().populateCompany(company);
			companyModels.add(new CompanyModels.CompanyListModel(company.getId(), company.getHid())
					.withName(company.getName()).withAbbrName(company.getAbbrName()).withStatus(company.getStatus())
					.withContactName(
							company.getContact() == null || (StringUtils.isEmpty(company.getContact().getFirstName())
									&& StringUtils.isEmpty(company.getContact().getLastName())) ? ""
											: company.getContact().fullName())
					.withBillingContactName(company.getBillingContact() == null
							|| (StringUtils.isEmpty(company.getBillingContact().getFirstName())
									&& StringUtils.isEmpty(company.getBillingContact().getLastName())) ? ""
											: company.getBillingContact().fullName())
					.withParentCompanyName(
							company.getRefParentCompany() == null ? "" : company.getRefParentCompany().getName()));
		}
		result = new PageImpl<>(companyModels, pageRequest, companyPage.getTotalElements());

		return new SearchResultModels.CompanySearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY')")
	@RequestMapping(value = "/{id}/company", method = RequestMethod.GET)
	public CompanyModels.CompanyUpsertModel company(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		String method = "company";

		Company company = new Company();

		if (!id.equalsIgnoreCase("new")) {
			company = getCompanyService().getCompanyRepository().findById(id).orElse(null);
			Assert.notNull(company, "company is null");
			Assert.isTrue(validateUser(company.getId()),
					"The user must be an administrator or either the user's tenant or sub-tenants must be the tenant.");
		}

		User authenticatedUser = getAuthenticatedUser();
		Company usersCompany = getCoreCacheService().findCompanyById(authenticatedUser.getCompanyId());
		Assert.notNull(usersCompany, "Company not found! companyId=" + authenticatedUser.getCompanyId());

		// status options
		List<CompanyModels.CompanyStatusOption> statusOptions = new ArrayList<>();
		for (CompanyStatus companyStatus : CompanyStatus.values())
			statusOptions.add(new CompanyModels.CompanyStatusOption(companyStatus));

		List<CompanyModels.CompanyOption> filteredParentCompanyOptions = new ArrayList<>();
		// is the company in scope the authenticated users company
		if (id.equals(authenticatedUser.getCompanyId())) {
			if (authenticatedUser.isAdmin()) {
				// company options
				filteredParentCompanyOptions.addAll(getCompanyOptions(authenticatedUser.getCompanyId(),
						EnumSet.allOf(CompanyStatus.class), true, authenticatedUser.isAdmin()));
			} else if (!StringUtils.isEmpty(usersCompany.getParentCompanyId())) {
				Company usersParentCompany = getCoreCacheService().findCompanyById(usersCompany.getParentCompanyId());
				filteredParentCompanyOptions.add(new CompanyModels.CompanyOption(usersParentCompany));
			}
		} else {
			List<CompanyModels.CompanyOption> parentCompanyOptions = new ArrayList<>();
			if (authenticatedUser.isAdmin()) {
				// company options
				parentCompanyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
						EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());
			} else {
				// limited to only user's company as of R9S3
				parentCompanyOptions.add(new CompanyModels.CompanyOption(usersCompany));
			}
			filteredParentCompanyOptions.addAll(parentCompanyOptions);
		}

		// remove the company in scope from list
		if (!id.equalsIgnoreCase("new"))
			for (Iterator<CompanyModels.CompanyOption> iter = filteredParentCompanyOptions.iterator(); iter.hasNext();)
				if (iter.next().getId().equals(id))
					iter.remove();

		// sort by name
		filteredParentCompanyOptions.sort(Comparator.comparing(CompanyModels.CompanyOption::getName));
		logDebug(method, "filteredParentCompanyOptions: %s", filteredParentCompanyOptions.size());

		// convert to model
		CompanyUpsertModel model = new CompanyModels.CompanyUpsertModel()
				.withCompany(getPegasusModelUtil().toCompanyModel(company))
				.withParentCompanyOptions(filteredParentCompanyOptions).withStatusOptions(statusOptions);

		return model;
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_COMPANY')")
	@RequestMapping(path = "/{id}/company", method = RequestMethod.POST)
	public CompanyModels.CompanyModel createCompany(@PathVariable String id,
			@RequestBody CompanyModels.CompanyModel model) {
		Assert.hasText(id, "id is empty");
		Assert.isTrue(id.equalsIgnoreCase("new"), "Invalid id, id must equal 'new'");
		Assert.notNull(model, "company is null");

		// convert from model
		Company company = getPegasusModelUtil().toCompany(model);
		// persist
		company = getCompanyService().create(company, getUserId());
		// lookup
		company = getCompanyService().getCompanyRepository().findById(company.getId()).orElse(null);

		// convert to model and return
		return getPegasusModelUtil().toCompanyModel(company);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_COMPANY')")
	@RequestMapping(path = "{id}/company", method = RequestMethod.PUT)
	public CompanyModels.CompanyModel updateCompany(@PathVariable String id,
			@RequestBody CompanyModels.CompanyModel model) {
		Assert.hasText(id, "id is empty");
		Assert.notNull(model, "company is null");

		Company company = getCoreCacheService().findCompanyById(id);
		Assert.notNull(company, "company not found :: companyId=[" + model.getId() + "]");

		// convert from model
		company = getPegasusModelUtil().toCompany(model, company);
		// persist
		company = getCompanyService().update(company, getUserId());
		// lookup
		company = getCompanyService().getCompanyRepository().findById(id).orElse(null);

		// convert to model and return
		return getPegasusModelUtil().toCompanyModel(company);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_BILLING')")
	@RequestMapping(value = "{companyId}/billing", method = RequestMethod.GET)
	public CompanyModels.CompanyBillingModel billing(@PathVariable String companyId) {
		Assert.hasText(companyId, "companyId is null");

		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		return new CompanyModels.CompanyBillingModel().withId(company.getId()).withHid(company.getHid())
				.withAddress(getPegasusModelUtil().toAddressModel(company.getBillingAddress()))
				.withContact(getPegasusModelUtil().toContactModel(company.getBillingContact()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_COMPANY_BILLING')")
	@RequestMapping(value = "{companyId}/billing", method = RequestMethod.PUT)
	public CompanyModels.CompanyBillingModel updateBilling(@PathVariable String companyId,
			@RequestBody CompanyModels.CompanyBillingModel model) {
		Assert.hasText(companyId, "companyId is null");
		Assert.notNull(model, "model is null");

		// must always lookup company, billing information are nested objects
		// persisted with the company object
		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		// populate
		company.setBillingAddress(getPegasusModelUtil().toAddress(model.getAddress(), company.getBillingAddress()));
		company.setBillingContact(getPegasusModelUtil().toContact(model.getContact(), company.getBillingContact()));

		// update
		company = getCompanyService().update(company, getUserId());

		return new CompanyModels.CompanyBillingModel().withId(company.getId()).withHid(company.getHid())
				.withAddress(getPegasusModelUtil().toAddressModel(company.getBillingAddress()))
				.withContact(getPegasusModelUtil().toContactModel(company.getBillingContact()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_AUTHENTICATION')")
	@RequestMapping(value = "{companyId}/authentication", method = RequestMethod.GET)
	public CompanyModels.CompanyAuthenticationModel authentication(@PathVariable String companyId) {
		Assert.hasText(companyId, "companyId is null");

		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		// convert to models
		List<CompanyAuthModel> ldapAuths = new ArrayList<>();
		List<CompanyAuthModel> samlAuths = new ArrayList<>();
		getAuthService().getAuthRepository().findAllByCompanyId(companyId).stream().forEach(auth -> {
			switch (auth.getType()) {
			case LDAP:
				Application application = getCoreCacheService().findApplicationById(auth.getLdap().getApplicationId());
				Assert.notNull(application,
						"Unable to find application! applicationId=" + auth.getLdap().getApplicationId());
				ldapAuths.add(new CompanyAuthModel().withCompanyId(company.getId()).withId(auth.getId())
						.withHid(auth.getHid()).withType(auth.getType()).withEnabled(auth.isEnabled())
						.withLdap(new AuthLdapModel().withApplicationName(application.getName())
								.withApplicationId(auth.getLdap().getApplicationId())
								.withDomain(auth.getLdap().getDomain()).withUrl(auth.getLdap().getUrl())));
				break;
			case SAML:
				samlAuths.add(new CompanyAuthModel().withCompanyId(company.getId()).withId(auth.getId())
						.withHid(auth.getHid()).withType(auth.getType()).withEnabled(auth.isEnabled())
						.withSaml(new AuthSamlModel().withEmailAttr(auth.getSaml().getEmailAttr())
								.withFirstNameAttr(auth.getSaml().getFirstNameAttr())
								.withLastNameAttr(auth.getSaml().getLastNameAttr()).withIdp(auth.getSaml().getIdp())));
				break;
			default:
				throw new AcsLogicalException("Unsupported type: type=" + auth.getType());
			}
		});

		return new CompanyModels.CompanyAuthenticationModel().withId(company.getId()).withHid(company.getHid())
				.withPasswordPolicy(getPegasusModelUtil().toPasswordPolicyModel(company.getPasswordPolicy()))
				.withLoginPolicy(getPegasusModelUtil().toLoginPolicyModel(company.getLoginPolicy()))
				.withLdapAuths(ldapAuths).withSamlAuths(samlAuths);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_AUTHENTICATION')")
	@RequestMapping(value = "{companyId}/authentication/passwordpolicy", method = RequestMethod.GET)
	public CompanyModels.CompanyAuthenticationModel passwordPolicy(@PathVariable String companyId) {
		Assert.hasText(companyId, "companyId is null");

		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		// convert to model and return
		return new CompanyModels.CompanyAuthenticationModel().withId(company.getId()).withHid(company.getHid())
				.withPasswordPolicy(getPegasusModelUtil().toPasswordPolicyModel(company.getPasswordPolicy()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_COMPANY_PASSWORD_POLICY')")
	@RequestMapping(value = "{companyId}/authentication/passwordpolicy", method = RequestMethod.PUT)
	public CompanyModels.CompanyAuthenticationModel updatePasswordPolicy(@PathVariable String companyId,
			@RequestBody PasswordPolicyModel model) {
		Assert.hasText(companyId, "companyId is null");
		Assert.notNull(model, "model is null");

		// must always lookup company, security information are nested objects
		// persisted with the company object
		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		// populate
		company.setPasswordPolicy(getPegasusModelUtil().toPasswordPolicy(model, company.getPasswordPolicy()));

		// update
		company = getCompanyService().update(company, getUserId());

		return new CompanyModels.CompanyAuthenticationModel().withId(company.getId()).withHid(company.getHid())
				.withPasswordPolicy(getPegasusModelUtil().toPasswordPolicyModel(company.getPasswordPolicy()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_AUTHENTICATION')")
	@RequestMapping(value = "{companyId}/authentication/loginpolicy", method = RequestMethod.GET)
	public CompanyModels.CompanyAuthenticationModel loginPolicy(@PathVariable String companyId) {
		Assert.hasText(companyId, "companyId is null");

		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		// convert to model and return
		return new CompanyModels.CompanyAuthenticationModel().withId(company.getId()).withHid(company.getHid())
				.withLoginPolicy(getPegasusModelUtil().toLoginPolicyModel(company.getLoginPolicy()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_COMPANY_LOGIN_POLICY')")
	@RequestMapping(value = "{companyId}/authentication/loginpolicy", method = RequestMethod.PUT)
	public CompanyModels.CompanyAuthenticationModel updateLoginPolicy(@PathVariable String companyId,
			@RequestBody LoginPolicyModel model) {
		Assert.hasText(companyId, "companyId is null");
		Assert.notNull(model, "model is null");

		// must always lookup company, security information are nested objects
		// persisted with the company object
		Company company = getCompanyService().getCompanyRepository().findById(companyId).orElse(null);
		Assert.notNull(company, "Unable to find company! companyId=" + companyId);

		// populate
		company.setLoginPolicy(getPegasusModelUtil().toLoginPolicy(model, company.getLoginPolicy()));

		// update
		company = getCompanyService().update(company, getUserId());

		return new CompanyModels.CompanyAuthenticationModel().withId(company.getId()).withHid(company.getHid())
				.withLoginPolicy(getPegasusModelUtil().toLoginPolicyModel(company.getLoginPolicy()));
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_AUTHENTICATION')")
	@RequestMapping(value = "{companyId}/authentication/{authId}/auth/{authType}", method = RequestMethod.GET)
	public CompanyModels.CompanyAuthUpsertModel auth(@PathVariable String companyId, @PathVariable String authId,
			@PathVariable String authType) {
		Assert.hasText(companyId, "companyId is empty");
		Assert.hasText(authId, "authId is empty");
		Assert.hasText(authType, "authType is empty");

		Auth auth = new Auth();
		if (!authId.equalsIgnoreCase("new")) {
			auth = getAuthService().getAuthRepository().findById(authId).orElse(null);
			Assert.notNull(auth, "auth not found: authId=[" + authId + "]");
		} else {
			auth.setCompanyId(companyId);
			auth.setType(AuthType.valueOf(authType.toUpperCase()));

			switch (auth.getType()) {
			case LDAP:
				auth.setLdap(new AuthLdap());
				break;
			case SAML:
				auth.setSaml(new AuthSaml());
				break;
			default:
				throw new AcsLogicalException("Unsupported type! type=" + auth.getType());
			}
		}

		List<ApplicationModels.ApplicationOption> ldapApplications = new ArrayList<>();
		if (auth.getType() == AuthType.LDAP) {
			ApplicationSearchParams params = new ApplicationSearchParams().withCompanyIds(companyId);
			// TODO revisit, need to support this logic for now until Itus API
			// can be developed
			if (!getAuthenticatedUser().isAdmin())
				params.withProductIds(getCoreCacheService().findProductBySystemName(ProductSystemNames.ITUS).getId());

			List<Application> applications = getApplicationService().getApplicationRepository()
					.findApplications(params);
			applications.stream().forEach(appl -> {
				ldapApplications.add(new ApplicationModels.ApplicationOption(appl));
			});
		}

		CompanyModels.CompanyAuthModel model = getPegasusModelUtil().toCompanyAuthModel(auth);
		model.withId(auth.getId()).withHid(auth.getHid()).withCompanyId(auth.getCompanyId()).withType(auth.getType())
				.withEnabled(auth.isEnabled());

		return new CompanyModels.CompanyAuthUpsertModel().withCompanyAuth(model).withLdapApplications(ldapApplications);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_COMPANY_AUTH')")
	@RequestMapping(value = "authentication/auth", method = RequestMethod.POST)
	public CompanyModels.CompanyAuthModel createAuth(@RequestBody CompanyModels.CompanyAuthModel model) {
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getCompanyId(), "companyId is empty");
		Assert.notNull(model.getType(), "type is null");
		Assert.isTrue(validateUser(model.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		// convert from model
		Auth auth = getPegasusModelUtil().toAuth(model);
		// persist
		auth = getAuthService().create(auth, getUserId());
		// look up
		auth = getAuthService().getAuthRepository().findById(auth.getId()).orElse(null);
		// convert to model and return
		return getPegasusModelUtil().toCompanyAuthModel(auth);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_COMPANY_AUTH')")
	@RequestMapping(value = "authentication/auth", method = RequestMethod.PUT)
	public CompanyModels.CompanyAuthModel updateAuth(@RequestBody CompanyModels.CompanyAuthModel model) {
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getCompanyId(), "companyId is empty");
		Assert.notNull(model.getType(), "type is null");
		Assert.isTrue(validateUser(model.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		// look up
		Auth existing = getAuthService().getAuthRepository().findById(model.getId()).orElse(null);
		Assert.notNull(existing, "Unable to find auth! authId=" + model.getId());
		// convert from model
		existing = getPegasusModelUtil().toAuth(model, existing);
		// persist
		existing = getAuthService().update(existing, getUserId());
		// look up
		Auth auth = getAuthService().getAuthRepository().findById(existing.getId()).orElse(null);
		// convert to model and return
		return getPegasusModelUtil().toCompanyAuthModel(auth);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_SUBSCRIPTIONS')")
	@RequestMapping(value = "{id}/subscriptions", method = RequestMethod.GET)
	public List<CompanyModels.SubscriptionModel> subscriptions(@PathVariable String id) {
		Assert.hasText(id, "companyId is null");
		Assert.isTrue(validateUser(id),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		List<Subscription> subscription = getSubscriptionService().getSubscriptionRepository().findByCompanyId(id);
		List<CompanyModels.SubscriptionModel> subscriptionModels = new ArrayList<>();
		subscription.stream().forEach(subscr -> {
			subscriptionModels.add(new CompanyModels.SubscriptionModel().withId(subscr.getId()).withHid(subscr.getHid())
					.withName(subscr.getName()).withDescription(subscr.getDescription()).withEnabled(subscr.isEnabled())
					.withStartDate(subscr.getStartDate()).withEndDate(subscr.getEndDate()));
		});

		return subscriptionModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_APPLICATIONS')")
	@RequestMapping(value = "{id}/applications", method = RequestMethod.GET)
	public List<CompanyModels.ApplicationModel> applications(@PathVariable String id) {
		Assert.hasText(id, "companyId is null");
		Assert.isTrue(validateUser(id),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		List<Application> applications = getApplicationService().getApplicationRepository().findByCompanyId(id);
		List<CompanyModels.ApplicationModel> applicationModels = new ArrayList<>();

		applications.stream().forEach(appl -> {
			appl = getCoreCacheHelper().populateApplication(appl);

			List<String> productExtensionNames = new ArrayList<>();
			appl.getRefProductExtensions().stream().forEach(pe -> {
				productExtensionNames.add(pe.getName());
			});

			applicationModels.add((CompanyModels.ApplicationModel) new CompanyModels.ApplicationModel()
					.withId(appl.getId()).withHid(appl.getHid()).withName(appl.getName())
					.withDescription(appl.getDescription()).withCode(appl.getCode()).withEnabled(appl.isEnabled())
					.withProductName(appl.getRefProduct().getName()).withProductExtensionNames(productExtensionNames));
		});

		if (!applications.isEmpty()) {
			Collections.sort(applicationModels, new Comparator<CompanyModels.ApplicationModel>() {

				@Override
				public int compare(CompanyModels.ApplicationModel o1, CompanyModels.ApplicationModel o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		}

		return applicationModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_HIERARCHY')")
	@RequestMapping(value = "{id}/hierarchy", method = RequestMethod.GET)
	public List<CompanyModels.CompanyListModel> hierarchy(@PathVariable String id) {
		Assert.hasText(id, "id is null");
		Assert.isTrue(validateUser(id),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		Company currentCompany = getCompanyService().getCompanyRepository().findById(id).orElse(null);
		Assert.notNull(currentCompany, "company is null");

		// find child companies
		List<Company> childCompanies = getCompanyService().getCompanyRepository().findByParentCompanyId(id);

		List<CompanyModels.CompanyListModel> companyModels = new ArrayList<>();
		childCompanies.stream().forEach(company -> {
			company = getCoreCacheHelper().populateCompany(company);
			companyModels.add(new CompanyModels.CompanyListModel(company.getId(), company.getHid())
					.withName(company.getName()).withAbbrName(company.getAbbrName()).withStatus(company.getStatus())
					.withContactName(
							company.getContact() == null || (StringUtils.isEmpty(company.getContact().getFirstName())
									&& StringUtils.isEmpty(company.getContact().getLastName())) ? ""
											: company.getContact().fullName())
					.withBillingContactName(company.getBillingContact() == null
							|| (StringUtils.isEmpty(company.getBillingContact().getFirstName())
									&& StringUtils.isEmpty(company.getBillingContact().getLastName())) ? ""
											: company.getBillingContact().fullName())
					.withParentCompanyName(
							company.getRefParentCompany() == null ? "" : company.getRefParentCompany().getName()));
		});

		return companyModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_ACCESS_KEYS')")
	@RequestMapping(value = "{companyId}/accesskeys", method = RequestMethod.POST)
	public AccessKeySearchResult<CompanyModels.AccessKeyModel> accessKeys(@PathVariable String companyId,
			@RequestBody(required = false) SearchFilterModels.AccessKeySearchFilter searchFilter) {

		Assert.hasText(companyId, "companyId is null");
		Assert.isTrue(validateUser(companyId),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		if (searchFilter == null) {
			searchFilter = new SearchFilterModels.AccessKeySearchFilter();
		}

		AccessKeySearchParams params = getSearchParamsUtil().buildAccessKeyParams(searchFilter);
		params.addCompanyIds(companyId);
		if (searchFilter.getRelationEntityType()
				.equals(SearchFilterModels.AccessKeySearchFilter.RelationEntityType.MY_KEYS)) {
			Company company = getCoreCacheService().findCompanyById(companyId);
			params.addPri(company.getPri());
		}

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));
		Page<AccessKey> accessKeyPage = getAccessKeyService().getAccessKeyRepository().findAccessKeys(pageRequest,
				params);

		// convert to visual model
		List<CompanyModels.AccessKeyModel> accessKeyModels = new ArrayList<>();

		long currentTime = Instant.now().toEpochMilli();
		for (AccessKey accKey : accessKeyPage.getContent()) {
			Application application = null;
			if (!StringUtils.isEmpty(accKey.getApplicationId())) {
				application = getCoreCacheService().findApplicationById(accKey.getApplicationId());
			}

			accKey = getPegasusModelUtil().toEncryptedAccessKeyFields(application, accKey);
			Company company = getCompanyService().getCompanyRepository().findById(accKey.getCompanyId()).orElse(null);

			final long expTimestamp = accKey.getExpiration().toEpochMilli();
			accessKeyModels.add(new CompanyModels.AccessKeyModel(accKey).withCompanyId(accKey.getCompanyId())
					.withName(accKey.getName()).withRawApiKey(accKey.getEncryptedApiKey().substring(0, 9).concat("..."))
					.withExpiration(String.valueOf(expTimestamp)).withOwnerDisplayName(company.getName())
					.withIsExpired(expTimestamp < currentTime));
		}

		Page<CompanyModels.AccessKeyModel> result = new PageImpl<>(accessKeyModels, pageRequest,
				accessKeyPage.getTotalElements());

		return new SearchResultModels.AccessKeySearchResult<CompanyModels.AccessKeyModel>(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_COMPANY_ACCESS_KEYS')")
	@RequestMapping(value = "{companyId}/accesskey/{accessKeyId}", method = RequestMethod.GET)
	public CompanyModels.AccessKeyModel accessKey(@PathVariable("companyId") String companyId,
			@PathVariable("accessKeyId") String accessKeyId) {

		Assert.hasText(companyId, "companyId is null");
		Assert.hasText(accessKeyId, "accessKeyId is null");

		Assert.isTrue(validateUser(companyId),
				"User must be an admin or the authenticated user's companyId must match the passed in companyId");

		AccessKey accessKey = getAccessKeyService().getAccessKeyRepository().findById(accessKeyId).orElse(null);
		Assert.notNull(accessKey, "AccessKey not found");
		Assert.isTrue(companyId.equals(accessKey.getCompanyId()), "User have not access to this AccessKey");

		String rawApiKey = null;
		String rawSecretKey = null;
		if (!StringUtils.isEmpty(accessKey.getApplicationId())) {
			Application application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
			rawApiKey = getCryptoService().decrypt(application.getId(), accessKey.getEncryptedApiKey());
			rawSecretKey = getCryptoService().decrypt(application.getId(), accessKey.getEncryptedSecretKey());
		} else {
			rawApiKey = getCryptoService().decrypt(accessKey.getEncryptedApiKey());
			rawSecretKey = getCryptoService().decrypt(accessKey.getEncryptedSecretKey());
		}

		List<AccessKeyModelAbstract.AccessPrivilegeModel> accessPrivilegeModels = accessKey.getPrivileges().stream()
				.map(accessPrivilege -> new AccessKeyModelAbstract.AccessPrivilegeModel(accessPrivilege.getLevel(),
						accessPrivilege.getPri()))
				.collect(Collectors.toList());

		CryptoClientImpl cryptoClient256 = new CryptoClientImpl();
		CryptoClient128Impl cryptoClient128 = new CryptoClient128Impl();

		return new CompanyModels.AccessKeyModel(accessKey).withCompanyId(accessKey.getCompanyId())
				.withName(accessKey.getName()).withRawApiKey(rawApiKey).withRawSecretKey(rawSecretKey)
				.withExpiration(String.valueOf(accessKey.getExpiration())).withAccessPrivilege(accessPrivilegeModels)
				.withAes128ApiKey(cryptoClient128.internalEncrypt(rawApiKey))
				.withAes128SecretKey(cryptoClient128.internalEncrypt(rawSecretKey))
				.withAes256ApiKey(cryptoClient256.internalEncrypt(rawApiKey))
				.withAes256SecretKey(cryptoClient256.internalEncrypt(rawSecretKey));
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_COMPANY_ACCESS_KEY')")
	@RequestMapping(value = "{companyId}/accesskey", method = RequestMethod.POST)
	public List<CompanyModels.AccessKeyModel> createAccessKey(@RequestBody CompanyModels.AccessKeyModel model,
			@PathVariable("companyId") String companyId) {

		throw new NotImplementedException();
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_COMPANY_ACCESS_KEY')")
	@RequestMapping(value = "{companyId}/accesskey", method = RequestMethod.PUT)
	public CompanyModels.AccessKeyModel updateAccessKey(@RequestBody CompanyModels.AccessKeyModel model,
			@PathVariable("companyId") String companyId) {

		Assert.notNull(companyId, "companyId is null");
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

		CompanyModels.AccessKeyModel accessKeyModel = new CompanyModels.AccessKeyModel(accessKey);

		return (CompanyModels.AccessKeyModel) getPegasusModelUtil().toAccessKeyModel(accessKey, application,
				accessKeyModel);
	}
}
