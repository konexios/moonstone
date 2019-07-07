package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.repo.TelemetryItemRepository;

@Service
public class TelemetryItemService extends KronosServiceAbstract {

	@Autowired
	private TelemetryItemRepository telemetryItemRepository;

	public TelemetryItemRepository getTelemetryItemRepository() {
		return telemetryItemRepository;
	}

	public void deleteBy(Device device) {
		String method = "deleteBy";
		Assert.notNull(device, "device is null");
		Long numDeleted = telemetryItemRepository.deleteByDeviceId(device.getId());
		logInfo(method, "Telemetry items have been deleted for device id=" + device.getId() + ", total " + numDeleted);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return telemetryItemRepository.deleteByApplicationId(applicationId);
	}
}
