package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.IbmAccountModel;
import com.arrow.acn.client.model.IbmDeviceModel;
import com.arrow.acn.client.model.IbmGatewayModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.data.IbmDevice;
import com.arrow.kronos.data.IbmGateway;
import com.arrow.kronos.repo.IbmDeviceSearchParams;
import com.arrow.kronos.repo.IbmGatewaySearchParams;
import com.arrow.kronos.service.IbmAccountService;
import com.arrow.kronos.service.IbmDeviceService;
import com.arrow.kronos.service.IbmGatewayService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;

@RestController
@RequestMapping("/api/v1/kronos/integration/ibm")
public class IbmIntegrationApi extends BaseApiAbstract {

	@Autowired
	private IbmAccountService ibmAccountService;
	@Autowired
	private IbmDeviceService ibmDeviceService;
	@Autowired
	private IbmGatewayService ibmGatewayService;

	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public ListResultModel<IbmAccountModel> findAccounts(
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		List<IbmAccount> ibmAccounts = null;
		if (StringUtils.isNotEmpty(enabled)) {
			ibmAccounts = ibmAccountService.getIbmAccountRepository()
					.findAllByApplicationIdAndEnabled(accessKey.getApplicationId(), Boolean.parseBoolean(enabled));
		} else {
			ibmAccounts = ibmAccountService.getIbmAccountRepository().findByApplicationId(accessKey.getApplicationId());
		}
		List<IbmAccountModel> data = ibmAccounts.stream()
				.map(ibmAccount -> buildIbmAccountModel(ibmAccount, accessKey.getRefApplication()))
				.collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<IbmAccountModel>().withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/devices", method = RequestMethod.GET)
	public ListResultModel<IbmDeviceModel> findDevices(
			@RequestParam(name = "ibmAccountHid", required = false) String ibmAccountHid,
			@RequestParam(name = "deviceHid", required = false) String deviceHid,
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		IbmDeviceSearchParams params = new IbmDeviceSearchParams();
		String ibmAccountId = null;
		if (StringUtils.isNotEmpty(ibmAccountHid)) {
			IbmAccount ibmAccount = getKronosCache().findIbmAccountByHid(ibmAccountHid);
			Assert.notNull(ibmAccount, "ibmAccount is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(ibmAccount.getApplicationId()),
					"application does not match");
			ibmAccountId = ibmAccount.getId();
			params.addIbmAccountIds(ibmAccountId);
		}
		String deviceId = null;
		if (StringUtils.isNotEmpty(deviceHid)) {
			Device device = getKronosCache().findDeviceByHid(deviceHid);
			Assert.notNull(device, "device is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(device.getApplicationId()), "application does not match");
			deviceId = device.getId();
			params.addDeviceIds(deviceId);
		}
		Assert.isTrue(ibmAccountId != null || deviceId != null, "missing search criteria");
		if (StringUtils.isNotEmpty(enabled)) {
			params.setEnabled(Boolean.parseBoolean(enabled));
		}

		List<IbmDeviceModel> data = ibmDeviceService.getIbmDeviceRepository().findIbmDevices(params).stream()
				.map(ibmDevice -> buildIbmDeviceModel(ibmDevice, accessKey.getRefApplication()))
				.collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<IbmDeviceModel>().withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/gateways", method = RequestMethod.GET)
	public ListResultModel<IbmGatewayModel> findGateways(
			@RequestParam(name = "ibmAccountHid", required = false) String ibmAccountHid,
			@RequestParam(name = "gatewayHid", required = false) String gatewayHid,
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		IbmGatewaySearchParams params = new IbmGatewaySearchParams();
		String ibmAccountId = null;
		if (StringUtils.isNotEmpty(ibmAccountHid)) {
			IbmAccount ibmAccount = getKronosCache().findIbmAccountByHid(ibmAccountHid);
			Assert.notNull(ibmAccount, "ibmAccount is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(ibmAccount.getApplicationId()),
					"application does not match");
			ibmAccountId = ibmAccount.getId();
			params.addIbmAccountIds(ibmAccountId);
		}
		String gatewayId = null;
		if (StringUtils.isNotEmpty(gatewayHid)) {
			Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
			Assert.notNull(gateway, "gateway is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(gateway.getApplicationId()),
					"application does not match");
			gatewayId = gateway.getId();
			params.addGatewayIds(gatewayId);
		}
		Assert.isTrue(ibmAccountId != null || gatewayId != null, "missing search criteria");
		if (StringUtils.isNotEmpty(enabled)) {
			params.setEnabled(Boolean.parseBoolean(enabled));
		}

		List<IbmGatewayModel> data = ibmGatewayService.getIbmGatewayRepository().findIbmGateways(params).stream()
				.map(ibmGateway -> buildIbmGatewayModel(ibmGateway, accessKey.getRefApplication()))
				.collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<IbmGatewayModel>().withData(data).withSize(data.size());
	}

	private IbmAccountModel buildIbmAccountModel(IbmAccount ibmAccount, Application application) {
		Assert.notNull(ibmAccount, "ibmAccount is null");
		Assert.notNull(application, "application is null");

		String method = "buildIbmAccountModel";
		IbmAccountModel result = buildModel(new IbmAccountModel(), ibmAccount);
		result.setApiKey(getCryptoService().decrypt(application.getId(), ibmAccount.getApiKey()));
		result.setApplicationHid(application.getHid());
		result.setAuthToken(getCryptoService().decrypt(application.getId(), ibmAccount.getAuthToken()));
		result.setOrganizationId(ibmAccount.getOrganizationId());
		if (ibmAccount.getUserId() != null) {
			User user = getCoreCacheService().findUserById(ibmAccount.getUserId());
			if (user != null) {
				result.setUserHid(user.getHid());
			} else {
				logWarn(method, "user is not found, id: %s", ibmAccount.getUserId());
			}
		}
		result.setEnabled(ibmAccount.isEnabled());
		return result;
	}

	private IbmDeviceModel buildIbmDeviceModel(IbmDevice ibmDevice, Application application) {
		Assert.notNull(ibmDevice, "ibmDevice is null");
		Assert.notNull(application, "application is null");

		String method = "buildIbmDeviceModel";
		IbmDeviceModel result = buildModel(new IbmDeviceModel(), ibmDevice);
		result.setAuthToken(getCryptoService().decrypt(application.getId(), ibmDevice.getAuthToken()));
		result.setEnabled(ibmDevice.isEnabled());
		Device device = getKronosCache().findDeviceById(ibmDevice.getDeviceId());
		if (device != null) {
			result.setDeviceHid(device.getHid());
		} else {
			logWarn(method, "device is not found, id: %s", ibmDevice.getDeviceId());
		}
		IbmAccount ibmAccount = getKronosCache().findIbmAccountById(ibmDevice.getIbmAccountId());
		if (ibmAccount != null) {
			result.setIbmAccountHid(ibmAccount.getHid());
		} else {
			logWarn(method, "ibmAccount is not found, id: %s", ibmDevice.getIbmAccountId());
		}
		return result;
	}

	private IbmGatewayModel buildIbmGatewayModel(IbmGateway ibmGateway, Application application) {
		Assert.notNull(ibmGateway, "ibmGateway is null");
		Assert.notNull(application, "application is null");

		String method = "buildIbmGatewayModel";
		IbmGatewayModel result = buildModel(new IbmGatewayModel(), ibmGateway);
		Gateway gateway = getKronosCache().findGatewayById(ibmGateway.getGatewayId());
		if (gateway != null) {
			result.setGatewayHid(gateway.getHid());
		} else {
			logWarn(method, "gateway is not found, id: %s", ibmGateway.getGatewayId());
		}
		IbmAccount ibmAccount = getKronosCache().findIbmAccountById(ibmGateway.getIbmAccountId());
		if (ibmAccount != null) {
			result.setIbmAccountHid(ibmAccount.getHid());
		} else {
			logWarn(method, "ibmAccount is not found, id: %s", ibmGateway.getIbmAccountId());
		}
		result.setAuthToken(getCryptoService().decrypt(application.getId(), ibmGateway.getAuthToken()));
		result.setEnabled(ibmGateway.isEnabled());
		return result;
	}
}
