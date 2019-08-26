package com.arrow.kronos.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceState;
import com.arrow.kronos.data.DeviceStateTrans;
import com.arrow.kronos.data.DeviceStateTrans.Status;
import com.arrow.kronos.repo.DeviceStateTransRepository;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

import moonstone.acs.AcsLogicalException;

@Service
public class DeviceStateTransService extends KronosServiceAbstract {

	@Autowired
	private DeviceStateTransRepository deviceStateTransRepository;
	@Autowired
	private DeviceStateService deviceStateService;

	public DeviceStateTransRepository getDeviceStateTransRepository() {
		return deviceStateTransRepository;
	}

	public DeviceStateTrans create(DeviceStateTrans deviceStateTrans, String who) {
		String method = "create";

		// logical checks
		if (deviceStateTrans == null) {
			logInfo(method, "deviceStateTrans is null");
			throw new AcsLogicalException("deviceStateTrans is null");
		}

		if (StringUtils.isEmpty(deviceStateTrans.getDeviceId())) {
			logInfo(method, "deviceId is empty");
			throw new AcsLogicalException("deviceId is empty");
		}

		if (StringUtils.isEmpty(deviceStateTrans.getApplicationId())) {
			logInfo(method, "applicationId is empty");
			throw new AcsLogicalException("applicationId is empty");
		}

		if (deviceStateTrans.getType() == null) {
			logInfo(method, "type is null");
			throw new AcsLogicalException("type is null");
		}

		if (deviceStateTrans.getStatus() == null) {
			logInfo(method, "status is null");
			throw new AcsLogicalException("status is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		boolean updateType = deviceStateTrans.getType() == DeviceStateTrans.Type.UPDATE;

		if (updateType) {
			// for update type, set status to COMPLETE
			deviceStateTrans.setStatus(Status.COMPLETE);
		}

		// insert
		deviceStateTrans = deviceStateTransRepository.doInsert(deviceStateTrans, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceStateTrans.CreateDeviceStateTrans)
		        .applicationId(deviceStateTrans.getApplicationId()).objectId(deviceStateTrans.getId())
		        .parameter("deviceId", deviceStateTrans.getDeviceId())
		        .parameter("type", deviceStateTrans.getType().name())
		        .parameter("status", deviceStateTrans.getStatus().name()).by(who));

		if (updateType) {
			DeviceState deviceState = deviceStateService.getDeviceStateRepository().findByApplicationIdAndDeviceId(
			        deviceStateTrans.getApplicationId(), deviceStateTrans.getDeviceId());

			if (deviceState == null) {
				deviceState = new DeviceState();
				deviceState.setApplicationId(deviceStateTrans.getApplicationId());
				deviceState.setDeviceId(deviceStateTrans.getDeviceId());
				deviceState.getStates().putAll(deviceStateTrans.getStates());
				deviceState = deviceStateService.create(deviceState, who);
			} else {
				deviceState.getStates().putAll(deviceStateTrans.getStates());
				deviceState = deviceStateService.update(deviceState, who);
			}
		}

		return deviceStateTrans;
	}

	public DeviceStateTrans update(DeviceStateTrans deviceStateTrans, String who) {
		String method = "update";

		// logical checks
		if (deviceStateTrans == null) {
			logInfo(method, "deviceStateTrans is null");
			throw new AcsLogicalException("deviceStateTrans is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		deviceStateTrans = deviceStateTransRepository.doSave(deviceStateTrans, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceStateTrans.UpdateDeviceStateTrans)
		        .applicationId(deviceStateTrans.getApplicationId()).objectId(deviceStateTrans.getId())
		        .parameter("deviceId", deviceStateTrans.getDeviceId())
		        .parameter("type", deviceStateTrans.getType().name())
		        .parameter("status", deviceStateTrans.getStatus().name()).by(who));

		return deviceStateTrans;
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

		Long numDeleted = deviceStateTransRepository.deleteByDeviceId(device.getId());

		logInfo(method, "Device state transactions have been deleted for device id=" + device.getId() + ", total "
		        + numDeleted);

		if (numDeleted > 0) {
			// write audit log
			getAuditLogService()
			        .save(AuditLogBuilder.create().type(KronosAuditLog.DeviceStateTrans.DeleteDeviceStateTrans)
			                .applicationId(device.getApplicationId()).parameter("deviceId", device.getId())
			                .parameter("numDeleted", "" + numDeleted).by(who));
		}
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

		List<DeviceStateTrans> transactions = deviceStateTransRepository.findAllByDeviceId(device.getId());
		for (DeviceStateTrans transaction : transactions) {
			String fromApplicationId = transaction.getApplicationId();
			transaction.setApplicationId(toApplication.getId());
			transaction = this.update(transaction, who);

			// write audit log
			getAuditLogService().save(AuditLogBuilder.create()
			        .type(KronosAuditLog.DeviceStateTrans.MoveDeviceStateTransOut).applicationId(fromApplicationId)
			        .objectId(transaction.getId()).parameter("deviceId", transaction.getDeviceId())
			        .parameter("toApplicationId", toApplication.getId()).by(who));
			getAuditLogService()
			        .save(AuditLogBuilder.create().type(KronosAuditLog.DeviceStateTrans.MoveDeviceStateTransIn)
			                .applicationId(transaction.getApplicationId()).objectId(transaction.getId())
			                .parameter("deviceId", transaction.getDeviceId())
			                .parameter("fromApplicationId", fromApplicationId).by(who));
		}
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return deviceStateTransRepository.deleteByApplicationId(applicationId);
	}
}
