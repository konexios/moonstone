package com.arrow.kronos.service;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientCompanyApi;
import com.arrow.pegasus.client.api.ClientMiramontiApi;
import com.arrow.pegasus.client.api.ClientSocialEventApi;
import com.arrow.pegasus.data.MiramontiTenant;
import com.arrow.pegasus.data.SocialEvent;

@Service("kronosMiramontiService")
public class MiramontiService extends KronosServiceAbstract {

	private final static String MIRAMONTI_EVENT_NAME = "Miramonti";

	@Autowired
	AdminService adminService;
	@Autowired
	KronosApplicationService kronosApplicationService;
	@Autowired
	SocialEventRegistrationService socialEventRegistrationService;

	@Autowired
	ClientMiramontiApi clientMiramontiApi;
	@Autowired
	ClientCompanyApi clientCompanyApi;
	@Autowired
	ClientApplicationApi clientApplicationApi;
	@Autowired
	ClientSocialEventApi clientSocialEventApi;
	@Autowired
	KronosApplicationProvisioningService kronosApplicationProvisioningService;

	public SocialEventRegistration createRegistration(String number, String applicationEngineId, String who) {
		String method = "createRegistration";
		Assert.hasText(number, "number is empty");

		logInfo(method, "creating pegasus company for: %s", number);
		MiramontiTenant tenant = clientMiramontiApi.createCompany(number,
				getPlatformConfigService().getConfig().getRefZone().getId(), applicationEngineId);
		logInfo(method, "created companyId: %s", tenant.getCompanyId());

		logInfo(method, "provisioning kronos application for: %s", number);
		KronosApplication kronosApp = kronosApplicationProvisioningService
				.provisionApplication(tenant.getApplicationId(), true, who);
		logInfo(method, "created kronos applicationId: %s", kronosApp.getId());

		SocialEventRegistration registration = new SocialEventRegistration();
		registration.setCompanyId(tenant.getCompanyId());
		registration.setSubscriptionId(tenant.getSubscriptionId());
		registration.setApplicationId(tenant.getApplicationId());
		registration.setUserId(tenant.getUserId());
		registration.setVerificationCode(generateVerificationCode());
		registration.setOrigEmail(tenant.getLogin());
		registration.setOrigEncryptedPassword(tenant.getEncryptedPassword());

		SocialEvent event = clientSocialEventApi.findByName(MIRAMONTI_EVENT_NAME);
		Assert.notNull(event, "Miramonti event not found: " + MIRAMONTI_EVENT_NAME);
		registration.setSocialEventId(event.getId());

		logInfo(method, "creating registration for: %s", number);
		socialEventRegistrationService.create(registration, who);
		logInfo(method, "created new registration: %s --> %s", number, registration.getId());

		return registration;
	}

	public Long deleteRegistration(String id, String who) {
		String method = "deleteRegistration";
		Assert.hasText(id, "id is empty");

		long result = 0;
		SocialEventRegistration registration = socialEventRegistrationService.getSocialEventRegistrationRepository()
				.findById(id).orElse(null);
		Assert.notNull(registration, "registration not found: " + id);

		try {
			logInfo(method, "deleting kronos applicationId: %s", registration.getApplicationId());
			Long count = adminService.deleteApplication(registration.getApplicationId(), who);
			logInfo(method, "kronos application deleted: %s, count: %d", registration.getApplicationId(), count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting kronos applicationId: " + registration.getApplicationId(), t);
		}

		try {
			logInfo(method, "deleting pegasus companyId: %s", registration.getCompanyId());
			Long count = clientCompanyApi.delete(registration.getCompanyId());
			logInfo(method, "pegasus company deleted: %s, count: %d", registration.getCompanyId(), count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting pegasus companyId: " + registration.getCompanyId(), t);
		}

		try {
			logInfo(method, "deleting registration: %s", id);
			socialEventRegistrationService.delete(registration, who);
			result++;
		} catch (Throwable t) {
			logError(method, "error deleting registration: " + id, t);
		}

		return result;
	}

	public String generateVerificationCode() {
		return new RandomStringGenerator.Builder().withinRange('0', '9').filteredBy(CharacterPredicates.DIGITS).build()
				.generate(4);
	}
}
