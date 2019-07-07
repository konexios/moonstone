package com.arrow.rhea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.repo.DeviceManufacturerRepository;

@Service
public class DeviceManufacturerService extends RheaServiceAbstract {

	@Autowired
	private DeviceManufacturerRepository deviceManufacturerRepository;

	public DeviceManufacturerRepository getDeviceManufacturerRepository() {
		return deviceManufacturerRepository;
	}

	public DeviceManufacturer create(DeviceManufacturer deviceManufacturer, String who) {
		Assert.notNull(deviceManufacturer, "deviceManufacturer is null");

		String method = "create";
		logInfo(method, "...");

		// persist
		deviceManufacturer = deviceManufacturerRepository.doInsert(deviceManufacturer, who);

		getAuditLogService()
		        .save(AuditLogBuilder.create().type(RheaAuditLog.DeviceManufacturer.CreateDeviceManufacturer)
		                .productName(ProductSystemNames.RHEA).objectId(deviceManufacturer.getId()).by(who));

		return deviceManufacturer;
	}

	public DeviceManufacturer update(DeviceManufacturer deviceManufacturer, String who) {
		Assert.notNull(deviceManufacturer, "deviceManufacturer is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		deviceManufacturer = deviceManufacturerRepository.doSave(deviceManufacturer, who);

		getAuditLogService()
		        .save(AuditLogBuilder.create().type(RheaAuditLog.DeviceManufacturer.UpdateDeviceManufacturer)
		                .productName(ProductSystemNames.RHEA).objectId(deviceManufacturer.getId()).by(who));

		getRheaCacheService().clearDeviceManufacturer(deviceManufacturer);

		return deviceManufacturer;
	}
}