package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.repo.ApplicationRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class ApplicationService extends BaseServiceAbstract {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private AccessKeyService accessKeyService;

	@Autowired
	private RoleService roleService;

	public ApplicationRepository getApplicationRepository() {
		return applicationRepository;
	}

	public void createVaultLogin(Application application, String adminToken) {
		// obsolete
	}

	public Application create(Application application, String who) {
		Assert.notNull(application, "application is null");

		String method = "create";

		// logical checks
		if (application == null) {
			logInfo(method, "application is null");
			throw new AcsLogicalException("application is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// generate code if one is not provided
		if (StringUtils.isEmpty(application.getCode()))
			application.setCode(generateCode());

		application = applicationRepository.doInsert(application, who);
		application = populate(application);

		logInfo(method, "creating owner key ...");
		checkCreateOwnerKey(application, who);

		return application;
	}

	public Application update(Application application, String who) {
		String method = "update";

		// logical checks
		if (application == null) {
			logInfo(method, "application is null");
			throw new AcsLogicalException("application is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		if (StringUtils.isEmpty(application.getZoneId())) {
			logInfo(method, "zoneId is empty");
			throw new AcsLogicalException("zoneId is empty");
		}

		// generate code if one is not provided
		if (StringUtils.isEmpty(application.getCode()))
			application.setCode(generateCode());

		Application result = applicationRepository.doSave(application, who);

		// write audit log
		// TODO check property changes
		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Application.UPDATE_APPLICATION)
				.applicationId(application.getId()).objectId(application.getId()).by(who)
				.parameter("name", application.getName()));

		// clear cache
		Application cachedApplication = getCoreCacheService().findApplicationById(application.getId());
		if (cachedApplication != null) {
			getCoreCacheService().clearApplication(cachedApplication);
		}

		return result;
	}

	public Application populate(Application application) {
		if (application != null) {

			if (application.getRefCompany() == null && !StringUtils.isEmpty(application.getCompanyId()))
				application.setRefCompany(getCoreCacheService().findCompanyById(application.getCompanyId()));

			if (application.getRefSubscription() == null && !StringUtils.isEmpty(application.getSubscriptionId()))
				application.setRefSubscription(
						getCoreCacheService().findSubscriptionById(application.getSubscriptionId()));

			if (application.getRefApplicationEngine() == null
					&& !StringUtils.isEmpty(application.getApplicationEngineId()))
				application.setRefApplicationEngine(
						getCoreCacheService().findApplicationEngineById(application.getApplicationEngineId()));

			if (application.getRefZone() == null && !StringUtils.isEmpty(application.getZoneId()))
				application.setRefZone(getCoreCacheService().findZoneById(application.getZoneId()));

			if (application.getRefProduct() == null && !StringUtils.isEmpty(application.getProductId()))
				application.setRefProduct(getCoreCacheService().findProductById(application.getProductId()));
		}
		return application;
	}

	public String generateCode() {
		String code = "";
		do {
			String letters = new RandomStringGenerator.Builder().withinRange('A', 'Z')
					.filteredBy(CharacterPredicates.ASCII_UPPERCASE_LETTERS).build().generate(3);
			String digits = new RandomStringGenerator.Builder().withinRange('0', '9')
					.filteredBy(CharacterPredicates.DIGITS).build().generate(3);
			// code = RandomStringUtils.randomAlphabetic(3).toUpperCase() +
			// RandomStringUtils.randomNumeric(3);
			code = letters + digits;
		} while (getApplicationRepository().findByCode(code) != null);
		return code;
	}

	public void checkCreateOwnerKey(Application application, String who) {
		String method = "checkCreateOwnerKey";
		if (accessKeyService.findOwnerKey(application.getPri()) != null) {
			logInfo(method, "Owner Key exists for application: %s", application.getName());
		} else {
			accessKeyService.createOwnerKey(application.getCompanyId(), application.getSubscriptionId(),
					application.getId(), "ApplicationOwnerKey", application.getPri(), who);
		}
	}

	public Long deleteApplication(String applicationId, String who) {
		String method = "deleteApplication";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");

		long result = 0;

		// delete accessKeys
		try {
			logInfo(method, "deleting accessKeys for applicationId: %s", applicationId);
			long count = accessKeyService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d accessKeys", count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting accessKeys", t);
		}

		// delete roles
		try {
			logInfo(method, "deleting roles for applicationId: %s", applicationId);
			long count = roleService.deleteByApplicationId(applicationId, who);
			logInfo(method, "deleted %d roles", count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting roles", t);
		}

		Application application = applicationRepository.findById(applicationId).orElse(null);
		if (application != null) {
			// delete application
			try {
				logInfo(method, "deleting applicationId: %s", applicationId);
				applicationRepository.deleteById(applicationId);
				logInfo(method, "applicationId deleted: %s", applicationId);
				result++;
			} catch (Throwable t) {
				logError(method, "error deleting application", t);
			}
			// auditLog
			getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Application.DELETE_APPLICATION)
					.productName(ProductSystemNames.PEGASUS).applicationId(applicationId).objectId(applicationId)
					.by(who).parameter("name", application.getName()).parameter("result", String.valueOf(result)));
			// clear application cache
			getCoreCacheService().clearApplication(application);
		} else {
			logError(method, "application not found: %s", applicationId);
			return result;
		}

		logInfo(method, "result: %d", result);
		return result;
	}
}
