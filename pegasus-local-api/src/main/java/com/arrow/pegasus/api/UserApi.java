package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.acs.client.model.UserAuthenticationModel;
import com.arrow.pegasus.client.model.LastLoginRequestModel;
import com.arrow.pegasus.client.model.LastLoginResponseModel;
import com.arrow.pegasus.client.model.PasswordModel;
import com.arrow.pegasus.client.model.SamlAccountsModel;
import com.arrow.pegasus.client.model.SamlAuthenticationModel;
import com.arrow.pegasus.client.model.UserChangeModel;
import com.arrow.pegasus.client.model.UserPasswordChangeModel;
import com.arrow.pegasus.client.model.UserRefsModel;
import com.arrow.pegasus.data.LastLogin;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserAuth;
import com.arrow.pegasus.data.profile.UserEULA;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.repo.params.UserSearchParams;

@RestController(value = "localPegasusUserApi")
@RequestMapping("/api/v1/local/pegasus/users")
public class UserApi extends BaseApiAbstract {

	@RequestMapping(path = "/lastLogin", method = RequestMethod.POST)
	public LastLoginResponseModel findLastLogin(@RequestBody(required = false) LastLoginRequestModel body) {
		LastLoginRequestModel model = JsonUtils.fromJson(getApiPayload(), LastLoginRequestModel.class);
		User user = getCoreCacheService().findUserById(model.getUserId());
		Assert.notNull(user, "user is null");
		Application application = getCoreCacheService().findApplicationById(model.getApplicationId());
		Assert.notNull(application, "application is null");
		application = getCoreCacheHelper().populateApplication(application);

		LastLogin lastLogin = getAuditLogService().getAuditLogRepository().findLastLogin(user.getId(),
				application.getId(), application.getRefProduct().getSystemName());
		Assert.notNull(lastLogin, "lastLogin not found");
		return new LastLoginResponseModel().withUserId(lastLogin.getUserId()).withLastLogin(lastLogin.getLastLogin());
	}

	@RequestMapping(path = "/saml/accounts", method = RequestMethod.POST)
	public StatusModel syncSamlAccounts(@RequestBody(required = false) SamlAccountsModel body) {
		SamlAccountsModel model = JsonUtils.fromJson(getApiPayload(), SamlAccountsModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getApplicationId(), "applicationId is empty");
		Assert.notEmpty(model.getSamlAccounts(), "samlAccounts is empty");
		getUserService().syncSamlAccounts(model.getApplicationId(), model.getSamlAccounts());
		return StatusModel.OK;
	}

	@RequestMapping(path = "/auth", method = RequestMethod.POST)
	public User authenticate(@RequestBody(required = false) UserAuthenticationModel body) {
		UserAuthenticationModel model = JsonUtils.fromJson(getApiPayload(), UserAuthenticationModel.class);
		Assert.notNull(model, "model is null");

		// These assert checks are not longer needed as we need to throw
		// specific exceptions back
		// Assert.hasText(model.getUsername(), "username is empty");
		// Assert.hasText(model.getPassword(), "password is empty");
		User user = getUserService().authenticate(model.getUsername(), model.getPassword());
		Assert.notNull(user, "user not found");
		return user;
	}

	@RequestMapping(path = "/saml/auth", method = RequestMethod.POST)
	public User samlAuthenticate(@RequestBody(required = false) SamlAuthenticationModel body) {
		SamlAuthenticationModel model = JsonUtils.fromJson(getApiPayload(), SamlAuthenticationModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getIdp(), "idp is empty");
		Assert.hasText(model.getNameId(), "nameId is empty");
		User user = getUserService().samlAuthenticate(model.getIdp(), model.getNameId());
		Assert.notNull(user, "user not found");
		return user;
	}

	@RequestMapping(path = "/logins", method = RequestMethod.GET)
	public User findByLogin(@RequestParam(name = "login", required = true) String login) {
		Assert.hasText(login, "login is empty");
		return getUserService().getUserRepository().doFindByLogin(login);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public User create(@RequestBody(required = false) UserChangeModel body) {
		String method = "create";
		UserChangeModel model = JsonUtils.fromJson(getApiPayload(), UserChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getUser(), "user is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.hasText(model.getUser().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getUser().getLogin(), "login is empty");
		Assert.hasText(model.getUser().getPassword(), "password is empty");

		if (model.getUser().getAddress() != null) {
			logInfo(method, "address1: %s, address2: %s, city: %s, state: %s, zip: %s, country: %s",
					model.getUser().getAddress().getAddress1(), model.getUser().getAddress().getAddress2(),
					model.getUser().getAddress().getCity(), model.getUser().getAddress().getState(),
					model.getUser().getAddress().getZip(), model.getUser().getAddress().getCountry());
		} else {
			logInfo(method, "address is null");
		}
		User user = buildUser(model.getUser(), null);
		return getUserService().create(user, model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public User update(@RequestBody(required = false) UserChangeModel body) {
		String method = "update";
		UserChangeModel model = JsonUtils.fromJson(getApiPayload(), UserChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getUser(), "user is null");
		Assert.hasText(model.getWho(), "who is empty");
		User user = getUserService().getUserRepository().findById(model.getUser().getId()).orElse(null);
		Assert.notNull(user, "user not found");

		if (model.getUser().getAddress() != null) {
			logInfo(method, "address1: %s, address2: %s, city: %s, state: %s, zip: %s, country: %s",
					model.getUser().getAddress().getAddress1(), model.getUser().getAddress().getAddress2(),
					model.getUser().getAddress().getCity(), model.getUser().getAddress().getState(),
					model.getUser().getAddress().getZip(), model.getUser().getAddress().getCountry());
		} else {
			logInfo(method, "address is null");
		}

		user = buildUser(model.getUser(), user);
		return getUserService().update(user, model.getWho());
	}

	private User buildUser(User model, User user) {
		if (user == null) {
			user = new User();
		}
		if (StringUtils.hasText(model.getLogin())) {
			user.setLogin(model.getLogin());
		}
		if (StringUtils.hasText(model.getHashedLogin())) {
			user.setHashedLogin(model.getHashedLogin());
		}
		if (StringUtils.hasText(model.getPassword())) {
			user.setPassword(model.getPassword());
		}
		if (StringUtils.hasText(model.getParentUserId())) {
			User parentUser = getCoreCacheService().findUserById(model.getParentUserId());
			Assert.notNull(parentUser, "parent user not found");
			user.setParentUserId(model.getParentUserId());
		}
		if (StringUtils.hasText(model.getCompanyId())) {
			checkCompany(model.getCompanyId());
			user.setCompanyId(model.getCompanyId());
		}
		if (model.getContact() != null) {
			user.setContact(model.getContact());
		}
		if (model.getAddress() != null) {
			user.setAddress(model.getAddress());
		}
		if (model.getEulas() != null && !model.getEulas().isEmpty()) {
			List<UserEULA> eulas = new ArrayList<>();
			model.getEulas().forEach(userEULA -> {
				checkProduct(userEULA.getProductId());
				if (userEULA.getAgreedDate() != null) {
					eulas.add(userEULA);
				}
			});
			if (!eulas.isEmpty()) {
				user.setEulas(eulas);
			}
		}
		if (model.getRoleIds() != null && !model.getRoleIds().isEmpty()) {
			List<String> roleIds = new ArrayList<>();
			model.getRoleIds().forEach(roleId -> {
				checkRole(roleId);
				roleIds.add(roleId);
			});
			user.setRoleIds(roleIds);
		}
		if (model.getAuths() != null && !model.getAuths().isEmpty()) {
			List<UserAuth> auths = new ArrayList<>();
			model.getAuths().forEach(auth -> {
				Auth validatedAuth = getValidatedAuth(auth.getRefId());
				Assert.isTrue(auth.getType() == validatedAuth.getType(), "types of auths doesn't match");
				auths.add(auth);
			});
			user.setAuths(auths);
		}
		if (model.getStatus() != null) {
			user.setStatus(model.getStatus());
		}
		user.setAdmin(model.isAdmin());
		if (StringUtils.hasText(model.getSalt())) {
			user.setSalt(model.getSalt());
		}
		return user;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<User> findAll() {
		return getUserService().getUserRepository().findAll();
	}

	@RequestMapping(path = "/companies/roles", method = RequestMethod.GET)
	public List<User> findByCompanyIdAndRoleIds(@RequestParam(name = "companyId", required = true) String companyId,
			@RequestParam(name = "roleIds", required = true) String[] roleIds) {
		Assert.hasText(companyId, "companyId is empty");
		Assert.notEmpty(roleIds, "roleIds is empty");
		UserSearchParams params = new UserSearchParams();
		params.addCompanyIds(companyId);
		params.addRoleIds(roleIds);
		return getUserService().getUserRepository().findUsers(params);
	}

	@RequestMapping(path = "/passwords/reset", method = RequestMethod.POST)
	public PasswordModel resetPassword(@RequestBody(required = false) UserChangeModel body) {
		UserChangeModel model = JsonUtils.fromJson(getApiPayload(), UserChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getUser(), "user is null");
		Assert.hasText(model.getWho(), "who is empty");
		User user = getUserService().getUserRepository().findById(model.getUser().getId()).orElse(null);
		Assert.notNull(user, "user not found");
		String password = getUserService().resetPassword(model.getUser(), model.getWho());
		return new PasswordModel().withPassword(password);
	}

	@RequestMapping(path = "/passwords/auth", method = RequestMethod.POST)
	public User authenticateChangePassword(@RequestBody(required = false) UserAuthenticationModel body) {
		UserAuthenticationModel model = JsonUtils.fromJson(getApiPayload(), UserAuthenticationModel.class);
		Assert.notNull(model, "model is null");

		String method = "authenticateChangePassword";
		logDebug(method, "login: %s, password: %s", model.getUsername(), model.getPassword());
		User user = getUserService().authenticateChangePassword(model.getUsername(), model.getPassword());
		return user;
	}

	@RequestMapping(path = "/passwords", method = RequestMethod.POST)
	public List<String> setNewPassword(@RequestBody(required = false) UserPasswordChangeModel body) {
		UserPasswordChangeModel model = JsonUtils.fromJson(getApiPayload(), UserPasswordChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getUser(), "user is null");
		Assert.hasText(model.getNewPassword(), "newPassword is empty");
		Assert.hasText(model.getWho(), "who is empty");
		User user = getUserService().getUserRepository().findById(model.getUser().getId()).orElse(null);
		Assert.notNull(user, "user not found");
		return getUserService().setNewPassword(model.getUser(), model.getNewPassword(), model.getWho());
	}

	@RequestMapping(path = "/themis", method = RequestMethod.GET)
	public PagingResultModel<User> findThemisUsers(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty,
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "roleIds", required = false) String[] roleIds,
			@RequestParam(name = "statuses", required = false) EnumSet<UserStatus> statuses,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName) {
		Assert.isTrue(
				sortDirection == null && sortProperty == null
						|| sortDirection != null && sortProperty != null && sortDirection.length == sortProperty.length,
				"invalid sort options");
		PageRequest pageRequest = null;
		if (sortDirection != null) {
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < sortDirection.length; i++) {
				orders.add(new Order(Direction.fromString(sortDirection[i]), sortProperty[i]));
			}
			pageRequest = PageRequest.of(page, size, Sort.by(orders));
		} else {
			pageRequest = PageRequest.of(page, size);
		}
		UserSearchParams params = new UserSearchParams();
		params.addCompanyIds(companyId);
		params.addRoleIds(roleIds);
		params.setStatuses(statuses);
		params.setFirstName(firstName);
		params.setLastName(lastName);
		Page<User> users = getUserService().getUserRepository().findUsers(pageRequest, params);
		PagingResultModel<User> result = new PagingResultModel<User>().withPage(users.getNumber())
				.withTotalPages(users.getTotalPages()).withTotalSize(users.getTotalElements());
		result.withData(users.getContent()).withSize(users.getSize());
		return result;
	}

	@RequestMapping(path = "/refs", method = RequestMethod.POST)
	public UserRefsModel populateRefs(@RequestBody(required = false) User body) {
		User user = JsonUtils.fromJson(getApiPayload(), User.class);
		Assert.notNull(user, "user is null");
		return new UserRefsModel().withUser(getCoreCacheHelper().populateUser(user));
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public User findById(@PathVariable(name = "id", required = true) String id) {
		String method = "findById";
		User user = getUserService().getUserRepository().findById(id).orElse(null);
		Assert.notNull(user, "user not found");

		if (user.getAddress() != null) {
			logInfo(method, "address1: %s, address2: %s, city: %s, state: %s, zip: %s, country: %s",
					user.getAddress().getAddress1(), user.getAddress().getAddress2(), user.getAddress().getCity(),
					user.getAddress().getState(), user.getAddress().getZip(), user.getAddress().getCountry());
		} else {
			logInfo(method, "address is null");
		}

		return user;
	}
}
