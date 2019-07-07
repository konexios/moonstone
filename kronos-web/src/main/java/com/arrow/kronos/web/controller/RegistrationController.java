package com.arrow.kronos.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.KioskSignup;
import com.arrow.kronos.data.UserRegistration;
import com.arrow.kronos.service.DeveloperService;
import com.arrow.kronos.web.model.UserModels;
import com.arrow.kronos.web.model.UserRegistrationModels;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.User;

@RestController
@RequestMapping("/api/kronos/registration")
public class RegistrationController extends BaseControllerAbstract {

	@Autowired
	private DeveloperService developerService;

	@RequestMapping(value = "/{token}/preregistration", method = RequestMethod.GET)
	public UserRegistrationModels.PreRegistrationModel preRegistration(@PathVariable String token) {

		if (StringUtils.isEmpty(token))
			return null;

		KioskSignup kioskSignup = developerService.findPreRegistration(token, CoreConstant.ADMIN_USER);
		if (kioskSignup == null)
			return null;

		return new UserRegistrationModels.PreRegistrationModel().withEmail(kioskSignup.getEmail())
		        .withEventCode(kioskSignup.getEventCode()).withReferralCode(kioskSignup.getReferralCode());
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(@RequestBody UserRegistrationModels.UserRegistrationModel model) {
		Assert.notNull(model, "userRegistrationModel is null");

		String method = "register";
		logDebug(method, "register entered...");

		UserRegistration userRegistration = new UserRegistration();
		userRegistration.setCompanyName(model.getCompanyName());
		userRegistration.setCompanyWebSite(model.getCompanyWebSite());
		userRegistration.setEmail(model.getEmail());
		userRegistration.setFirstName(model.getFirstName());
		userRegistration.setLastName(model.getLastName());
		userRegistration.setProjectDescription(model.getProjectDescription());
		userRegistration.setTitle(model.getTitle());
		userRegistration.setReferralCode(model.getReferralCode());

		User user = developerService.register(userRegistration, CoreConstant.ADMIN_USER);
		logDebug(method, "userId: %s", user.getId());
	}

	@RequestMapping(value = "/{token}/verify", method = RequestMethod.GET)
	public UserModels.VerifiedUser verifyAccount(@PathVariable String token) {
		Assert.hasText(token, "token is empty");

		String method = "verifyAccount";
		logDebug(method, "verifyAccount entered...");

		User user = developerService.verifyAccount(token, CoreConstant.ADMIN_USER);

		return new UserModels.VerifiedUser().withLogin(user.getLogin()).withTempPassword(user.getPassword());
	}

	@RequestMapping(value = "/{token}/resendVerifyEmail", method = RequestMethod.GET)
	public void resendVerifyEmail(@PathVariable String token) {
		Assert.hasText(token, "token is empty");

		String method = "resendVerifyEmail";
		logDebug(method, "resendVerifyEmail entered...");

		developerService.resendVerifyEmail(token, CoreConstant.ADMIN_USER);
	}
}
