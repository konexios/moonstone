package com.arrow.rhea.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.repo.DeviceTypeRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class DeviceTypeService extends RheaServiceAbstract {

	@Autowired
	private DeviceTypeRepository deviceTypeRepository;

	public DeviceTypeRepository getDeviceTypeRepository() {
		return deviceTypeRepository;
	}

	public DeviceType create(DeviceType deviceType, String who) {
		String method = "create";

		// logical checks
		if (deviceType == null) {
			logInfo(method, "deviceType is null");
			throw new AcsLogicalException("deviceType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		deviceType = deviceTypeRepository.doInsert(deviceType, who);
		deviceType = populateRefs(deviceType);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.DeviceType.CreateDeviceType)
				.productName(ProductSystemNames.RHEA).objectId(deviceType.getId()).by(who)
		        .parameter("name", deviceType.getName()));

		return deviceType;
	}

	public DeviceType update(DeviceType deviceType, String who) {
		String method = "update";

		// logical checks
		if (deviceType == null) {
			logInfo(method, "deviceType is null");
			throw new AcsLogicalException("deviceType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		deviceType = deviceTypeRepository.doSave(deviceType, who);
		deviceType = populateRefs(deviceType);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.DeviceType.UpdateDeviceType)
				.productName(ProductSystemNames.RHEA).objectId(deviceType.getId()).by(who)
		        .parameter("name", deviceType.getName()));

		// clear cache
		getRheaCacheService().clearDeviceType(deviceType);

		return deviceType;
	}

	public DeviceType populateRefs(DeviceType deviceType) {
		if (deviceType != null) {
			if (deviceType.getRefCompany() == null && !StringUtils.isEmpty(deviceType.getCompanyId())) {
				deviceType.setRefCompany(getCoreCacheService().findCompanyById(deviceType.getCompanyId()));
			}

			if (deviceType.getRefDeviceProduct() == null && !StringUtils.isEmpty(deviceType.getDeviceProductId())) {
				deviceType.setRefDeviceProduct(
				        getRheaCacheService().findDeviceProductById(deviceType.getDeviceProductId()));
			}
		}

		return deviceType;
	}
}