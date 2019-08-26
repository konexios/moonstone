package com.arrow.kronos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.IbmDevice;
import com.arrow.kronos.repo.IbmDeviceRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class IbmDeviceService extends KronosServiceAbstract {

	@Autowired
	private IbmDeviceRepository ibmDeviceRepository;

	public IbmDeviceRepository getIbmDeviceRepository() {
		return ibmDeviceRepository;
	}

	public IbmDevice findBy(Device device) {
		List<IbmDevice> existing = ibmDeviceRepository.findAllByDeviceIdAndEnabled(device.getId(), true);
		if (existing.size() > 1) {
			throw new AcsLogicalException("more than one active IbmDevice is linked to this device");
		} else if (existing.size() == 1) {
			return existing.get(0);
		} else {
			return null;
		}
	}

	public void deleteBy(Device device) {
		String method = "deleteBy";
		Assert.notNull(device, "device is null");
		Long numDeleted = ibmDeviceRepository.deleteByDeviceId(device.getId());
		logInfo(method,
		        "IBM Device objects have been deleted for device id=" + device.getId() + ", total " + numDeleted);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return ibmDeviceRepository.deleteByApplicationId(applicationId);
	}
}
