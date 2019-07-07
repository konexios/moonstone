package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.KioskSignup;
import com.arrow.kronos.repo.KioskSignupRepository;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.service.TempTokenService;

@Service
public class KioskSignupService extends KronosServiceAbstract {

	@Autowired
	private KioskSignupRepository kioskSignupRepository;
	@Autowired
	private TempTokenService tempTokenService;
	@Autowired
	private EmailService emailService;

	@Value("${com.arrow.kronos.kiosk.data.companyId:}")
	private String configCompanyId;
	@Value("${com.arrow.kronos.kiosk.token.expire.seconds:}")
	private Long configTokenExpireSeconds;

	public KioskSignupRepository getKioskSignupRepository() {
		return kioskSignupRepository;
	}

	public KioskSignup create(KioskSignup kioskSignup, String who) {
		Assert.notNull(kioskSignup, "kioskSignup is null");
		Assert.hasText(who, "who is empty");

		String method = "create";
		logInfo(method, "...");

		// persist
		kioskSignup = kioskSignupRepository.doInsert(kioskSignup, who);

		return kioskSignup;
	}

	public KioskSignup update(KioskSignup kioskSignup, String who) {
		Assert.notNull(kioskSignup, "kioskSignup is null");
		Assert.hasText(who, "who is empty");

		String method = "update";
		logInfo(method, "...");

		// persist
		kioskSignup = kioskSignupRepository.doSave(kioskSignup, who);

		return kioskSignup;
	}

	public KioskSignup signup(KioskSignup kioskSignup, String who) {
		Assert.notNull(kioskSignup, "kioskSignup is null");
		Assert.hasText(kioskSignup.getEmail(), "email is empty");
		Assert.hasText(who, "who is empty");

		validateConfig();

		KioskSignup existing = kioskSignupRepository.findByEmail(kioskSignup.getEmail());
		if (existing != null)
			throw new AcsLogicalException("email already used");

		kioskSignup = create(kioskSignup, who);
		TempToken tempToken = createTempToken(kioskSignup, who);
		sendEmail(kioskSignup.getEmail(), tempToken);

		return kioskSignup;
	}

	public void sendEmail(String email, TempToken tempToken) {
		Assert.hasText(email, "email is empty");
		Assert.notNull(tempToken, "tempToken is null");

		String method = "sendEmail";
		logInfo(method, "...");

		emailService.sendKioskSignupEmail(email, tempToken);
	}

	public KioskSignup resendEmail(String email, String who) {
		Assert.hasText(email, "email is empty");
		Assert.hasText(who, "who is empty");

		validateConfig();

		String method = "resendEmail";
		logInfo(method, "...");

		KioskSignup kioskSignup = kioskSignupRepository.findByEmail(email);
		Assert.notNull(kioskSignup, "kioskSignup not found! email=" + email);

		TempToken tempToken = createTempToken(kioskSignup, who);
		sendEmail(kioskSignup.getEmail(), tempToken);

		return kioskSignup;
	}

	private TempToken createTempToken(KioskSignup kioskSignup, String who) {
		// create tempToken
		TempToken tempToken = new TempToken();
		tempToken.setCompanyId(configCompanyId);
		tempToken.setSingleUse(true);
		tempToken.setTimeToExpireSeconds(configTokenExpireSeconds);
		// kiosk signup properties
		tempToken.addProperty("kioskSignupId", kioskSignup.getId());
		tempToken.addProperty("email", kioskSignup.getEmail());
		tempToken.addProperty("referralCode", kioskSignup.getReferralCode());
		tempToken.addProperty("eventCode", kioskSignup.getEventCode());
		tempToken = tempTokenService.create(tempToken, who);

		return tempToken;
	}

	private void validateConfig() {
		Assert.hasText(configCompanyId, "com.arrow.kronos.kiosk.data.companyId is not defined");
		Assert.notNull(configTokenExpireSeconds, "com.arrow.kronos.kiosk.token.expire.seconds is null");
	}
}