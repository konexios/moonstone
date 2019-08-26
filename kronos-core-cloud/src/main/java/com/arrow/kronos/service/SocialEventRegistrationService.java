package com.arrow.kronos.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.kronos.data.SocialEventRegistrationStatuses;
import com.arrow.kronos.repo.SocialEventRegistrationRepository;
import com.arrow.kronos.repo.SocialEventRegistrationSearchParams;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientCompanyApi;
import com.arrow.pegasus.client.api.ClientSocialEventApi;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.profile.Zone;

import moonstone.acs.AcsLogicalException;

@Service
public class SocialEventRegistrationService extends KronosServiceAbstract {

	private static final String USER_LAST_NAME_PLACEHOLDER = "---";

	@Autowired
	private SocialEventRegistrationRepository socialEventRegistrationRepository;
	@Autowired
	private ClientSocialEventApi clientSocialEventApi;
	@Autowired
	private ClientUserApi clientUserApi;
	@Autowired
	private ClientCompanyApi clientCompanyApi;
	@Autowired
	private EmailService emailService;

	public SocialEventRegistrationRepository getSocialEventRegistrationRepository() {
		return socialEventRegistrationRepository;
	}

	public ClientSocialEventApi getClientSocialEventApi() {
		return clientSocialEventApi;
	}

	public SocialEventRegistration create(SocialEventRegistration socialEventRegistration, String who) {
		String method = "create";

		// logical checks
		if (socialEventRegistration == null) {
			logInfo(method, "socialEventRegistration is null");
			throw new AcsLogicalException("socialEventRegistration is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// generate verificationCode
		if (StringUtils.isEmpty(socialEventRegistration.getVerificationCode()))
			socialEventRegistration.setVerificationCode(generateCode());

		// insert
		socialEventRegistration = socialEventRegistrationRepository.doInsert(socialEventRegistration, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create()
		        .type(KronosAuditLog.SocialEventRegistration.CreateSocialEventRegistration)
		        .applicationId(socialEventRegistration.getApplicationId()).objectId(socialEventRegistration.getId())
		        .by(who).parameter("name", socialEventRegistration.getName())
		        .parameter("socialEventId", socialEventRegistration.getSocialEventId()));

		return socialEventRegistration;
	}

	public SocialEventRegistration update(SocialEventRegistration socialEventRegistration, String who) {
		String method = "update";

		// logical checks
		if (socialEventRegistration == null) {
			logInfo(method, "socialEventRegistration is null");
			throw new AcsLogicalException("socialEventRegistration is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		socialEventRegistration = socialEventRegistrationRepository.doSave(socialEventRegistration, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create()
		        .type(KronosAuditLog.SocialEventRegistration.UpdateSocialEventRegistration)
		        .applicationId(socialEventRegistration.getApplicationId()).objectId(socialEventRegistration.getId())
		        .by(who).parameter("name", socialEventRegistration.getName())
		        .parameter("socialEventId", socialEventRegistration.getSocialEventId()));

		// clear cache
		SocialEventRegistration cachedSocialEventRegistration = getKronosCache()
		        .findSocialEventRegistrationById(socialEventRegistration.getId());
		if (cachedSocialEventRegistration != null) {
			getKronosCache().clearSocialEventRegistration(cachedSocialEventRegistration);
		}

		return socialEventRegistration;
	}

	public void delete(SocialEventRegistration socialEventRegistration, String who) {
		String method = "delete";

		// logical checks
		if (socialEventRegistration == null) {
			logInfo(method, "socialEventRegistration is null");
			throw new AcsLogicalException("socialEventRegistration is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// delete
		logInfo(method, "socialEventRegistrationRepository ...");
		socialEventRegistrationRepository.delete(socialEventRegistration);
		logInfo(method, "Device id=" + socialEventRegistration.getId() + " has been deleted");

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create()
		        .type(KronosAuditLog.SocialEventRegistration.DeleteSocialEventRegistration)
		        .productName(ProductSystemNames.KRONOS).applicationId(socialEventRegistration.getApplicationId())
		        .objectId(socialEventRegistration.getId()).by(who).parameter("name", socialEventRegistration.getName())
		        .parameter("socialEventId", socialEventRegistration.getSocialEventId()));

		// clear cache
		getKronosCache().clearSocialEventRegistration(socialEventRegistration);
	}

	public SocialEventRegistration populateRefs(SocialEventRegistration socialEventRegistration) {

		if (socialEventRegistration != null) {
			if (socialEventRegistration.getRefApplication() == null
			        && !StringUtils.isEmpty(socialEventRegistration.getApplicationId())) {
				socialEventRegistration.setRefApplication(
				        getCoreCacheService().findApplicationById(socialEventRegistration.getApplicationId()));
			}

			if (socialEventRegistration.getRefSocialEvent() == null
			        && !StringUtils.isEmpty(socialEventRegistration.getSocialEventId())) {
				socialEventRegistration
				        .setRefSocialEvent(clientSocialEventApi.findById(socialEventRegistration.getSocialEventId()));
			}

			if (socialEventRegistration.getRefUser() == null
			        && !StringUtils.isEmpty(socialEventRegistration.getUserId())) {
				socialEventRegistration
				        .setRefUser(getCoreCacheService().findUserById(socialEventRegistration.getUserId()));
			}
		}

		return socialEventRegistration;
	}

	public Page<SocialEventRegistration> findSocialEventRegistrations(Pageable pageable,
	        SocialEventRegistrationSearchParams params) {
		return socialEventRegistrationRepository.findSocialEventRegistrations(pageable, params);
	}

	public List<SocialEventRegistration> findSocialEventRegistrations(SocialEventRegistrationSearchParams params) {
		return socialEventRegistrationRepository.findSocialEventRegistrations(params);
	}

	public String generateCode() {
		String result = "";
		Random rand = new Random();
		do {
			StringBuilder code = new StringBuilder();
			// 4-digit code
			for (int i = 0; i < 4; i++) {
				code.append(rand.nextInt(10));
			}
			result = code.toString();
		} while (socialEventRegistrationRepository.findByVerificationCode(result) != null);
		return result;
	}

	public SocialEventRegistration register(SocialEventRegistration socialEventRegistration, String who) {
		Assert.notNull(socialEventRegistration, "socialEventRegistration is null");
		Assert.hasText(who, "who is empty");

		if (socialEventRegistration.getStatus() != SocialEventRegistrationStatuses.PENDING) {
			throw new AcsLogicalException("Account is not in PENDING status");
		}
		socialEventRegistration.setStatus(SocialEventRegistrationStatuses.REGISTERED);
		socialEventRegistration = update(socialEventRegistration, who);

		// email verification code
		sendVerificationCode(socialEventRegistration);

		return socialEventRegistration;
	}

	public SocialEventRegistration verify(SocialEventRegistration socialEventRegistration, String who) {
		Assert.notNull(socialEventRegistration, "socialEventRegistration is null");
		Assert.hasText(who, "who is empty");

		if (socialEventRegistration.getStatus() != SocialEventRegistrationStatuses.REGISTERED) {
			throw new AcsLogicalException("Account is not in REGISTERED status");
		}
		User user = getCoreCacheService().findUserById(socialEventRegistration.getUserId());
		if (user == null) {
			throw new AcsLogicalException("User not found");
		}
		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		if (company == null) {
			throw new AcsLogicalException("Company not found");
		}
		// update company
		company.setName(socialEventRegistration.getName());
		company = clientCompanyApi.update(company, who);
		// update user
		user.setLogin(getCryptoService().getCrypto().internalEncrypt(socialEventRegistration.getEmail().toLowerCase()));
		user.setHashedLogin(getCryptoService().getCrypto().internalHash(socialEventRegistration.getEmail().toLowerCase()));
		user.setPassword(socialEventRegistration.getHashedPassword());
		user.setSalt(socialEventRegistration.getSalt());
		user.setStatus(UserStatus.Active);
		Contact contact = user.getContact();
		if (contact == null) {
			contact = new Contact();
		}
		contact.setEmail(socialEventRegistration.getEmail());
		contact.setFirstName(socialEventRegistration.getName());
		contact.setLastName(USER_LAST_NAME_PLACEHOLDER);
		user.setContact(contact);
		user = clientUserApi.update(user, who);
		// update registration status to Verified
		socialEventRegistration.setStatus(SocialEventRegistrationStatuses.VERIFIED);
		socialEventRegistration = update(socialEventRegistration, who);
		// send welcome email
		sendWelcomeEmail(socialEventRegistration);

		return socialEventRegistration;
	}

	public void resendVerificationCode(SocialEventRegistration socialEventRegistration) {
		Assert.notNull(socialEventRegistration, "socialEventRegistration is null");
		if (socialEventRegistration.getStatus() != SocialEventRegistrationStatuses.REGISTERED) {
			throw new AcsLogicalException("Account is not in REGISTERED status");
		}
		sendVerificationCode(socialEventRegistration);
	}

	private void sendVerificationCode(SocialEventRegistration socialEventRegistration) {
		Zone zone = getPlatformConfigService().getConfig().getRefZone();
		Map<String, Object> emailParams = new EmailService.ModelBuilder()
		        .withParameter("name", socialEventRegistration.getName())
		        .withParameter("verificationCode", socialEventRegistration.getVerificationCode())
		        .withParameter("zoneSystemName", zone.getSystemName())
		        .withParameter("zoneDescription", zone.getDescription()).build();
		emailService.sendSocialEventRegistrationEmail(socialEventRegistration.getEmail(), emailParams);
	}
	
	public void sendWelcomeEmail(SocialEventRegistration socialEventRegistration) {
		Assert.notNull(socialEventRegistration, "socialEventRegistration is null");
		Zone zone = getPlatformConfigService().getConfig().getRefZone();
		Map<String, Object> emailParams = new EmailService.ModelBuilder()
				.withParameter("name", socialEventRegistration.getName())
		        .withParameter("zoneSystemName", zone.getSystemName())
		        .withParameter("zoneDescription", zone.getDescription()).build();
		emailService.sendSocialEventRegistrationVerifiedEmail(socialEventRegistration.getEmail(),
				emailParams);
	}
	
	public SocialEventRegistration findCaseInsensitiveByEmail(String email) {
		Assert.hasText(email, "email is empty");
		SocialEventRegistration result = null;
		List<SocialEventRegistration> emailRegistrations = socialEventRegistrationRepository.findCaseInsensitiveByEmail(email);
		if (!emailRegistrations.isEmpty())
			result = emailRegistrations.get(0);
		return result;
	}
}
