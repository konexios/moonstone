package com.arrow.kronos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.AzureDevice;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.AzureDeviceRepository;

@Service
public class AzureDeviceService extends KronosServiceAbstract {

	@Autowired
	private AzureDeviceRepository azureDeviceRepository;

	public AzureDeviceRepository getAzureDeviceRepository() {
		return azureDeviceRepository;
	}

	public AzureDevice findBy(Gateway gateway) {
		List<AzureDevice> existing = azureDeviceRepository.findAllByGatewayIdAndEnabled(gateway.getId(), true);
		if (existing.size() > 1) {
			throw new AcsLogicalException("more than one active AzureDevice is linked to this gateway");
		} else if (existing.size() == 1) {
			return existing.get(0);
		} else {
			return null;
		}
	}

	public void deleteBy(Gateway gateway) {
		String method = "deleteBy";
		Assert.notNull(gateway, "gateway is null");
		Long numDeleted = azureDeviceRepository.deleteByGatewayId(gateway.getId());
		logInfo(method,
		        "AzureDevice objects have been deleted for gateway id=" + gateway.getId() + ", total " + numDeleted);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return azureDeviceRepository.deleteByApplicationId(applicationId);
	}
}
