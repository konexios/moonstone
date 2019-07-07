package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.UserRegistration;
import com.arrow.kronos.repo.UserRegistrationRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class UserRegistrationService extends KronosServiceAbstract {

	@Autowired
	private UserRegistrationRepository userRegistrationRepository;

	public UserRegistrationRepository getUserRegistrationRepository() {
		return userRegistrationRepository;
	}

	public UserRegistration create(UserRegistration userRegistration, String who) {
		Assert.notNull(userRegistration, "userRegistration is null");
		Assert.hasText(who, "who is empty");

		String method = "create";
		logInfo(method, "...");

		// persist
		userRegistration = userRegistrationRepository.doInsert(userRegistration, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.UserRegistration.CreateUserRegistration)
		        .productName(ProductSystemNames.KRONOS).objectId(userRegistration.getId()).by(who)
		        .parameter("email", userRegistration.getEmail()));

		return userRegistration;
	}

	public UserRegistration update(UserRegistration userRegistration, String who) {
		Assert.notNull(userRegistration, "userRegistration is null");
		Assert.hasText(who, "who is empty");

		String method = "update";
		logInfo(method, "...");

		// persist
		userRegistration = userRegistrationRepository.doSave(userRegistration, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.UserRegistration.UpdateUserRegistration)
		        .productName(ProductSystemNames.KRONOS).objectId(userRegistration.getId()).by(who)
		        .parameter("email", userRegistration.getEmail()));

		return userRegistration;
	}
}
