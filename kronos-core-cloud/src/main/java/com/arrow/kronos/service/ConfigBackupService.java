package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.ConfigBackup.Type;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.ConfigBackupRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class ConfigBackupService extends KronosServiceAbstract {

	@Autowired
	private ConfigBackupRepository configBackupRepository;

	public ConfigBackupRepository getConfigBackupRepository() {
		return configBackupRepository;
	}

	public ConfigBackup create(ConfigBackup configBackup, String who) {
		String method = "create";

		if (configBackup == null) {
			logInfo(method, "configBackup is null");
			throw new AcsLogicalException("configBackup is null");
		}
		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		configBackup = configBackupRepository.doInsert(configBackup, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.ConfigBackup.CreateConfigBackup)
		        .productName(ProductSystemNames.KRONOS).objectId(configBackup.getId()).by(who)
		        .parameter("type", configBackup.getType().toString()).parameter("name", configBackup.getName())
		        .parameter("objectId", configBackup.getObjectId()));

		return configBackup;
	}

	public ConfigBackup update(ConfigBackup configBackup, String who) {
		String method = "update";

		if (configBackup == null) {
			logInfo(method, "configBackup is null");
			throw new AcsLogicalException("configBackup is null");
		}
		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		configBackup = configBackupRepository.doSave(configBackup, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.ConfigBackup.UpdateConfigBackup)
		        .productName(ProductSystemNames.KRONOS).objectId(configBackup.getId()).by(who)
		        .parameter("type", configBackup.getType().toString()).parameter("name", configBackup.getName())
		        .parameter("objectId", configBackup.getObjectId()));

		return configBackup;
	}

	public void deleteBy(Device device, String who) {
		String method = "deleteBy";

		// logical checks
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		deleteBy(Type.DEVICE, device.getId(), device.getApplicationId(), who);
	}

	public void deleteBy(Gateway gateway, String who) {
		String method = "deleteBy";

		// logical checks
		if (gateway == null) {
			logInfo(method, "gateway is null");
			throw new AcsLogicalException("gateway is null");
		}

		deleteBy(Type.GATEWAY, gateway.getId(), gateway.getApplicationId(), who);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return configBackupRepository.deleteByApplicationId(applicationId);
	}

	private void deleteBy(Type type, String objectId, String applicationId, String who) {
		String method = "deleteBy";
		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// delete
		Long numDeleted = configBackupRepository.deleteByTypeAndObjectId(type, objectId);
		logInfo(method, "Config backups have been deleted for object type=" + type.name() + ", id=" + objectId
		        + ", total " + numDeleted);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.ConfigBackup.DeleteConfigBackup)
		        .productName(ProductSystemNames.KRONOS).objectId(objectId).by(who).parameter("type", type.toString())
		        .parameter("numDeleted", "" + numDeleted));
	}
}
