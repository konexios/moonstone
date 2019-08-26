package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.AzureDevice;
import com.arrow.kronos.data.Gateway;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Application;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubNotFoundException;

import moonstone.acs.AcsRuntimeException;
import moonstone.acs.AcsSystemException;

@Service
public class AzureIntegrationService extends KronosServiceAbstract {
	@Autowired
	private AzureAccountService azureAccountService;
	@Autowired
	private AzureDeviceService azureDeviceService;
	@Autowired
	private GatewayService gatewayService;

	public AzureDevice checkAndCreateAzureDevice(Gateway gateway) {
		String method = "checkAndCreateAzureDevice";

		try {
			AzureDevice result = azureDeviceService.findBy(gateway);
			if (result == null) {
				AzureAccount account = azureAccountService.findActiveAccount(gateway);
				if (account != null) {
					Application application = getCoreCacheService().findApplicationById(account.getApplicationId());
					checkEnabled(application, "application");

					RegistryManager registry = RegistryManager
							.createFromConnectionString(azureAccountService.buildIotHubConnectionString(account));

					Device remoteDevice = null;
					try {
						remoteDevice = registry.getDevice(gateway.getUid());
					} catch (IotHubNotFoundException e) {
					}
					if (remoteDevice == null) {
						remoteDevice = Device.createFromId(gateway.getUid(), null, null);
						logInfo(method, "create remoteDevice for uid: %s", gateway.getUid());
						registry.addDevice(remoteDevice);
						logInfo(method, "remoteDevice created successfully, eTag: %s", remoteDevice.geteTag());
					} else {
						logWarn(method, "remoteDevice found for uid: %s", gateway.getUid());
					}

					// create new azure device
					result = new AzureDevice();
					result.setGatewayId(gateway.getId());
					result.setApplicationId(gateway.getApplicationId());
					result.setEnabled(true);
					result.setAzureAccountId(account.getId());
					result.setPrimaryKey(getCryptoService().encrypt(application.getId(), remoteDevice.getPrimaryKey()));
					result.setSecondaryKey(getCryptoService().encrypt(application.getId(), remoteDevice.getSecondaryKey()));
					azureDeviceService.getAzureDeviceRepository().doInsert(result, null);
					logInfo(method, "created new local azure device: %s", result.getPri());

				} else {
					logInfo(method, "no active Azure account found");
				}
			}
			// update gateway with externalId
			if (result != null && StringUtils.isEmpty(gateway.getExternalId())) {
				gateway.setExternalId(gateway.getUid());
				gatewayService.update(gateway, CoreConstant.ADMIN_USER);
				logInfo(method, "updated gateway with externalId: %s", gateway.getUid());
			}
			return result;
		} catch (AcsRuntimeException e) {
			throw e;
		} catch (Throwable t) {
			throw new AcsSystemException("Unable to create remote device in Azure", t);
		}
	}
}
