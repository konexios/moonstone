package com.arrow.pegasus.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.arrow.pegasus.data.LastLogin;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserAuth;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.arrow.pegasus.repo.params.RoleSearchParams;
import com.arrow.pegasus.repo.params.UserSearchParams;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.SmtpEmailSender;
import com.arrow.pegasus.web.model.CompanyModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;
import com.arrow.pegasus.web.model.UserModels;
import com.arrow.pegasus.web.model.UserModels.UserCompanyOption;
import com.arrow.pegasus.web.model.UserModels.UserRoleOption;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;

@RestController
@RequestMapping("/api/pegasus/users")
public class UserController extends PegasusControllerAbstract {

	@Autowired
	private SmtpEmailSender smtpEmailSender;

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "{id}/applications", method = RequestMethod.GET)
	public List<CompanyModels.ApplicationModel> applications(@PathVariable String id) {
		Assert.hasText(id, "companyId is null");

		Company company = getCoreCacheService().findCompanyById(id);
		Assert.notNull(company, "Company not found! companyId=" + id);

		List<Application> applications = new ArrayList<>();

		// OUT OF SCOPE FOR R9S3
		// if (StringUtils.isEmpty(company.getParentCompanyId())) {
		// Product pegasusProduct =
		// getCoreCacheService().findProductBySystemName(ProductSystemNames.PEGASUS);
		// Assert.notNull(pegasusProduct, "Pegasus product not found!
		// systemName=" + ProductSystemNames.PEGASUS);
		// Product rheaProduct =
		// getCoreCacheService().findProductBySystemName(ProductSystemNames.RHEA);
		// Assert.notNull(rheaProduct, "Rhea product not found! systemName=" +
		// ProductSystemNames.RHEA);
		//
		// ApplicationSearchParams params = new ApplicationSearchParams();
		// params.addCompanyIds(company.getId());
		// params.addProductIds(pegasusProduct.getId(), rheaProduct.getId());
		// params.setEnabled(true);
		//
		// applications =
		// getApplicationService().getApplicationRepository().findApplications(params);
		// } else {
		// applications =
		// getApplicationService().getApplicationRepository().findByCompanyId(id);
		// }

		ApplicationSearchParams params = new ApplicationSearchParams();
		params.addCompanyIds(company.getId());
		if (!getAuthenticatedUser().isAdmin())
			params.setEnabled(true);
		applications = getApplicationService().getApplicationRepository().findApplications(params);

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

		applicationModels
				.sort(Comparator.comparing(CompanyModels.ApplicationModel::getName, String.CASE_INSENSITIVE_ORDER));

		return applicationModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public List<UserModels.UserOption> options() {
		return getUserOptions(null, UserStatus.Active);
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	public UserModels.UserFilterOptions filterOptions(HttpSession session) {

		User authenticatedUser = getAuthenticatedUser();

		// company options
		List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		// user status options
		UserStatus[] statusOptions = UserStatus.values();

		return new UserModels.UserFilterOptions().withCompanyOptions(companyOptions).withStatusOptions(statusOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_USER')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.UserSearchResult find(@RequestBody SearchFilterModels.UserSearchFilter searchFilter) {

		// search params
		UserSearchParams params = new UserSearchParams();
		if (!getAuthenticatedUser().isAdmin()) {
			// always enforce user's companyId and child tenants
			List<String> companyIds = getCompanyIds(getAuthenticatedUser().getCompanyId(),
					EnumSet.of(CompanyStatus.Active), true, getAuthenticatedUser().isAdmin());
			params.addCompanyIds(companyIds.toArray(new String[companyIds.size()]));
		} else
			params.addCompanyIds(searchFilter.getCompanyIds());

		params.setStatuses(searchFilter.getStatuses() != null && searchFilter.getStatuses().length > 0
				? EnumSet.copyOf(Arrays.asList(searchFilter.getStatuses()))
				: EnumSet.noneOf(UserStatus.class));

		List<String> sortProperties = new ArrayList<>();
		if (searchFilter.getSortField().equalsIgnoreCase("fullName")) {
			sortProperties.add("contact.firstName");
			sortProperties.add("contact.lastName");
		} else {
			sortProperties.add(searchFilter.getSortField());
		}

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), sortProperties));

		Page<User> userPage = null;
		if (searchFilter.getFirstNameLastNameAndLogin() == null) {
			params.setFirstName(searchFilter.getFirstName());
			params.setLastName(searchFilter.getLastName());
			params.setLogin(searchFilter.getLogin());

			// lookup
			userPage = getUserService().getUserRepository().findUsers(pageRequest, params);
		} else {
			params.setFirstNameLastNameAndLogin(searchFilter.getFirstNameLastNameAndLogin());

			// lookup
			userPage = getUserService().getUserRepository().findUsers(pageRequest, params);
		}

		// convert to visual model
		Page<UserModels.UserList> result = null;
		List<UserModels.UserList> userModels = new ArrayList<>();
		for (User user : userPage) {
			// lookup user's tenant/company
			user.setRefCompany(getCoreCacheService().findCompanyById(user.getCompanyId()));
			// decrypt user's login
			String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);
			userModels.add(new UserModels.UserList(user, decryptedLogin, user.getRefCompany()));
		}
		result = new PageImpl<>(userModels, pageRequest, userPage.getTotalElements());

		return new SearchResultModels.UserSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{applicationId}/roles", method = RequestMethod.GET)
	public List<UserModels.UserRoleOption> roleOptions(@PathVariable String applicationId) {
		Assert.hasText(applicationId, "applicationId is null");
		return getUserRoleOptionsByApplication(applicationId, true);
	}

	// @PreAuthorize("hasAuthority('PEGASUS_READ_USER')")
	// @RequestMapping(value = "/{id}/user")
	// public UserModels.UserUpsert user(HttpSession session, @PathVariable
	// String id) {
	// Assert.notNull(session, "session is null");
	// Assert.hasText(id, "id is null");
	//
	// User user = new User();
	// user.setStatus(null); // override default status
	// user.setContact(new Contact());
	// user.setAddress(new Address());
	//
	// // all roles
	// List<UserRoleOption> userRoleOptions = new ArrayList<>();
	//
	// LastLogin lastLogin = null;
	// if (!id.equalsIgnoreCase("new")) {
	// user = getUserService().getUserRepository().findById(id);
	// Assert.notNull(user, "user not found: userId=[" + id + "]");
	// user = getCoreCacheHelper().populateUser(user);
	//
	// // last login
	// lastLogin =
	// getAuditLogService().getAuditLogRepository().findLastLogin(user.getId(),
	// null, null);
	//
	// // decrypt user's login
	// user = getPegasusModelUtil().populateDecryptedLogin(user);
	// userRoleOptions = getUserRoleOptions(user.getCompanyId(), true);
	// }
	//
	// List<UserModels.UserStatusOption> statusOptions = new ArrayList<>();
	// for (UserStatus userStatus : UserStatus.values())
	// statusOptions.add(new UserModels.UserStatusOption(userStatus));
	//
	// List<CompanyOption> companyOptions =
	// getCompanyOptions(getAuthenticatedUser().getCompanyId(),
	// EnumSet.of(CompanyStatus.Active), getAuthenticatedUser().isAdmin());
	// if (!getAuthenticatedUser().isAdmin()) {
	// User currentUser = getAuthenticatedUser();
	// companyOptions = companyOptions.stream()
	// .filter(companyOption ->
	// companyOption.getId().equals(currentUser.getCompanyId()))
	// .collect(Collectors.toList());
	// }
	//
	// return new UserModels.UserUpsert(
	// new UserModels.UserModel(user).withLastLogin((lastLogin == null ? null :
	// lastLogin.getLastLogin())),
	// statusOptions, getCompanyOptions(CompanyStatus.Active), userRoleOptions,
	// getCompanyUserList(companyOptions));
	//
	// }

	@PreAuthorize("hasAuthority('PEGASUS_READ_USER')")
	@RequestMapping(value = "/{id}/user")
	public UserModels.UserUpsert user(HttpSession session, @PathVariable String id) {

		Assert.notNull(session, "session is null");
		Assert.hasText(id, "id is null");

		User authenticatedUser = getAuthenticatedUser();

		// company options
		final List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		User user = new User();
		String decryptedLogin = null;
		if (!id.equals("new")) {
			user = getCoreCacheService().findUserById(id);
			Assert.notNull(user, "user not found");

			decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);
		}

		return new UserModels.UserUpsert(new UserModels.UserModel(user, decryptedLogin), null, companyOptions, null,
				null, null);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_USER')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public UserModels.UserModel create(HttpSession session, @RequestBody UserModels.UserModel model) {
		Assert.notNull(session, "session is null");
		Assert.notNull(model, "user is null");

		String method = "create";

		Application application = getCurrentApplication(session);
		Assert.notNull(application, "application is null");

		User user = getPegasusModelUtil().toUser(model);

		// create user
		user = getUserService().create(user, getUserId());
		// reset password
		String tempPassword = getUserService().resetPassword(user, getUserId());
		logDebug(method, "tempPassword: %s", tempPassword);

		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		StringBuilder sb = new StringBuilder();
		sb.append(user.getContact().fullName() + ",");
		sb.append("\n\n");
		sb.append("Your Arrow Connect account has been created.");
		sb.append(" Use the following login and password to access your account.");
		sb.append("\n\nLogin " + decryptedLogin);
		sb.append("\nPassword " + tempPassword);
		sb.append("\n\nOnce you login you will be required to change your password.");
		sb.append("\n\nThanks\n\n");

		smtpEmailSender.send(new String[] { user.getContact().getEmail() }, null, "Arrow Connect Account Setup",
				sb.toString(), EmailContentType.PLAIN_TEXT);

		return new UserModels.UserModel(user, decryptedLogin);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_USER')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public UserModels.UserModel update(HttpSession session, @RequestBody UserModels.UserModel model) {
		Assert.notNull(session, "session is null");
		Assert.notNull(model, "user is null");

		Application application = getCurrentApplication(session);
		Assert.notNull(application, "application is null");

		User existing = getUserService().getUserRepository().findById(model.getId()).orElse(null);
		Assert.notNull(existing, "user not found :: userId=[" + model.getId() + "]");
		Assert.isTrue(getAuthenticatedUser().isAdmin() || model.isAdmin() == existing.isAdmin(),
				"You have not access for change admin flag");

		// populate
		existing = getPegasusModelUtil().toUser(model, existing);

		User user = getUserService().update(existing, getUserId());
		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		return new UserModels.UserModel(user, decryptedLogin);
	}

	@PreAuthorize("hasAuthority('PEGASUS_RESET_USER_PASSWORD')")
	@RequestMapping(path = "/{id}/password/reset", method = RequestMethod.PUT)
	public UserModels.UserModel resetPassword(HttpSession session, @PathVariable String id) {
		Assert.notNull(session, "session is null");
		Assert.hasText(id, "id is empty");

		Application application = getCurrentApplication(session);
		Assert.notNull(application, "application is null");

		User user = getCoreCacheService().findUserById(id);
		Assert.notNull(user, "user is null");
		user = resetUserPassword(user, getUserId());

		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		return new UserModels.UserModel(user, decryptedLogin);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_USER')")
	@RequestMapping(path = "/{id}/disable", method = RequestMethod.PUT)
	public UserModels.UserModel disableUser(HttpSession session, @PathVariable String id) {

		Assert.notNull(session, "session is null");
		Assert.hasText(id, "id is empty");

		Application application = getCurrentApplication(session);
		Assert.notNull(application, "application is null");

		User user = getCoreCacheService().findUserById(id);
		Assert.notNull(user, "user is null");
		user.setStatus(UserStatus.InActive);

		User updatedUser = getUserService().update(user, getUserId());

		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		return new UserModels.UserModel(updatedUser, decryptedLogin);
	}

	@RequestMapping(path = "/password/forgot", method = RequestMethod.PUT)
	public void forgotPassword(@RequestBody UserModels.UserModel user) {

		final String login = user.getLogin();
		Assert.hasText(login, "login is empty");

		User savedUser = getUserService().loadUserByLogin(login);
		Assert.notNull(user, "user is null");

		// reset password
		resetUserPassword(savedUser, savedUser.getId());
	}

	@PreAuthorize("hasAuthority('PEGASUS_RESET_USER_PASSWORD')")
	@RequestMapping(path = "/password/change", method = RequestMethod.PUT)
	public UserModels.UserModel changePassword(HttpSession session,
			@RequestBody UserModels.PasswordChangeModel passwordModel) {

		Assert.notNull(session, "session is null");
		Application application = getCurrentApplication(session);
		Assert.notNull(application, "application is null");

		User authenticatedUser = getAuthenticatedUser();
		Assert.notNull(authenticatedUser, "user is null");

		final User user = getCoreCacheService().findUserById(authenticatedUser.getId());

		// hashed current password
		String hashedCurrentPassword = getCryptoService().getCrypto().hash(passwordModel.getCurrentPassword(),
				user.getSalt());

		Assert.isTrue(hashedCurrentPassword.equals(user.getPassword()), "Current password is wrong");

		// change password
		List<String> result = getUserService().setNewPassword(user, passwordModel.getNewPassword(), user.getId());

		// if there are errors
		Assert.isTrue(result.isEmpty(), String.join("<br>", result));

		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		String emailText = new StringBuilder().append(user.getContact().fullName()).append(",").append("\n\n")
				.append("Your Arrow Connect password has been changed.")
				.append(" Use the following login and password to access your account.").append("\n\nLogin ")
				.append(decryptedLogin).append("\n\nThanks,\n\nArrow SI Team.").toString();

		// send email in other thread
		new Thread(() -> smtpEmailSender.send(new String[] { user.getContact().getEmail() }, null,
				"Arrow Connect Password Change", emailText, EmailContentType.PLAIN_TEXT)).start();

		return new UserModels.UserModel(user, decryptedLogin);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_USER')")
	@RequestMapping(path = "/{id}/profile", method = RequestMethod.GET)
	public UserModels.UserUpsert profile(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		User user = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(user, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(user.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		User authenticatedUser = getAuthenticatedUser();

		// company options
		List<CompanyOption> companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(),
				EnumSet.of(CompanyStatus.Active), true, authenticatedUser.isAdmin());

		if (!authenticatedUser.isAdmin()) {
			User currentUser = authenticatedUser;
			companyOptions = companyOptions.stream()
					.filter(companyOption -> companyOption.getId().equals(currentUser.getCompanyId()))
					.collect(Collectors.toList());
		}

		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		return new UserModels.UserUpsert(new UserModels.UserModel(user, decryptedLogin), null, companyOptions, null,
				null, getCompanyUserList(companyOptions));
	}

	@RequestMapping(path = "/password/change/{login:.+}", method = RequestMethod.PUT)
	public UserModels.UserModel changePassword(@RequestBody UserModels.PasswordChangeModel passwordModel,
			@PathVariable String login) {

		User user = getUserService().loadUserByLogin(login);

		Assert.isTrue(user.getStatus().equals(UserStatus.PasswordReset),
				"Could not change password without authentication");

		// hashed current password
		String hashedCurrentPassword = getCryptoService().getCrypto().hash(passwordModel.getCurrentPassword(),
				user.getSalt());

		Assert.isTrue(hashedCurrentPassword.equals(user.getPassword()), "Current password is wrong");

		// change password
		List<String> result = getUserService().setNewPassword(user, passwordModel.getNewPassword(), user.getId());

		// if there are errors
		Assert.isTrue(result.isEmpty(), String.join("<br>", result));

		String emailText = new StringBuilder().append(user.getContact().fullName()).append(",").append("\n\n")
				.append("Your Arrow Connect password has been changed.")
				.append(" Use the following login and password to access your account.").append("\n\nLogin ")
				.append(login).append("\n\nThanks,\n\nArrow SI Team.").toString();

		// send email in other thread
		new Thread(() -> smtpEmailSender.send(new String[] { user.getContact().getEmail() }, null,
				"Arrow Connect Password Change", emailText, EmailContentType.PLAIN_TEXT)).start();

		return new UserModels.UserModel(user, login);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_USER')")
	@RequestMapping(path = "/{id}/profile", method = RequestMethod.PUT)
	public UserModels.UserProfileModel updateProfile(@PathVariable String id,
			@RequestBody UserModels.UserProfileModel model) {
		Assert.notNull(id, "id is null");
		Assert.notNull(model, "user is null");

		User currentUser = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(currentUser, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(currentUser.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		// populate user with updates
		currentUser = getPegasusModelUtil().toUserFromProfile(model, currentUser);

		User user = getUserService().update(currentUser, getUserId());

		return getPegasusModelUtil().toUserProfileModel(user);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_USER_ACCOUNT')")
	@RequestMapping(path = "/{id}/account", method = RequestMethod.GET)
	public UserModels.UserUpsert account(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		String method = "account";

		User authenticatedUser = getAuthenticatedUser();

		User user = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(user, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(user.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		user = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(user, "user not found: userId=[" + id + "]");
		user = getCoreCacheHelper().populateUser(user);

		// last login
		LastLogin lastLogin = getAuditLogService().getAuditLogRepository().findLastLogin(user.getId(), null, null);

		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);
		List<UserRoleOption> userRoleOptions = getUserRoleOptions(user.getCompanyId(), true);

		List<UserModels.UserStatusOption> statusOptions = new ArrayList<>();
		for (UserStatus userStatus : UserStatus.values()) {
			statusOptions.add(new UserModels.UserStatusOption(userStatus));
		}

		// company options
		List<CompanyOption> companyOptions = new ArrayList<>();
		List<CompanyOption> companyRoleOptions = new ArrayList<>();
		if (authenticatedUser.isAdmin()) {
			companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(), EnumSet.allOf(CompanyStatus.class),
					false, authenticatedUser.isAdmin());

			companyRoleOptions.addAll(companyOptions);
		} else {
			companyOptions = getCompanyOptions(authenticatedUser.getCompanyId(), EnumSet.of(CompanyStatus.Active), true,
					authenticatedUser.isAdmin());

			companyRoleOptions.addAll(companyOptions);

			// OUT OF SCOPE FOR R9S3
			// Set<String> companyIdSet = new HashSet<>();
			// for (CoreCompanyModels.CompanyOption company :
			// companyRoleOptions)
			// companyIdSet.add(company.getId());
			//
			// // include root tenant (Arrow Electronics Inc)
			// List<Company> rootCompanies =
			// getCompanyService().getCompanyRepository()
			// .findRootCompaniesByStatus(EnumSet.of(CompanyStatus.Active));
			// logDebug(method, "rootCompanies: %s", rootCompanies.size());
			//
			// for (Company company : rootCompanies)
			// if (!companyIdSet.contains(company.getId()))
			// companyRoleOptions.add(new
			// CoreCompanyModels.CompanyOption(company));
		}

		// sort by name
		companyOptions.sort(Comparator.comparing(CompanyModels.CompanyOption::getName));
		logDebug(method, "companyOptions: %s", companyOptions.size());

		companyRoleOptions.sort(Comparator.comparing(CompanyModels.CompanyOption::getName));
		logDebug(method, "companyRoleOptions: %s", companyRoleOptions.size());

		return new UserModels.UserUpsert(
				new UserModels.UserModel(user, decryptedLogin)
						.withLastLogin((lastLogin == null ? null : lastLogin.getLastLogin())),
				statusOptions, companyOptions, companyRoleOptions, userRoleOptions, null);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_USER_ACCOUNT')")
	@RequestMapping(path = "/{id}/account", method = RequestMethod.PUT)
	public UserModels.UserAccountModel updateAccount(@PathVariable String id,
			@RequestBody UserModels.UserAccountModel model) {
		Assert.notNull(id, "id is null");
		Assert.notNull(model, "user is null");

		User currentUser = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(currentUser, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(currentUser.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		// populate user with updates
		currentUser = getPegasusModelUtil().toUser(model, currentUser);

		User user = getUserService().update(currentUser, getUserId());
		getCoreCacheService().clearUser(user);

		return getPegasusModelUtil().toUserAccountModel(user);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_USER_AUTHENTICATION')")
	@RequestMapping(path = "/{id}/authentication", method = RequestMethod.GET)
	public UserModels.UserAuthenticationUpsert authentication(@PathVariable String id) {

		Assert.hasText(id, "id is null");
		User user = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(user, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(user.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		final List<String> authIds = user.getAuths().stream().map(UserAuth::getRefId).collect(Collectors.toList());

		List<Auth> userAuths = getAuthService().getAuthRepository().findAllByIdIn(authIds);

		List<Auth> authOptions = getAuthService().getAuthRepository().findAllByCompanyId(user.getCompanyId()).stream()
				.filter(Auth::isEnabled).collect(Collectors.toList());

		return getPegasusModelUtil().toUserAuthenticationUpsert(user, userAuths, authOptions);
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_USER_AUTHENTICATION')")
	@RequestMapping(path = "/{id}/authentication", method = RequestMethod.POST)
	public UserModels.UserAuthenticationModel createAuthentication(@PathVariable String id,
			@RequestBody UserModels.UserAuthenticationModel model) {
		Assert.notNull(id, "id is null");
		Assert.notNull(model, "authentication is null");

		User currentUser = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(currentUser, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(currentUser.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		currentUser.getAuths().add(getPegasusModelUtil().toUserAuthFromModel(model));

		User user = getUserService().update(currentUser, getUserId());

		return user.getAuths().stream().filter(auth -> auth.getRefId().equals(model.getProvider())).findFirst()
				.map(auth -> {
					Auth authentication = getCoreCacheService().findAuthById(model.getProvider());
					return getPegasusModelUtil().toUserAuthenticationModel(authentication, auth.getPrincipal(),
							auth.isEnabled());
				}).orElse(null);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_USER_AUTHENTICATION')")
	@RequestMapping(path = "/{id}/authentication", method = RequestMethod.PUT)
	public UserModels.UserAuthenticationModel updateAuthentication(@PathVariable String id,
			@RequestBody UserModels.UserAuthenticationModel model) {

		Assert.notNull(id, "id is null");
		Assert.notNull(model, "authentication is null");
		Assert.notNull(model.getAuthId(), "authentication.id is null");

		User currentUser = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(currentUser, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(currentUser.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		currentUser.getAuths().removeIf(auth -> auth.getRefId().equals(model.getAuthId()));

		currentUser.getAuths().add(getPegasusModelUtil().toUserAuthFromModel(model));

		User user = getUserService().update(currentUser, getUserId());

		return user.getAuths().stream().filter(auth -> auth.getRefId().equals(model.getProvider())).findFirst()
				.map(auth -> {
					Auth authentication = getCoreCacheService().findAuthById(model.getProvider());
					return getPegasusModelUtil().toUserAuthenticationModel(authentication, auth.getPrincipal(),
							auth.isEnabled());
				}).orElse(null);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_USER_AUTHENTICATION')")
	@RequestMapping(path = "/{id}/authentications/disable/all", method = RequestMethod.PATCH)
	public List<UserModels.UserAuthenticationModel> disableAllAuthentications(@PathVariable String id) {

		User currentUser = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(currentUser, "user not found :: userId=[" + id + "]");
		Assert.isTrue(validateUser(currentUser.getCompanyId()),
				"User must be an admin or the authenticated user's companyId must match companyId associated to the passed in user");

		currentUser.getAuths().forEach(userAuth -> userAuth.setEnabled(false));

		User user = getUserService().update(currentUser, getUserId());

		return user.getAuths().stream().map(auth -> {
			Auth authentication = getCoreCacheService().findAuthById(auth.getRefId());
			return getPegasusModelUtil().toUserAuthenticationModel(authentication, auth.getPrincipal(),
					auth.isEnabled());
		}).collect(Collectors.toList());
	}

	@RequestMapping(value = "/password-policy", method = RequestMethod.GET)
	public PasswordPolicy getPasswordPolicy() {
		User user = getCoreCacheService().findUserById(getUserId());

		return getPasswordPolicy(user);
	}

	@RequestMapping(value = "/password-policy/{login:.+}", method = RequestMethod.GET)
	public PasswordPolicy getPasswordPolicy(@PathVariable String login) {
		User user = getUserService().loadUserByLogin(login);

		return getPasswordPolicy(user);
	}

	private PasswordPolicy getPasswordPolicy(User user) {
		Assert.notNull(user, "user is null");
		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company is null");

		return company.getPasswordPolicy();
	}

	private User resetUserPassword(User user, String who) {

		String method = "forgotPassword";

		// reset password
		String tempPassword = getUserService().resetPassword(user, who);
		logDebug(method, "tempPassword: %s", tempPassword);

		// decrypt user's login
		String decryptedLogin = getPegasusModelUtil().toDecryptedLogin(user);

		StringBuilder sb = new StringBuilder();
		sb.append(user.getContact().fullName() + ",");
		sb.append("\n\n");
		sb.append("Your Arrow Connect password has been reset.");
		sb.append(" Use the following login and password to access your account.");
		sb.append("\n\nLogin " + decryptedLogin);
		sb.append("\nPassword " + tempPassword);
		sb.append("\n\nOnce you login you will be required to change your password.");
		sb.append("\n\nThanks\n\n");

		// send email in other thread
		new Thread(() -> smtpEmailSender.send(new String[] { user.getContact().getEmail() }, null,
				"Arrow Connect Password Reset", sb.toString(), EmailContentType.PLAIN_TEXT)).start();

		return user;
	}

	private List<UserModels.UserCompanyOption> getCompanyUserList(List<CompanyOption> companyOptions) {
		List<UserModels.UserCompanyOption> companyUserList = new ArrayList<>();
		for (CompanyOption companyOption : companyOptions) {
			List<User> companyUsers = getUserService().getUserRepository().findByCompanyId(companyOption.getId());
			for (User companyUser : companyUsers) {
				companyUserList.add(new UserModels.UserCompanyOption(companyUser));
			}
		}

		if (!companyUserList.isEmpty())
			Collections.sort(companyUserList, new Comparator<UserModels.UserCompanyOption>() {

				@Override
				public int compare(UserCompanyOption o1, UserCompanyOption o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});

		return companyUserList;
	}

	private List<UserModels.UserRoleOption> getUserRoleOptions(String companyId, boolean enabled) {
		Assert.notNull(enabled, "enabled is null");

		Set<String> applicationIds = new HashSet<>();
		if (!StringUtils.isEmpty(companyId)) {
			List<Application> companyApplications = getApplicationService().getApplicationRepository()
					.findByCompanyId(companyId);
			for (Application application : companyApplications)
				applicationIds.add(application.getId());
		}

		List<Role> roles = null;
		if (!applicationIds.isEmpty()) {
			RoleSearchParams params = new RoleSearchParams();
			params.setApplicationIds(applicationIds);
			roles = getRoleService().getRoleRepository().findRoles(params);
		} else
			roles = getRoleService().getRoleRepository().findByEnabled(enabled);

		List<UserRoleOption> roleOptions = mapRolesToRoleOptions(roles);

		if (!roleOptions.isEmpty())
			Collections.sort(roleOptions, new Comparator<UserModels.UserRoleOption>() {

				@Override
				public int compare(UserModels.UserRoleOption o1, UserModels.UserRoleOption o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

		return Collections.unmodifiableList(roleOptions);
	}

	private List<UserRoleOption> mapRolesToRoleOptions(List<Role> roles) {
		List<UserRoleOption> roleOptions = new ArrayList<>();
		for (Role role : roles) {
			String applicationName = "UNKNOWN";
			if (!StringUtils.isEmpty(role.getApplicationId())) {
				Application roleApplication = getCoreCacheService().findApplicationById(role.getApplicationId());
				if (roleApplication != null)
					applicationName = roleApplication.getName();
			}

			roleOptions.add(new UserRoleOption(applicationName, role));
		}
		return roleOptions;
	}

	private List<UserModels.UserRoleOption> getUserRoleOptionsByApplication(String applicationId, boolean enabled) {
		Assert.notNull(enabled, "enabled is null");

		List<Role> roles = new ArrayList<>();
		if (!StringUtils.isEmpty(applicationId)) {
			RoleSearchParams params = new RoleSearchParams();
			params.addApplicationIds(new String[] { applicationId });
			if (!getAuthenticatedUser().isAdmin()) {
				params.setEnabled(enabled);
				params.setHidden(false);
			}
			roles = getRoleService().getRoleRepository().findRoles(params);
		}

		List<UserRoleOption> roleOptions = mapRolesToRoleOptions(roles);
		roleOptions.sort(Comparator.comparing(UserRoleOption::getName, String.CASE_INSENSITIVE_ORDER));

		return Collections.unmodifiableList(roleOptions);
	}
}
