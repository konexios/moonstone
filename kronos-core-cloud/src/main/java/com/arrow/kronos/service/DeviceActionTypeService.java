package com.arrow.kronos.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.repo.DeviceActionTypeRepository;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

@Service
public class DeviceActionTypeService extends KronosServiceAbstract {

	@Autowired
	private DeviceActionTypeRepository deviceActionTypeRepository;

	public DeviceActionTypeRepository getDeviceActionTypeRepository() {
		return deviceActionTypeRepository;
	}

	public DeviceActionType create(DeviceActionType deviceActionType, String who) {
		String method = "create";

		// logical checks
		if (deviceActionType == null) {
			logInfo(method, "deviceActionType is null");
			throw new AcsLogicalException("deviceActionType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		deviceActionType = deviceActionTypeRepository.doInsert(deviceActionType, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceActionType.CreateDeviceActionType)
		        .applicationId(deviceActionType.getApplicationId()).objectId(deviceActionType.getId()).by(who)
		        .parameter("systemName", deviceActionType.getSystemName())
		        .parameter("name", deviceActionType.getName()));

		return deviceActionType;
	}

	public DeviceActionType update(DeviceActionType deviceActionType, String who) {
		String method = "update";

		// logical checks
		if (deviceActionType == null) {
			logInfo(method, "deviceActionType is null");
			throw new AcsLogicalException("deviceActionType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		deviceActionType = deviceActionTypeRepository.doSave(deviceActionType, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceActionType.UpdateDeviceActionType)
		        .applicationId(deviceActionType.getApplicationId()).objectId(deviceActionType.getId()).by(who)
		        .parameter("systemName", deviceActionType.getSystemName())
		        .parameter("name", deviceActionType.getName()));

		// clear cache
		DeviceActionType cachedDeviceActionType = getKronosCache().findDeviceActionTypeById(deviceActionType.getId());
		if (cachedDeviceActionType != null) {
			getKronosCache().clearDeviceActionType(cachedDeviceActionType);
		}

		return deviceActionType;
	}

	public DeviceActionType clone(DeviceActionType fromDeviceActionType, Application application, String who) {
		Assert.notNull(fromDeviceActionType, "fromDeviceActionType is null");
		Assert.notNull(application, "application is null");
		Assert.hasText(who, "who is empty");

		DeviceActionType deviceActionType = new DeviceActionType();
		deviceActionType.setApplicationId(application.getId());
		deviceActionType.setName(fromDeviceActionType.getName());
		deviceActionType.setDescription(fromDeviceActionType.getDescription());
		deviceActionType.setEnabled(fromDeviceActionType.isEnabled());
		deviceActionType.setSystemName(fromDeviceActionType.getSystemName());
		deviceActionType.setEditable(fromDeviceActionType.isEditable());
		deviceActionType.setParameters(fromDeviceActionType.getParameters());

		return this.create(deviceActionType, who);
	}

	public Map<String, DeviceActionType> findOrClone(Iterable<DeviceActionType> deviceActionTypes,
	        Application application, String who) {
		Assert.notNull(deviceActionTypes, "deviceActionTypes is null");
		Assert.notNull(application, "application is null");
		Assert.hasText(who, "who is empty");

		Map<String, DeviceActionType> result = new HashMap<>();
		for (DeviceActionType deviceActionType : deviceActionTypes) {
			if (result.containsKey(deviceActionType.getId()))
				continue;

			DeviceActionType toDeviceActionType = getKronosCache().findDeviceActionTypeBySystemName(application.getId(),
			        deviceActionType.getSystemName());
			if (toDeviceActionType == null) {
				toDeviceActionType = this.clone(deviceActionType, application, who);
			}
			result.put(deviceActionType.getId(), toDeviceActionType);
		}
		return result;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return deviceActionTypeRepository.deleteByApplicationId(applicationId);
	}
}
