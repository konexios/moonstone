package com.arrow.pegasus.client.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.AcsClientException;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.SamlAccountModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.acs.client.model.UserAuthenticationModel;
import com.arrow.pegasus.DuplicateLoginException;
import com.arrow.pegasus.InActiveAccountException;
import com.arrow.pegasus.InvalidLoginException;
import com.arrow.pegasus.LockedAccountException;
import com.arrow.pegasus.LoginRequiredException;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.RequiredChangePasswordException;
import com.arrow.pegasus.UnverifiedAccountException;
import com.arrow.pegasus.client.model.LastLoginRequestModel;
import com.arrow.pegasus.client.model.LastLoginResponseModel;
import com.arrow.pegasus.client.model.PasswordModel;
import com.arrow.pegasus.client.model.SamlAccountsModel;
import com.arrow.pegasus.client.model.SamlAuthenticationModel;
import com.arrow.pegasus.client.model.UserChangeModel;
import com.arrow.pegasus.client.model.UserPasswordChangeModel;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.service.AuthenticationService;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientUserApi extends ClientApiAbstract implements AuthenticationService {
	private static final String USERS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/users";

	public LastLoginResponseModel findLastLogin(String userId, String applicationId) {
		String method = "findLastLogin";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/lastLogin");
			LastLoginRequestModel model = new LastLoginRequestModel().withUserId(userId)
			        .withApplicationId(applicationId);
			LastLoginResponseModel result = execute(new HttpPost(uri), JsonUtils.toJson(model),
			        LastLoginResponseModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "lastLogin: %s", result.getLastLogin());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public StatusModel syncSamlAccounts(String applicationId, List<SamlAccountModel> samlAccounts) {
		String method = "syncSamlAccounts";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/saml/accounts");
			SamlAccountsModel model = new SamlAccountsModel().withApplicationId(applicationId)
			        .withSamlAccounts(samlAccounts);
			StatusModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), StatusModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "status: %s", result.getStatus());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User authenticate(String username, String password) {
		String method = "authenticate";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/auth");
			UserAuthenticationModel model = new UserAuthenticationModel().withUsername(username).withPassword(password);
			User result = execute(new HttpPost(uri), JsonUtils.toJson(model), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());

			if (result == null)
				throw new InvalidLoginException();

			return result;
		} catch (AcsClientException e) {
			String className = e.getError().getExceptionClassName();
			logError(method, "className: %s", className);
			if (className.equals(NotAuthorizedException.class.getName())) {
				throw new NotAuthorizedException();
			} else if (className.equals(LoginRequiredException.class.getName())) {
				throw new LoginRequiredException();
			} else if (className.equals(LockedAccountException.class.getName())) {
				throw new LockedAccountException();
			} else if (className.equals(UnverifiedAccountException.class.getName())) {
				throw new UnverifiedAccountException();
			} else if (className.equals(RequiredChangePasswordException.class.getName())) {
				throw new RequiredChangePasswordException();
			} else if (className.equals(InActiveAccountException.class.getName())) {
				throw new InActiveAccountException();
			} else if (className.equals(InvalidLoginException.class.getName())) {
				throw new InvalidLoginException();
			} else {
				throw e;
			}
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User samlAuthenticate(String idp, String nameId) {
		String method = "samlAuthenticate";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/saml/auth");
			SamlAuthenticationModel model = new SamlAuthenticationModel().withIdp(idp).withNameId(nameId);
			User result = execute(new HttpPost(uri), JsonUtils.toJson(model), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User findByLogin(String username) {
		String method = "findByLogin";
		try {
			URI uri = new URIBuilder(buildUri(USERS_ROOT_URL + "/logins")).addParameter("login", username).build();
			User result = execute(new HttpGet(uri), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User create(User user, String who) {
		String method = "create";
		try {
			URI uri = buildUri(USERS_ROOT_URL);
			UserChangeModel model = new UserChangeModel().withUser(user).withWho(who);
			User result = execute(new HttpPost(uri), JsonUtils.toJson(model), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());
			return result;
		} catch (AcsClientException e) {
			String className = e.getError().getExceptionClassName();
			logError(method, "className: %s", className);
			if (className.equals(DuplicateKeyException.class.getName())) {
				throw new DuplicateLoginException();
			} else {
				throw e;
			}
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User update(User user, String who) {
		String method = "update";
		try {
			URI uri = buildUri(USERS_ROOT_URL);
			UserChangeModel model = new UserChangeModel().withUser(user).withWho(who);
			User result = execute(new HttpPut(uri), JsonUtils.toJson(model), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<User> findAll() {
		try {
			URI uri = buildUri(USERS_ROOT_URL);
			List<User> result = execute(new HttpGet(uri), new TypeReference<List<User>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<User> findByCompanyIdAndRoleIds(String companyId, String... roleIds) {
		try {
			if (Arrays.asList(roleIds).isEmpty()) {
				return new ArrayList<User>();
			}
			URIBuilder builder = new URIBuilder(buildUri(USERS_ROOT_URL + "/companies/roles")).addParameter("companyId",
			        companyId);
			Arrays.stream(roleIds).forEach(roleId -> builder.addParameter("roleIds", roleId));
			URI uri = builder.build();
			List<User> result = execute(new HttpGet(uri), new TypeReference<List<User>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public String resetPassword(User user, String who) {
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/passwords/reset");
			UserChangeModel model = new UserChangeModel().withUser(user).withWho(who);
			PasswordModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), PasswordModel.class);
			Assert.notNull(result, "password is null");
			return result.getPassword();
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User authenticateChangePassword(String login, String currentPassword) {
		String method = "authenticateChangePassword";
		logDebug(method, "login: %s, current: %s", login, currentPassword);
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/passwords/auth");
			UserAuthenticationModel model = new UserAuthenticationModel().withUsername(login)
			        .withPassword(currentPassword);
			User result = execute(new HttpPost(uri), JsonUtils.toJson(model), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<String> setNewPassword(User user, String newPassword, String who) {
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/passwords");
			UserChangeModel model = new UserPasswordChangeModel().withNewPassword(newPassword).withUser(user)
			        .withWho(who);
			List<String> result = execute(new HttpPost(uri), JsonUtils.toJson(model),
			        new TypeReference<List<String>>() {
			        });
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public User findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(USERS_ROOT_URL + "/ids/" + id);
			User result = execute(new HttpGet(uri), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, login: %s", result.getId(), result.getHid(), result.getLogin());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Page<User> findThemisUsers(PageRequest pageRequest, String companyId, String[] roleIds,
	        EnumSet<UserStatus> statuses, String firstName, String lastName) {
		Assert.notNull(pageRequest, "pageRequest is null");
		try {
			URIBuilder builder = new URIBuilder(buildUri(USERS_ROOT_URL + "/themis"));
			if (StringUtils.isNotBlank(companyId)) {
				builder.addParameter("companyId", companyId);
			}
			if (StringUtils.isNotBlank(firstName)) {
				builder.addParameter("firstName", firstName);
			}
			if (StringUtils.isNotBlank(lastName)) {
				builder.addParameter("lastName", lastName);
			}
			if (roleIds != null) {
				Arrays.stream(roleIds).forEach(roleId -> builder.addParameter("roleIds", roleId));
			}
			if (statuses != null) {
				statuses.forEach(status -> builder.addParameter("statuses", status.toString()));
			}
			builder.addParameter("size", String.valueOf(pageRequest.getPageSize())).addParameter("page",
			        String.valueOf(pageRequest.getPageNumber()));
			Sort sort = pageRequest.getSort();
			if (sort != null) {
				sort.forEach(order -> builder.addParameter("sortDirection", order.getDirection().toString())
				        .addParameter("sortProperty", order.getProperty()));
			}
			URI uri = builder.build();
			PagingResultModel<User> model = execute(new HttpGet(uri), new TypeReference<PagingResultModel<User>>() {
			});
			Page<User> result = new PageImpl<User>(model.getData(), pageRequest, model.getTotalSize());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}
