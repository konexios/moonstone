package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.KronosUser;
import com.arrow.kronos.repo.KronosUserRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class KronosUserService extends KronosServiceAbstract {

	@Autowired
	private KronosUserRepository kronosUserRepository;

	public KronosUserRepository getKronosUserRepository() {
		return kronosUserRepository;
	}

	public KronosUser create(KronosUser kronosUser, String who) {
		Assert.notNull(kronosUser, "kronosUser is null");
		Assert.hasText(kronosUser.getUserId(), "userId is empty");
		Assert.hasText(kronosUser.getApplicationId(), "applicationId is empty");

		String method = "create";
		logInfo(method, "...");

		// persist
		kronosUser = kronosUserRepository.doInsert(kronosUser, who);
		kronosUser = populateRefs(kronosUser);

		Assert.notNull(kronosUser.getRefUser(), "kronosUser.refUser not populated");
		Assert.notNull(kronosUser.getRefApplication(), "kronosUser.refApplication not populated");
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.User.CreateUser)
		        .applicationId(kronosUser.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(kronosUser.getId()).by(who).parameter("iotProvider",
		                kronosUser.getIotProvider() != null ? kronosUser.getIotProvider().getName() : "null"));

		return kronosUser;
	}

	public KronosUser update(KronosUser kronosUser, String who) {
		Assert.notNull(kronosUser, "kronosUser is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		kronosUser = kronosUserRepository.doSave(kronosUser, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.User.UpdateUser)
		        .applicationId(kronosUser.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(kronosUser.getId()).by(who).parameter("iotProvider",
		                kronosUser.getIotProvider() != null ? kronosUser.getIotProvider().getName() : "null"));

		// clear cache
		getKronosCache().clearKronosUser(kronosUser);

		return kronosUser;
	}

	public KronosUser populateRefs(KronosUser kronosUser) {
		if (kronosUser != null) {

			if (kronosUser.getRefUser() == null && !StringUtils.isEmpty(kronosUser.getUserId())) {
				kronosUser.setRefUser(getCoreCacheService().findUserById(kronosUser.getUserId()));
			}

			if (kronosUser.getRefApplication() == null && !StringUtils.isEmpty(kronosUser.getApplicationId()))
				kronosUser.setRefApplication(getCoreCacheService().findApplicationById(kronosUser.getApplicationId()));
		}
		return kronosUser;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return kronosUserRepository.deleteByApplicationId(applicationId);
	}
}
