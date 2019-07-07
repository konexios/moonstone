package com.arrow.kronos.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.AccountRegistrationModel;
import com.arrow.acn.client.model.AccountRegistrationOK;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.JsonUtils;
import com.arrow.kronos.KronosApiConstants;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientRoleApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.security.Role;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/accounts")
public class AccountApi extends BaseApiAbstract {

	@Autowired
	private ClientRoleApi clientRoleApi;
	@Autowired
	private ClientApplicationApi clientApplicationApi;

	@RequestMapping(path = "", method = RequestMethod.POST)
	public AccountRegistrationOK createOrLogin(
	        @ApiParam(value = "account registration model", required = true) @RequestBody(required = false) AccountRegistrationModel body,
	        HttpServletRequest request) {
		String method = "createOrLogin";

		AccountRegistrationModel model = JsonUtils.fromJson(getApiPayload(), AccountRegistrationModel.class);
		model.trim();

		Assert.hasText(model.getName(), "name is required");
		Assert.hasText(model.getEmail(), "email is required");
		Assert.isTrue(EmailValidator.getInstance().isValid(model.getEmail()), "invalid email");
		Assert.hasText(model.getPassword(), "password is required");

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		Application application = null;
		if (!StringUtils.isEmpty(model.getCode())) {
			application = clientApplicationApi.findByCode(model.getCode());
			Assert.notNull(application, "invalid application code");
			Assert.isTrue(validateProduct(application.getId(), ProductSystemNames.KRONOS),
			        "application code product mismatched");
		} else {
			application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
			Assert.notNull(application, "application not found: " + accessKey.getApplicationId());
		}

		String password = model.getPassword();
		// hide password before logging
		model.setPassword("********");
		AuditLog auditLog = auditLog(method, application.getId(), null, accessKey.getId(), JsonUtils.toJson(model),
		        request);

		if (getClientUserApi().findByLogin(model.getEmail()) != null) {
			User user = getClientUserApi().authenticate(model.getEmail(), password);
			if (user != null) {
				auditLog.setObjectId(user.getId());
				getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

				Role role = clientRoleApi.findByNameAndApplicationId(KronosApiConstants.DEFAULT_USER_ROLE,
				        application.getId());
				checkEnabled(role, "defaultUserRole");
				if (!user.getRoleIds().contains(role.getId())) {
					user.getRoleIds().add(role.getId());
					user.getRefRoles().add(role);
					logDebug(method, "added default role: %s", role.getName());
					user = getClientUserApi().update(user, accessKey.getId());
					getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Account.UpdateAccount)
					        .applicationId(application.getId()).productName(ProductSystemNames.KRONOS)
					        .objectId(user.getId()).by(user.getId()).parameter("roleId", role.getId()));
				}

				logInfo(method, "login ok - name: %s, email: %s", model.getName(), model.getEmail());
				getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Account.LoginOK)
				        .applicationId(accessKey.getApplicationId()).productName(ProductSystemNames.KRONOS)
				        .objectId(user.getId()).by(user.getId()));
				return (AccountRegistrationOK) new AccountRegistrationOK().withApplicationHid(application.getHid())
				        .withName(user.getContact().fullName()).withEmail(user.getContact().getEmail())
				        .withHid(user.getHid()).withMessage("login ok");
			} else {
				getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Account.LoginFailed)
				        .applicationId(accessKey.getApplicationId()).productName(ProductSystemNames.KRONOS)
				        .objectId(accessKey.getId()).by(accessKey.getId()).parameter("name", model.getName())
				        .parameter("email", model.getEmail()));
				throw new AcsLogicalException("login failed");
			}
		} else {
			User user = new User();
			user.setAdmin(false);
			user.setCompanyId(application.getCompanyId());
			user.setLogin(model.getEmail());
			user.setPassword(password);
			user.setStatus(UserStatus.Active);

			// set default role
			Role role = clientRoleApi.findByNameAndApplicationId(KronosApiConstants.DEFAULT_USER_ROLE,
			        application.getId());
			checkEnabled(role, "defaultUserRole");
			user.getRoleIds().add(role.getId());
			user.getRefRoles().add(role);
			logDebug(method, "added default role: %s", role.getName());

			Contact contact = new Contact();
			contact.setEmail(model.getEmail());
			String[] tokens = model.getName().split(" ", -1);
			contact.setFirstName(tokens[0].trim());
			if (tokens.length > 1) {
				contact.setLastName(model.getName().substring(contact.getFirstName().length()).trim());
			} else {
				contact.setLastName(" ");
			}
			user.setContact(contact);
			user = getClientUserApi().create(user, accessKey.getId());

			auditLog.setObjectId(user.getId());
			getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

			logInfo(method, "created new user - name: %s, email: %s, id: %s, application: %s", model.getName(),
			        model.getEmail(), user.getId(), application.getName());
			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Account.CreateAccount)
			        .applicationId(application.getId()).productName(ProductSystemNames.KRONOS).objectId(user.getId())
			        .by(user.getId()).parameter("name", model.getName()).parameter("email", model.getEmail()));
			return (AccountRegistrationOK) new AccountRegistrationOK().withApplicationHid(application.getHid())
			        .withName(user.getContact().fullName()).withEmail(user.getContact().getEmail())
			        .withHid(user.getHid()).withMessage("new account created");
		}
	}
}
