package com.arrow.kronos.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceState;
import com.arrow.kronos.repo.DeviceStateRepository;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

import moonstone.acs.AcsLogicalException;

@Service
public class DeviceStateService extends KronosServiceAbstract {

	@Autowired
	private DeviceStateRepository deviceStateRepository;

	public DeviceStateRepository getDeviceStateRepository() {
		return deviceStateRepository;
	}

	public DeviceState create(DeviceState deviceState, String who) {
		String method = "create";

		// logical checks
		if (deviceState == null) {
			logInfo(method, "deviceState is null");
			throw new AcsLogicalException("deviceState is null");
		}

		if (StringUtils.isEmpty(deviceState.getDeviceId())) {
			logInfo(method, "deviceId is empty");
			throw new AcsLogicalException("deviceId is empty");
		}

		if (StringUtils.isEmpty(deviceState.getApplicationId())) {
			logInfo(method, "applicationId is empty");
			throw new AcsLogicalException("applicationId is empty");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		deviceState = deviceStateRepository.doInsert(deviceState, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceState.CreateDeviceState)
				.applicationId(deviceState.getApplicationId()).objectId(deviceState.getId())
				.parameter("deviceId", deviceState.getDeviceId()).by(who));

		return deviceState;
	}

	public DeviceState update(DeviceState deviceState, String who) {
		String method = "update";

		// logical checks
		if (deviceState == null) {
			logInfo(method, "deviceState is null");
			throw new AcsLogicalException("deviceState is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		deviceState = deviceStateRepository.doSave(deviceState, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceState.UpdateDeviceState)
				.applicationId(deviceState.getApplicationId()).objectId(deviceState.getId())
				.parameter("deviceId", deviceState.getDeviceId()).by(who));

		// clear cache
		getKronosCache().clearDeviceState(deviceState);

		return deviceState;
	}

	public void deleteBy(Device device, String who) {
		String method = "deleteBy";

		// logical checks
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		List<DeviceState> states = deviceStateRepository.deleteByApplicationIdAndDeviceId(device.getApplicationId(),
				device.getId());

		logInfo(method, "Device states have been deleted for device id=" + device.getId() + ", total " + states.size());

		for (DeviceState state : states) {
			// write audit log
			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceState.DeleteDeviceState)
					.applicationId(state.getApplicationId()).objectId(state.getId())
					.parameter("deviceId", state.getDeviceId()).by(who));

			// clear cache
			getKronosCache().clearDeviceState(state);
		}
	}

	public DeviceState populateRefs(DeviceState deviceState) {

		if (deviceState == null)
			return deviceState;

		Assert.hasText(deviceState.getDeviceId(), "deviceId is empty");
		Assert.hasText(deviceState.getApplicationId(), "applicationId is empty");

		Device device = getKronosCache().findDeviceById(deviceState.getDeviceId());
		Assert.notNull(device, "device is null");
		Assert.isTrue(StringUtils.equals(device.getApplicationId(), deviceState.getApplicationId()),
				"Device applicationId do not match DeviceState applicationId");

		deviceState.setRefDevice(device);

		return deviceState;
	}

	public void moveBy(Device device, Application toApplication, String who) {
		String method = "moveBy";

		// logical checks
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		if (toApplication == null) {
			logInfo(method, "toApplication is null");
			throw new AcsLogicalException("toApplication is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		List<DeviceState> states = deviceStateRepository.findAllByApplicationIdAndDeviceId(device.getApplicationId(),
				device.getId());
		for (DeviceState state : states) {
			String fromApplicationId = state.getApplicationId();
			state.setApplicationId(toApplication.getId());
			state = this.update(state, who);

			// write audit log
			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceState.MoveDeviceStateOut)
					.applicationId(fromApplicationId).objectId(state.getId()).parameter("deviceId", state.getDeviceId())
					.parameter("toApplicationId", toApplication.getId()).by(who));
			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceState.MoveDeviceStateIn)
					.applicationId(state.getApplicationId()).objectId(state.getId())
					.parameter("deviceId", state.getDeviceId()).parameter("fromApplicationId", fromApplicationId)
					.by(who));
		}
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return deviceStateRepository.deleteByApplicationId(applicationId);
	}
}
