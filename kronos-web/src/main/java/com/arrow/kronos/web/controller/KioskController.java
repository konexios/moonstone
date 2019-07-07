package com.arrow.kronos.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.KioskSignup;
import com.arrow.kronos.service.KioskSignupService;
import com.arrow.kronos.web.model.KioskRegistrationModels;
import com.arrow.pegasus.CoreConstant;

@RestController
@RequestMapping("/api/kronos/kiosk")
public class KioskController extends BaseControllerAbstract {

	@Autowired
	private KioskSignupService kioskSignupService;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void signup(@RequestBody KioskRegistrationModels.SignupModel model) {
		Assert.notNull(model, "model is null");

		String method = "signup";
		logDebug(method, "signup entered...");

		KioskSignup kioskSignup = new KioskSignup();
		kioskSignup.setEmail(model.getEmail());
		kioskSignup.setReferralCode(model.getReferralCode());
		kioskSignup.setEventCode(model.getEventCode());

		kioskSignup = kioskSignupService.signup(kioskSignup, CoreConstant.ADMIN_USER);
		logDebug(method, "kioskSignupId: %s", kioskSignup.getId());
	}

	@RequestMapping(value = "/resend", method = RequestMethod.POST)
	public void resend(@RequestBody KioskRegistrationModels.SignupModel model) {
		Assert.notNull(model, "model is null");

		String method = "resend";
		logDebug(method, "resend entered...");

		KioskSignup kioskSignup = kioskSignupService.resendEmail(model.getEmail(), CoreConstant.ADMIN_USER);
		logDebug(method, "kioskSignupId: %s", kioskSignup.getId());
	}
}
