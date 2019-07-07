package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.PasswordModel;
import com.arrow.acs.client.model.StatusMessagesModel;
import com.arrow.acs.client.model.UserAppAuthenticationModel;
import com.arrow.acs.client.model.UserAppModel;
import com.arrow.acs.client.model.UserAuthenticationModel;
import com.arrow.acs.client.model.UserModel;
import com.arrow.acs.client.model.UserStatus;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.repo.params.UserSearchParams;

import springfox.documentation.annotations.ApiIgnore;

@RestController(value = "pegasusUserApi")
@RequestMapping("/api/v1/pegasus/users")
public class UserApi extends BaseApiAbstract {

	@RequestMapping(path = "/auth", method = RequestMethod.POST)
	public UserModel authenticate(@RequestBody(required = false) UserAuthenticationModel body) {
		validateRootAccess();
		UserAuthenticationModel model = JsonUtils.fromJson(getApiPayload(), UserAuthenticationModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getUsername(), "username is empty");
		Assert.hasText(model.getPassword(), "password is empty");
		User user = getUserService().authenticate(model.getUsername(), model.getPassword());
		Assert.notNull(user, "user not found");
		return toUserModel(user);
	}

	@ApiIgnore
	@RequestMapping(path = "/auth2", method = RequestMethod.POST)
	public UserAppModel authenticate2(@RequestBody(required = false) UserAppAuthenticationModel body) {
		// TODO validate access
		// validateRootAccess();

		UserAppAuthenticationModel model = JsonUtils.fromJson(getApiPayload(), UserAppAuthenticationModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getUsername(), "username is empty");
		Assert.hasText(model.getPassword(), "password is empty");
		Assert.hasText(model.getApplicationCode(), "applicationCode is empty");

		Application application = getCoreCacheService().findApplicationByCode(model.getApplicationCode());
		Assert.notNull(application, "application not found");
		Assert.isTrue(application.isEnabled(), "application is disabled");

		User user = getUserService().authenticate(model.getUsername(), model.getPassword(), application);
		Assert.notNull(user, "user not found");

		UserAppModel result = toUserModel(new UserAppModel(), user);
		result.setUserHid(user.getHid());
		result.setApplicationHid(application.getHid());
		result.setApplicationName(application.getName());
		if (application.getZoneId() != null) {
			Zone zone = getCoreCacheService().findZoneById(application.getZoneId());
			if (zone != null) {
				result.setZoneSystemName(zone.getSystemName());
				result.setZoneHid(zone.getHid());
				result.setZoneName(zone.getName());
				Region region = getCoreCacheService().findRegionById(zone.getRegionId());
				if (region != null) {
					result.setRegionHid(region.getHid());
					result.setRegionName(region.getName());
				}
			}
		}
		return result;
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public UserModel findByHid(@PathVariable String hid) {
		validateCanReadUserWithHid(hid);
		User user = getCoreCacheService().findUserByHid(hid);
		Assert.notNull(user, "user not found");
		return toUserModel(user);
	}

	@RequestMapping(path = "/logins", method = RequestMethod.GET)
	public UserModel findByLogin(@RequestParam(name = "login", required = true) String login) {
		validateRootAccess();
		Assert.hasText(login, "login is empty");
		User user = getUserService().getUserRepository().doFindByLogin(login);
		Assert.notNull(user, "user not found");
		return toUserModel(user);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<UserModel> findBy(
			// @formatter:off
			@RequestParam(name = "login", required = false) String login,
			@RequestParam(name = "companyHid", required = false) String companyHid,
			@RequestParam(name = "status", required = false) UserStatus status,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName,
			@RequestParam(name = "sipUri", required = false) String sipUri,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "roleHids", required = false) String[] roleHids,
			@RequestParam(name = "_page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "_size", required = false, defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
		// @formatter:on

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		EnumSet<com.arrow.pegasus.data.profile.UserStatus> statuses = EnumSet
				.noneOf(com.arrow.pegasus.data.profile.UserStatus.class);
		if (status != null) {
			statuses.add(fromUserStatusModel(status));
		}
		Company company = null;
		if (companyHid != null) {
			company = getCoreCacheService().findCompanyByHid(companyHid);
		} else {
			company = accessKey.getRefCompany();
		}
		Assert.notNull(company, "company not found");
		if (!accessKey.canRead(company)) {
			throw new NotAuthorizedException();
		}
		PageRequest pageRequest = PageRequest.of(page, size);
		PagingResultModel<UserModel> result = new PagingResultModel<UserModel>();
		result.setPage(page);
		result.setSize(pageRequest.getPageSize());

		UserSearchParams params = new UserSearchParams();
		params.setFirstName(firstName);
		params.setLastName(lastName);
		params.setLogin(login);
		params.setSipUri(sipUri);
		params.setEmail(email);
		params.addCompanyIds(company.getId());
		params.setStatuses(statuses);
		if (roleHids != null && roleHids.length > 0) {
			List<String> roleIds = Arrays.asList(roleHids).stream().map(hid -> getCoreCacheService().findRoleByHid(hid))
					.filter(role -> role != null).map(role -> role.getId()).collect(Collectors.toList());
			if (roleIds.isEmpty()) {
				return result;
			}
			params.addRoleIds(roleIds.toArray(new String[roleIds.size()]));
		}
		Page<User> users = getUserService().getUserRepository().findUsers(pageRequest, params);
		List<UserModel> data = new ArrayList<>();
		users.forEach(user -> {
			data.add(toUserModel(user));
		});
		result.setData(data);
		result.setTotalPages(users.getTotalPages());
		result.setTotalSize(users.getTotalElements());
		return result;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel createUser(@RequestBody(required = false) UserModel body) {
		AccessKey accessKey = validateApplicationOwner();

		UserModel model = JsonUtils.fromJson(getApiPayload(), UserModel.class);
		Assert.notNull(model, "UserModel is null");
		Assert.hasText(model.getLogin(), "login is empty");
		User user = getUserService().getUserRepository().doFindByLogin(model.getLogin());
		if (user != null) {
			return new HidModel().withHid(user.getHid()).withMessage("User already exists");
		} else {
			user = fromUserModel(model);
			User result = getUserService().create(user, accessKey.getId());
			return new HidModel().withHid(result.getHid()).withMessage("OK");
		}
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel updateUser(@PathVariable(value = "hid") String hid, @RequestBody(required = false) UserModel body) {
		AccessKey accessKey = validateApplicationOwner();

		UserModel model = JsonUtils.fromJson(getApiPayload(), UserModel.class);
		Assert.notNull(model, "UserModel is null");
		model.setHid(hid);

		User user = getCoreCacheService().findUserByHid(hid);
		Assert.notNull(user, "user is not found");
		if (StringUtils.isNotBlank(model.getCompanyHid())) {
			Company company = getCoreCacheService().findCompanyByHid(model.getCompanyHid());
			Assert.notNull(company, "company is not found");
			user.setCompanyId(company.getId());
		}
		if (model.getContact() != null) {
			user.setContact(populateContact(user.getContact(), model.getContact()));
		}
		if (model.getAddress() != null) {
			user.setAddress(fromAddressModel(model.getAddress()));
		}
		if (StringUtils.isNotBlank(model.getLogin())
				&& !user.getHashedLogin().equals(getCryptoService().getCrypto().internalHash(model.getLogin()))) {
			User tempUser = getUserService().getUserRepository().doFindByLogin(model.getLogin());
			Assert.isNull(tempUser, "duplicate login");
			user.setHashedLogin(getCryptoService().getCrypto().internalHash(model.getLogin()));
			user.setLogin(getCryptoService().getCrypto().internalEncrypt(model.getLogin()));
		}
		if (model.getRoleHids() != null && !model.getRoleHids().isEmpty()) {
			user.setRoleIds(populateIds(model.getRoleHids().stream().map(this::getValidatedRole)));
		}
		if (model.getStatus() != null) {
			user.setStatus(fromUserStatusModel(model.getStatus()));
		}
		User result = getUserService().update(user, accessKey.getId());
		return new HidModel().withHid(result.getHid()).withMessage("OK");
	}

	AccessKey validateCanReadUserWithHid(String hid) {
		String method = "validateCanReadUserWithHid";
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		User user = getCoreCacheService().findUserByHid(hid);
		Assert.notNull(user, "user not found");
		if (!StringUtils.equals(accessKey.getCompanyId(), user.getCompanyId())) {
			logWarn(method, "companyId mismatched!");
		}
		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company not found");
		if (!accessKey.canRead(company)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	AccessKey validateCanWriteCompanyWithId(String id) {
		String method = "validateCanWriteCompanyWithId";
		Company company = getCoreCacheService().findCompanyById(id);
		Assert.notNull(company, "invalid company id");
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!StringUtils.equals(accessKey.getCompanyId(), company.getId())) {
			logWarn(method, "companyId mismatched!");
		}
		if (!hasPrivilege(accessKey::canWrite, company)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	@RequestMapping(path = "/{hid}/reset-password", method = RequestMethod.POST)
	public PasswordModel resetPassword(@PathVariable(value = "hid") String hid) {
		User user = getCoreCacheService().findUserByHid(hid);
		Assert.notNull(user, "user is not found");
		AccessKey accessKey = validateCanWriteCompanyWithId(user.getCompanyId());
		String password = getUserService().resetPassword(user, accessKey.getId());
		return new PasswordModel().withPassword(password);
	}

	@RequestMapping(path = "/{hid}/set-new-password", method = RequestMethod.POST)
	public StatusMessagesModel setNewPassword(@PathVariable(value = "hid") String hid,
			@RequestBody(required = false) PasswordModel body) {
		User user = getCoreCacheService().findUserByHid(hid);
		Assert.notNull(user, "user is not found");
		AccessKey accessKey = validateCanWriteCompanyWithId(user.getCompanyId());

		PasswordModel model = JsonUtils.fromJson(getApiPayload(), PasswordModel.class);
		Assert.notNull(model, "password model is null");
		Assert.hasText(model.getPassword(), "password is empty");

		List<String> result = getUserService().setNewPassword(user, model.getPassword(), accessKey.getId());
		if (result.size() == 0) {
			return StatusMessagesModel.OK;
		}
		return StatusMessagesModel.error(result);
	}
}
