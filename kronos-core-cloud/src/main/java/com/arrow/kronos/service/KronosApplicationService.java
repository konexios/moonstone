package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.repo.KronosApplicationRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class KronosApplicationService extends KronosServiceAbstract {

	@Autowired
	private KronosApplicationRepository kronosApplicationRepository;

	public KronosApplicationRepository getKronosApplicationRepository() {
		return kronosApplicationRepository;
	}

	public KronosApplication create(KronosApplication kronosApplication, String who) {
		Assert.notNull(kronosApplication, "kronosApplication is null");
		Assert.hasText(kronosApplication.getApplicationId(), "applicationId is empty");

		String method = "create";
		logInfo(method, "...");

		// persist
		kronosApplication = kronosApplicationRepository.doInsert(kronosApplication, who);
		kronosApplication = populateRefs(kronosApplication);

		Assert.notNull(kronosApplication.getRefApplication(), "kronosApplication.refApplication not populated");
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Application.CreateApplication)
		        .applicationId(kronosApplication.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(kronosApplication.getId()).by(who)
		        .parameter("iotProvider", kronosApplication.getIotProvider().getName()));

		return kronosApplication;
	}

	public KronosApplication update(KronosApplication kronosApplication, String who) {
		Assert.notNull(kronosApplication, "kronosApplication is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		kronosApplication = kronosApplicationRepository.doSave(kronosApplication, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Application.UpdateApplication)
		        .applicationId(kronosApplication.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(kronosApplication.getId()).by(who)
		        .parameter("iotProvider", kronosApplication.getIotProvider().getName()));

		// clear cache
		getKronosCache().clearKronosApplication(kronosApplication);

		return kronosApplication;
	}

	public KronosApplication populateRefs(KronosApplication kronosApplication) {
		if (kronosApplication != null) {
			if (kronosApplication.getRefApplication() == null
			        && !StringUtils.isEmpty(kronosApplication.getApplicationId()))
				kronosApplication.setRefApplication(
				        getCoreCacheService().findApplicationById(kronosApplication.getApplicationId()));
		}
		return kronosApplication;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return kronosApplicationRepository.deleteByApplicationId(applicationId);
	}
}
