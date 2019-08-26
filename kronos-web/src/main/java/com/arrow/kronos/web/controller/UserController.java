package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.service.DeveloperService;
import com.arrow.kronos.service.UserRegistrationService;
import com.arrow.kronos.web.model.UserModels;
import com.arrow.kronos.web.model.UserModels.PasswordPolicyModel;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.SmtpEmailSender;

import moonstone.acs.AcsLogicalException;

@RestController
@RequestMapping("/api/kronos/user")
public class UserController extends BaseControllerAbstract {

	@Autowired
	private SmtpEmailSender smtpEmailSender;

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private DeveloperService developerService;

	@RequestMapping(value = "/password-policy", method = RequestMethod.GET)
	public PasswordPolicy getPasswordPolicy() {
		User user = getCoreCacheService().findUserById(getUserId());

		return getPasswordPolicy(user);
	}

	@RequestMapping(value = "/password-policy/{login:.+}", method = RequestMethod.GET)
	public PasswordPolicy getPasswordPolicy(@PathVariable String login) {
		User user = getClientUserApi().findByLogin(login);

		return getPasswordPolicy(user);
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public UserModels.PasswordChangeResult changePassword(
	        @RequestBody UserModels.PasswordChangeModel passwordChangeModel) {
		return changePassword(getAuthenticatedUser().getLogin(), passwordChangeModel.getCurrentPassword(),
		        passwordChangeModel.getNewPassword());
	}

	@RequestMapping(value = "/change-password/{login:.+}", method = RequestMethod.POST)
	public UserModels.PasswordChangeResult changePassword(@PathVariable String login,
	        @RequestBody UserModels.PasswordChangeModel passwordChangeModel) {
		String method = "changePassword";
		User user = getClientUserApi().findByLogin(login);
		Assert.isTrue(user.getStatus() == UserStatus.PasswordReset, "You are not required to change your password");

		logDebug(method, "login: %s, current: %s, new: %s", login, passwordChangeModel.getCurrentPassword(),
		        passwordChangeModel.getNewPassword());
		return changePassword(login, passwordChangeModel.getCurrentPassword(), passwordChangeModel.getNewPassword());
	}

	@RequestMapping(value = "/reset-password/{login:.+}", method = RequestMethod.POST)
	public void changePassword(@PathVariable String login) {
		// TODO this function must be protected from robots (using captcha?)
		Assert.hasText(login, "login is empty");
		User user = getClientUserApi().findByLogin(login);
		if (user == null)
		    // TODO should we indicate to the caller that user doesn't exist?
		    throw new AcsLogicalException("User Name is invalid. Please try again.");

		String method = "forgotPassword";

		// reset password
		// assuming user resets the password for himself
		String tempPassword = getClientUserApi().resetPassword(user, user.getId());
		logDebug(method, "tempPassword: %s", tempPassword);

		StringBuilder sb = new StringBuilder();
		sb.append(user.getContact().fullName() + ",");
		sb.append("\n\n");
		sb.append("Your Arrow Connect password has been reset.");
		sb.append(" Use the following login and password to access your account.");
		sb.append("\n\nLogin " + login);
		sb.append("\nPassword " + tempPassword);
		sb.append("\n\nOnce you login you will be required to change your password.");
		sb.append("\n\nThanks,\n\nArrow Connect Support Team");

		smtpEmailSender.send(new String[] { user.getContact().getEmail() }, null, "Arrow Connect Password Reset",
		        sb.toString(), EmailContentType.PLAIN_TEXT);
	}

	private PasswordPolicy getPasswordPolicy(User user) {
		Assert.notNull(user, "user is null");
		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company is null");

		return new PasswordPolicyModel(company.getPasswordPolicy());
	}

	private UserModels.PasswordChangeResult changePassword(String login, String currentPassword, String newPassword) {
		User user = getClientUserApi().authenticateChangePassword(login, currentPassword);

		List<String> messages;
		if (user != null) {
			messages = getClientUserApi().setNewPassword(user, newPassword, user.getId());
			if (messages != null && messages.size() == 0) {
				// refresh user after password changing
				user = getClientUserApi().findById(user.getId());
				if (user != null) {
					// change pwd successful
					// UserRegistration userRegistration =
					// userRegistrationService.getUserRegistrationRepository()
					// .findByEmail(login);
					// if (userRegistration != null &&
					// userRegistration.getStatus() ==
					// UserRegistrationStatus.Verified) {
					// // activate account
					// developerService.activateAccount(user, user.getId());
					// }
				} else {
					messages = new ArrayList<>(1);
					messages.add("User not found");
				}
			}
		} else {
			messages = new ArrayList<>(1);
			messages.add("Failed to check the current password");
		}

		return new UserModels.PasswordChangeResult(messages);
	}
}
