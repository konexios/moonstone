package com.arrow.kronos.service;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosCloudConstants.Ibm;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.data.IbmDevice;
import com.arrow.kronos.data.IbmGateway;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.GatewayType;
import com.arrow.pegasus.data.profile.Application;
import com.google.gson.JsonObject;
import com.ibm.iotf.client.IoTFCReSTException;
import com.ibm.iotf.client.app.ApplicationClient;

import moonstone.acs.AcsLogicalException;

@Service
public class IbmIntegrationService extends KronosServiceAbstract {

	@Autowired
	private IbmAccountService accountService;
	@Autowired
	private IbmGatewayService ibmGatewayService;
	@Autowired
	private IbmDeviceService ibmDeviceService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayService gatewayService;

	public IbmDevice checkAndCreateDevice(Device device) {
		String method = "checkAndCreateDevice";
		IbmDevice result = ibmDeviceService.findBy(device);
		IbmAccount account = accountService.findActiveAccount(device);
		if (account != null) {
			if (result != null && !StringUtils.equals(account.getId(), result.getIbmAccountId())) {
				// there is enabled IBM Device but it's account id do not match
				// the active account - disable this IBM Device
				result.setEnabled(false);
				ibmDeviceService.getIbmDeviceRepository().doSave(result, null);
				result = null;
			}
			if (result == null) {
				// see if there is IBM Device with the active account
				result = ibmDeviceService.getIbmDeviceRepository().findByDeviceIdAndIbmAccountId(device.getId(),
						account.getId());
				if (result != null) {
					// set it to enabled
					result.setEnabled(true);
					ibmDeviceService.getIbmDeviceRepository().doSave(result, null);
				}
			}
			if (result == null) {
				Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
				Assert.notNull(gateway, "gateway not found: " + device.getGatewayId());

				DeviceType type = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
				Assert.notNull(type, "invalid device type: " + device.getDeviceTypeId());
				createDeviceType(account, type.getName());

				ApplicationClient client = createClient(account);
				try {
					client.api().getDevice(type.getName(), device.getUid());
					logError(method, "device already exists in IBM");
				} catch (Exception e) {
					logInfo(method, e.getMessage());
					try {
						JsonObject json = new JsonObject();
						json.addProperty("deviceId", device.getUid());

						JsonObject metadata = new JsonObject();
						metadata.addProperty("id", device.getId());
						metadata.addProperty("name", device.getName());
						metadata.addProperty("hid", device.getHid());
						json.add("metadata", metadata);

						JsonObject response = client.api().registerDeviceUnderGateway(type.getName(), gateway.getUid(),
								buildGatewayDeviceType(gateway.getType()), json);
						logInfo(method, "created new device in IBM Watson IoT: %s",
								response.get("clientId").getAsString());
						String authToken = response.get("authToken").getAsString();

						result = new IbmDevice();
						result.setDeviceId(device.getId());
						result.setApplicationId(device.getApplicationId());
						result.setAuthToken(getCryptoService().encrypt(device.getApplicationId(), authToken));
						result.setIbmAccountId(account.getId());
						ibmDeviceService.getIbmDeviceRepository().doInsert(result, null);
						logInfo(method, "created local IBM device: %s", result.getId());
					} catch (IoTFCReSTException ex) {
						logError(method, ex.getResponse().toString());
						throw new AcsLogicalException("unable to create device", ex);
					} catch (Exception ex) {
						throw new AcsLogicalException("unable to create device", ex);
					}
				}
			}
		} else {
			logError(method, "no active IBM account found");
		}
		// update device with externalId
		if (result != null && StringUtils.isEmpty(device.getExternalId())) {
			device.setExternalId(device.getUid());
			deviceService.update(device, CoreConstant.ADMIN_USER);
			logInfo(method, "updated device with externalId: %s", device.getUid());
		}
		return result;
	}

	public IbmGateway checkAndCreateGateway(Gateway gateway) {
		String method = "checkAndCreateGateway";
		IbmGateway result = ibmGatewayService.findBy(gateway);
		IbmAccount account = accountService.findActiveAccount(gateway);
		if (account != null) {
			if (result != null && !StringUtils.equals(account.getId(), result.getIbmAccountId())) {
				// there is enabled IBM Gateway but it's account id do not match
				// the active account - disable this IBM Gateway
				result.setEnabled(false);
				ibmGatewayService.getIbmGatewayRepository().doSave(result, null);
				result = null;
			}
			if (result == null) {
				// see if there is IBM Gateway with the active account
				result = ibmGatewayService.getIbmGatewayRepository().findByGatewayIdAndIbmAccountId(gateway.getId(),
						account.getId());
				if (result != null) {
					// set it to enabled
					result.setEnabled(true);
					ibmGatewayService.getIbmGatewayRepository().doSave(result, null);
				}
			}
			if (result == null) {
				createGatewayDeviceType(account, gateway.getType());
				String type = buildGatewayDeviceType(gateway.getType());
				ApplicationClient client = createClient(account);
				try {
					client.api().getDevice(type, gateway.getUid());
					logError(method, "gateway already exists in IBM");
				} catch (Exception e) {
					try {
						JsonObject metadata = new JsonObject();
						metadata.addProperty("id", gateway.getId());
						metadata.addProperty("name", gateway.getName());
						metadata.addProperty("hid", gateway.getHid());

						JsonObject response = client.api().registerDevice(type, gateway.getUid(), null, null, null,
								metadata);
						logInfo(method, "created new gateway in IBM Watson IoT: %s",
								response.get("clientId").getAsString());
						String authToken = response.get("authToken").getAsString();

						result = new IbmGateway();
						result.setAuthToken(getCryptoService().encrypt(gateway.getApplicationId(), authToken));
						result.setGatewayId(gateway.getId());
						result.setApplicationId(gateway.getApplicationId());
						result.setIbmAccountId(account.getId());
						ibmGatewayService.getIbmGatewayRepository().doInsert(result, null);
						logInfo(method, "created local IBM gateway: %s", result.getId());
					} catch (IoTFCReSTException ex) {
						logError(method, ex.getResponse().toString());
						throw new AcsLogicalException("unable to create gateway", ex);
					} catch (Exception ex) {
						throw new AcsLogicalException("unable to create gateway", ex);
					}
				}
			}
		} else {
			logError(method, "no active IBM account found");
		}
		// update gateway with externalId
		if (result != null && StringUtils.isEmpty(gateway.getExternalId())) {
			gateway.setExternalId(gateway.getUid());
			gatewayService.update(gateway, CoreConstant.ADMIN_USER);
			logInfo(method, "updated gateway with externalId: %s", gateway.getUid());
		}
		return result;
	}

	public void createGatewayDeviceType(IbmAccount account, GatewayType type) {
		String method = "createGatewayDeviceType";
		checkEnabled(account, "account");

		if (!account.isEnabled()) {
			throw new AcsLogicalException("ibm account is disabled");
		}
		ApplicationClient client = createClient(account);
		try {
			client.api().getDeviceType(buildGatewayDeviceType(type));
			logInfo(method, "gateway device type already exists in IBM Watson IoT!");
		} catch (Exception e) {
			logInfo(method, e.getMessage());
			try {
				JsonObject deviceType = new JsonObject();
				deviceType.addProperty("id", buildGatewayDeviceType(type));
				client.api().addGatewayDeviceType(deviceType);
				logInfo(method, "added new gateway device type: %s", type);
			} catch (IoTFCReSTException ex) {
				logError(method, ex.getResponse().toString());
				throw new AcsLogicalException("unable to create gateway", ex);
			} catch (Exception ex) {
				throw new AcsLogicalException("unable to create gateway device type: " + type, ex);
			}
		}
	}

	public void createDeviceType(IbmAccount account, String type) {
		String method = "createDeviceType";
		checkEnabled(account, "account");
		Assert.hasText(type, "type is empty");

		if (!account.isEnabled()) {
			throw new AcsLogicalException("ibm account is disabled");
		}
		ApplicationClient client = createClient(account);
		try {
			client.api().getDeviceType(type);
			logInfo(method, "device type already exists in IBM Watson IoT!");
		} catch (Exception e) {
			logInfo(method, e.getMessage());
			try {
				JsonObject deviceType = new JsonObject();
				deviceType.addProperty("id", type);
				client.api().addDeviceType(deviceType);
				logInfo(method, "added new device type: %s", type);
			} catch (IoTFCReSTException ex) {
				logError(method, ex.getResponse().toString());
				throw new AcsLogicalException("unable to create gateway", ex);
			} catch (Exception ex) {
				throw new AcsLogicalException("unable to create device type: " + type, ex);
			}
		}
	}

	public String buildGatewayDeviceType(GatewayType type) {
		return "gateway-" + type.name().toLowerCase();
	}

	private ApplicationClient createClient(IbmAccount account) {
		try {
			Application application = getCoreCacheService().findApplicationById(account.getApplicationId());
			Properties props = new Properties();
			props.setProperty(Ibm.HEADER_ORGANIZATION_ID, account.getOrganizationId());
			props.setProperty(Ibm.HEADER_CLIENT_ID, account.getOrganizationId());
			props.setProperty(Ibm.HEADER_AUTHENTICATION_MODE, Ibm.HEADER_AUTHENTICATION_MODE_API_KEY);
			props.setProperty(Ibm.HEADER_API_KEY, getCryptoService().decrypt(application.getId(), account.getApiKey()));
			props.setProperty(Ibm.HEADER_AUTHENTICATION_TOKEN,
					getCryptoService().decrypt(application.getId(), account.getAuthToken()));
			return new ApplicationClient(props);
		} catch (Exception e) {
			throw new AcsLogicalException("error setting up connection to IBM Watson", e);
		}
	}
}