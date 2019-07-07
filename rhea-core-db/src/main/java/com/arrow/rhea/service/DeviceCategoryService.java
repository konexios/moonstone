package com.arrow.rhea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.repo.DeviceCategoryRepository;

@Service
public class DeviceCategoryService extends RheaServiceAbstract {

	@Autowired
	private DeviceCategoryRepository deviceCategoryRepository;

	public DeviceCategoryRepository getDeviceCategoryRepository() {
		return deviceCategoryRepository;
	}

	public DeviceCategory create(DeviceCategory deviceCategory, String who) {
		Assert.notNull(deviceCategory, "deviceCategory is null");

		String method = "create";
		logInfo(method, "...");

		// persist
		deviceCategory = deviceCategoryRepository.doInsert(deviceCategory, who);
		deviceCategory = populateRefs(deviceCategory);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.DeviceCategory.CreateDeviceCategory)
		        .productName(ProductSystemNames.RHEA).objectId(deviceCategory.getId()).by(who));

		return deviceCategory;
	}

	public DeviceCategory update(DeviceCategory deviceCategory, String who) {
		Assert.notNull(deviceCategory, "deviceCategory is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		deviceCategory = deviceCategoryRepository.doSave(deviceCategory, who);
		deviceCategory = populateRefs(deviceCategory);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.DeviceCategory.UpdateDeviceCategory)
		        .productName(ProductSystemNames.RHEA).objectId(deviceCategory.getId()).by(who));

		getRheaCacheService().clearDeviceCategory(deviceCategory);

		return deviceCategory;
	}

	public DeviceCategory populateRefs(DeviceCategory deviceCategory) {
		if (deviceCategory != null) {
		}

		return deviceCategory;
	}
}