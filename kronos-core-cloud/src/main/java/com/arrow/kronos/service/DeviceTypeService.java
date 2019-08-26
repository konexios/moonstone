package com.arrow.kronos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosCloudConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.repo.DeviceRepository;
import com.arrow.kronos.repo.DeviceTypeRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acs.AcsLogicalException;

@Service
public class DeviceTypeService extends KronosServiceAbstract {

	@Autowired
	private DeviceTypeRepository deviceTypeRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;

	public DeviceTypeRepository getDeviceTypeRepository() {
		return deviceTypeRepository;
	}

	public DeviceType checkCreateDefaultGatewayType(String applicationId, String who) {
		String method = "checkCreateDefaultGatewayType";

		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application is invalid or not enabled");
		Assert.hasText(who, "who is empty");

		DeviceType deviceType = getKronosCache().findDeviceTypeByName(applicationId,
				KronosCloudConstants.DeviceType.DEFAULT_GATEWAY_TYPE);

		if (deviceType == null) {
			deviceType = new DeviceType();
			deviceType.setApplicationId(applicationId);
			deviceType.setName(KronosCloudConstants.DeviceType.DEFAULT_GATEWAY_TYPE);
			deviceType.setDescription(deviceType.getName());
			deviceType.setDeviceCategory(AcnDeviceCategory.GATEWAY);
			deviceType.setEditable(false);
			deviceType.setEnabled(true);
			return create(deviceType, who);
		} else if (deviceType.isEnabled()) {
			logInfo(method, "device type already exists");
			return deviceType;
		} else {
			throw new AcsLogicalException("found existing DISABLED device type");
		}
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

		if (deviceType.getDeviceCategory() == null) {
			logInfo(method, "deviceCategory is null");
			throw new AcsLogicalException("deviceCategory is null");
		}

		// insert
		deviceType = deviceTypeRepository.doInsert(deviceType, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceType.CreateDeviceType)
				.applicationId(deviceType.getApplicationId()).objectId(deviceType.getId()).by(who)
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

		if (deviceType.getDeviceCategory() == null) {
			logInfo(method, "deviceCategory is null");
			throw new AcsLogicalException("deviceCategory is null");
		}

		// update
		deviceType = deviceTypeRepository.doSave(deviceType, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceType.UpdateDeviceType)
				.applicationId(deviceType.getApplicationId()).objectId(deviceType.getId()).by(who)
				.parameter("name", deviceType.getName()));

		// clear cache
		DeviceType cachedDeviceType = getKronosCache().findDeviceTypeById(deviceType.getId());
		if (cachedDeviceType != null) {
			getKronosCache().clearDeviceType(cachedDeviceType);
		}

		return deviceType;
	}

	public DeviceType synchronizeTelemetryProperties(String deviceId, String who) {
		return synchronizeTelemetryProperties(deviceId, who, true);
	}

	public DeviceType synchronizeTelemetryProperties(String deviceId, String who, boolean checkEditable) {
		String method = "getLastTelemetryItem";

		// logical check
		if (deviceId == null || deviceId.isEmpty()) {
			logInfo(method, "deviceId is null or empty");
			throw new AcsLogicalException("deviceId is null or empty");
		}

		Device device = deviceRepository.findById(deviceId).orElse(null);

		// logical check
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		DeviceType deviceType = deviceTypeRepository.findById(device.getDeviceTypeId()).orElse(null);

		// logical check
		if (deviceType == null) {
			logInfo(method, "deviceType is null");
			throw new AcsLogicalException("deviceType is null");
		} else {
			if (!device.getApplicationId().equals(deviceType.getApplicationId())) {
				logInfo(method, "deviceType and device must have the same application id");
				throw new AcsLogicalException("deviceType and device must have the same application id");
			}
			if (checkEditable && !deviceType.isEditable()) {
				logInfo(method, "deviceType is not editable");
				throw new AcsLogicalException("deviceType is not editable");
			}
		}

		Map<String, DeviceTelemetry> deviceTypeTelemetry = new HashMap<>();
		deviceType.getTelemetries().stream().forEach(p -> {
			deviceTypeTelemetry.put(p.getName(), p);
		});

		List<LastTelemetryItem> lastTelemetryItems = lastTelemetryItemService.getLastTelemetryItemRepository()
				.findByDeviceId(device.getId());

		lastTelemetryItems.stream().forEach(telemetry -> {
			DeviceTelemetry telemetryItem;
			if (!deviceTypeTelemetry.containsKey(telemetry.getName())) {
				telemetryItem = new DeviceTelemetry();
				telemetryItem.setName(telemetry.getName());
				telemetryItem.setDescription(telemetry.getName());
				telemetryItem.setType(telemetry.getType());
			} else {
				telemetryItem = deviceTypeTelemetry.get(telemetry.getName());
				if (telemetryItem.getDescription().isEmpty()) {
					telemetryItem.setDescription(telemetry.getName());
				}
				if (!telemetryItem.getType().name().equals(telemetry.getType().name())) {
					telemetryItem.setType(telemetry.getType());
				}
			}
			if (telemetryItem != null) {
				deviceTypeTelemetry.put(telemetryItem.getName(), telemetryItem);
			}
		});

		List<DeviceTelemetry> deviceTypeTelemetryProperty = new ArrayList<>(deviceTypeTelemetry.values());
		deviceType.setTelemetries(deviceTypeTelemetryProperty);

		deviceType = update(deviceType, who);

		return deviceType;
	}

	public DeviceType createDeviceAction(String deviceTypeId, DeviceAction deviceAction, String who) {
		Assert.hasText(deviceTypeId, "deviceTypeId is null");
		Assert.notNull(deviceAction, "deviceAction is null");
		Assert.notNull(deviceAction.getDeviceActionTypeId(), "deviceActionTypeId is null");
		Assert.hasText(who, "who is empty");

		DeviceType deviceType = deviceTypeRepository.findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "deviceType not found: " + deviceTypeId);
		deviceType.getActions().add(deviceAction);

		deviceType = update(deviceType, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.CreateDeviceAction)
				.applicationId(deviceType.getApplicationId()).productName(ProductSystemNames.KRONOS)
				.objectId(deviceType.getId()).by(who)
				.parameter("deviceActionIndex", String.valueOf(deviceType.getActions().size() - 1))
				.parameter("deviceActionTypeId", deviceAction.getDeviceActionTypeId()));

		return deviceType;
	}

	public DeviceType updateDeviceAction(String deviceTypeId, int index, DeviceAction deviceAction, String who) {
		Assert.hasText(deviceTypeId, "deviceTypeId is null");
		Assert.notNull(deviceAction, "deviceAction is null");
		Assert.notNull(deviceAction.getDeviceActionTypeId(), "deviceActionTypeId is null");
		Assert.hasText(who, "who is empty");

		DeviceType deviceType = deviceTypeRepository.findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "deviceType not found: " + deviceTypeId);
		Assert.isTrue(index >= 0 && index < deviceType.getActions().size(), "deviceActionIndex is out of bounds");
		deviceType.getActions().set(index, deviceAction);

		deviceType = update(deviceType, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.UpdateDeviceAction)
				.applicationId(deviceType.getApplicationId()).productName(ProductSystemNames.KRONOS)
				.objectId(deviceType.getId()).by(who).parameter("deviceActionIndex", String.valueOf(index))
				.parameter("deviceActionTypeId", deviceAction.getDeviceActionTypeId()));

		return deviceType;
	}

	public DeviceType deleteDeviceActions(String deviceTypeId, List<Integer> actionIndices, String who) {
		Assert.hasText(deviceTypeId, "deviceTypeId is null");
		Assert.hasText(who, "who is empty");

		DeviceType deviceType = deviceTypeRepository.findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "deviceType not found: " + deviceTypeId);
		List<DeviceAction> actions = deviceType.getActions();
		List<DeviceAction> deletedActions = new ArrayList<>(actionIndices.size());
		for (int i = 0; i < actions.size(); i++) {
			if (actionIndices.contains(i)) {
				deletedActions.add(actions.get(i));
			}
		}
		actions.removeAll(deletedActions);

		deviceType = update(deviceType, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.DeleteDeviceActions)
				.applicationId(deviceType.getApplicationId()).productName(ProductSystemNames.KRONOS)
				.objectId(deviceType.getId()).by(who)
				.parameter("deletedActionsCount", String.valueOf(deletedActions.size())));

		return deviceType;
	}

	public DeviceType enable(String deviceTypeId, String who) {
		return updateEnable(deviceTypeId, who, true);
	}

	public DeviceType disable(String deviceTypeId, String who) {
		return updateEnable(deviceTypeId, who, false);
	}

	private DeviceType updateEnable(String deviceTypeId, String who, boolean enabled) {
		Assert.hasLength(deviceTypeId, "deviceTypeId is empty");
		Assert.hasLength(who, "who is empty");

		// String auditLogType = KronosAuditLog.Device.EnableDevice;

		String auditLogType = KronosAuditLog.DeviceType.EnableDeviceType;

		if (!enabled)
			auditLogType = KronosAuditLog.Device.DisableDevice;

		DeviceType deviceType = deviceTypeRepository.findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "deviceType not found: " + deviceTypeId);

		Application application = getCoreCacheService().findApplicationById(deviceType.getApplicationId());
		Assert.notNull(application, "application not found: " + deviceType.getApplicationId());

		// change enabled
		deviceType.setEnabled(enabled);

		// save
		deviceType = update(deviceType, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(auditLogType)
				.applicationId(deviceType.getApplicationId()).productName(ProductSystemNames.KRONOS)
				.objectId(deviceType.getId()).by(who).parameter("applicationId", deviceType.getApplicationId()));

		return deviceType;
	}

	public DeviceType clone(DeviceType fromDeviceType, Application application,
			Map<String, DeviceActionType> mappedDeviceActionTypes, String who) {
		Assert.notNull(fromDeviceType, "fromDeviceType is null");
		Assert.notNull(application, "application is null");
		Assert.notNull(mappedDeviceActionTypes, "mappedDeviceActionTypes is null");
		Assert.hasText(who, "who is empty");

		DeviceType deviceType = new DeviceType();
		deviceType.setApplicationId(application.getId());
		deviceType.setName(fromDeviceType.getName());
		deviceType.setDescription(fromDeviceType.getDescription());
		deviceType.setEnabled(fromDeviceType.isEnabled());
		deviceType.setDeviceCategory(fromDeviceType.getDeviceCategory());
		deviceType.setRheaDeviceTypeId(fromDeviceType.getRheaDeviceTypeId());
		deviceType.setEditable(fromDeviceType.isEditable());
		deviceType.setTelemetries(fromDeviceType.getTelemetries());
		deviceType.setStateMetadata(fromDeviceType.getStateMetadata());
		if (fromDeviceType.getActions() != null) {
			// map device type actions to target device action types
			List<DeviceAction> actions = new ArrayList<>(fromDeviceType.getActions().size());
			for (DeviceAction fromAction : fromDeviceType.getActions()) {
				if (fromAction != null && fromAction.getDeviceActionTypeId() != null) {
					// clone device action
					DeviceActionType actionType = mappedDeviceActionTypes.get(fromAction.getDeviceActionTypeId());
					Assert.notNull(actionType,
							"failed to map device action type of device type to the one in target application");
					DeviceAction action = new DeviceAction();
					action.setDeviceActionTypeId(actionType.getId());
					action.setDescription(fromAction.getDescription());
					action.setCriteria(fromAction.getCriteria());
					action.setNoTelemetry(fromAction.isNoTelemetry());
					action.setNoTelemetryTime(fromAction.getNoTelemetryTime());
					action.setExpiration(fromAction.getExpiration());
					action.setEnabled(fromAction.isEnabled());
					action.setParameters(fromAction.getParameters());
					actions.add(action);
				} else {
					actions.add(fromAction);
				}
			}
			deviceType.setActions(actions);
		}

		return this.create(deviceType, who);
	}

	public Map<String, DeviceType> findOrClone(Iterable<DeviceType> deviceTypes, Application application,
			Map<String, DeviceActionType> mappedDeviceActionTypes, String who) {
		Assert.notNull(deviceTypes, "deviceTypes is null");
		Assert.notNull(application, "application is null");
		Assert.notNull(mappedDeviceActionTypes, "mappedDeviceActionTypes is null");
		Assert.hasText(who, "who is empty");

		Map<String, DeviceType> result = new HashMap<>();
		for (DeviceType deviceType : deviceTypes) {
			if (result.containsKey(deviceType.getId()))
				continue;

			DeviceType toDeviceType = getKronosCache().findDeviceTypeByName(application.getId(), deviceType.getName());
			if (toDeviceType == null) {
				toDeviceType = this.clone(deviceType, application, mappedDeviceActionTypes, who);
			}
			result.put(deviceType.getId(), toDeviceType);
		}
		return result;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return deviceTypeRepository.deleteByApplicationId(applicationId);
	}
}
