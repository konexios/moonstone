package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.repo.AuditLogRepository;

@Service
public class AuditLogService extends ServiceAbstract {

	@Autowired
	private AuditLogRepository auditLogRepository;
	@Autowired
	private CoreCacheService coreCacheService;

	public AuditLogRepository getAuditLogRepository() {
		return auditLogRepository;
	}

	public AuditLog create(AuditLogBuilder builder) {
		String method = "save";
		AuditLog auditLog = builder.build();

		if (StringUtils.isEmpty(auditLog.getProductName())) {
			if (StringUtils.isEmpty(auditLog.getApplicationId())) {
				throw new AcsLogicalException("AuditLog.productName is missing!");
			}
			Application application = coreCacheService.findApplicationById(auditLog.getApplicationId());
			Assert.notNull(application, "Application not found: " + auditLog.getApplicationId());
			Product product = coreCacheService.findProductById(application.getProductId());
			Assert.notNull(product, "Product not found: " + application.getProductId());
			auditLog.setProductName(product.getName());
		}
		auditLogRepository.doInsert(auditLog, null);
		logInfo(method, "type: %s, objectId: %s", auditLog.getType(), auditLog.getObjectId());
		return auditLog;
	}
	
	public AuditLog save(AuditLogBuilder builder) {
		String method = "save";
		AuditLog auditLog = builder.build();

		if (StringUtils.isEmpty(auditLog.getProductName())) {
			if (StringUtils.isEmpty(auditLog.getApplicationId())) {
				throw new AcsLogicalException("AuditLog.productName is missing!");
			}
			Application application = coreCacheService.findApplicationById(auditLog.getApplicationId());
			Assert.notNull(application, "Application not found: " + auditLog.getApplicationId());
			Product product = coreCacheService.findProductById(application.getProductId());
			Assert.notNull(product, "Product not found: " + application.getProductId());
			auditLog.setProductName(product.getName());
		}
		auditLogRepository.doSave(auditLog, null);
		logInfo(method, "type: %s, objectId: %s", auditLog.getType(), auditLog.getObjectId());
		return auditLog;
	}
}
