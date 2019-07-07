package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.AzureAccountModel;
import com.arrow.acn.client.model.AzureDeviceModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.AzureDevice;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.AzureDeviceSearchParams;
import com.arrow.kronos.service.AzureAccountService;
import com.arrow.kronos.service.AzureDeviceService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/kronos/integration/azure")
public class AzureIntegrationApi extends BaseApiAbstract {

	@Autowired
	private AzureAccountService azureAccountService;
	@Autowired
	private AzureDeviceService azureDeviceService;

	@ApiOperation(value = "find accounts")
	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public ListResultModel<AzureAccountModel> findAccounts(
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		List<AzureAccount> azureAccounts = null;
		if (StringUtils.isNotEmpty(enabled)) {
			azureAccounts = azureAccountService.getAzureAccountRepository()
					.findAllByApplicationIdAndEnabled(accessKey.getApplicationId(), Boolean.parseBoolean(enabled));
		} else {
			azureAccounts = azureAccountService.getAzureAccountRepository()
					.findAllByApplicationId(accessKey.getApplicationId());
		}
		List<AzureAccountModel> data = azureAccounts.stream()
				.map(azureAccount -> buildAzureAccountModel(azureAccount, accessKey.getRefApplication()))
				.collect(Collectors.toCollection(ArrayList::new));

		return new ListResultModel<AzureAccountModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "find devices")
	@RequestMapping(path = "/devices", method = RequestMethod.GET)
	public ListResultModel<AzureDeviceModel> findDevices(
			@RequestParam(name = "azureAccountHid", required = false) String azureAccountHid,
			@RequestParam(name = "gatewayHid", required = false) String gatewayHid,
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		AzureDeviceSearchParams params = new AzureDeviceSearchParams();
		String azureAccountId = null;
		if (StringUtils.isNotEmpty(azureAccountHid)) {
			AzureAccount azureAccount = getKronosCache().findAzureAccountByHid(azureAccountHid);
			Assert.notNull(azureAccount, "Azure account is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(azureAccount.getApplicationId()),
					"application does not match");
			azureAccountId = azureAccount.getId();
			params.addAzureAccountIds(azureAccountId);
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
		Assert.isTrue(azureAccountId != null || gatewayId != null, "missing search criteria");
		if (StringUtils.isNotEmpty(enabled)) {
			params.setEnabled(Boolean.parseBoolean(enabled));
		}

		List<AzureDeviceModel> data = azureDeviceService.getAzureDeviceRepository().findAzureDevices(params).stream()
				.map(azureDevice -> buildAzureDeviceModel(azureDevice))
				.collect(Collectors.toCollection(ArrayList::new));

		return new ListResultModel<AzureDeviceModel>().withData(data).withSize(data.size());
	}

	private AzureAccountModel buildAzureAccountModel(AzureAccount account, Application application) {
		Assert.notNull(account, "account is null");
		Assert.notNull(application, "application is null");
		String method = "buildAzureAccountModel";

		AzureAccountModel result = buildModel(new AzureAccountModel(), account);
		result.setAccessKey(getCryptoService().decrypt(application.getId(), account.getAccessKey()));
		result.setAccessKeyName(account.getAccessKeyName());
		result.setApplicationHid(application.getHid());
		result.setConsumerGroupName(account.getConsumerGroupName());
		result.setEnabled(account.isEnabled());
		result.setEventHubEndpoint(account.getEventHubEndpoint());
		result.setEventHubName(account.getEventHubName());
		result.setHostName(account.getHostName());
		result.setNumPartitions(account.getNumPartitions());
		if (account.getTelemetrySync() != null) {
			result.setTelemetrySync(account.getTelemetrySync().toString());
		}
		if (StringUtils.isNotEmpty(account.getUserId())) {
			User user = getCoreCacheService().findUserById(account.getUserId());
			if (user != null) {
				result.setUserHid(user.getHid());
			} else {
				logWarn(method, "user is not found, id: %s", account.getUserId());
			}
		}
		return result;
	}

	private AzureDeviceModel buildAzureDeviceModel(AzureDevice device) {
		Assert.notNull(device, "device is null");
		String method = "buildAzureDeviceModel";

		AzureDeviceModel result = buildModel(new AzureDeviceModel(), device);
		AzureAccount azureAccount = getKronosCache().findAzureAccountById(device.getAzureAccountId());
		if (azureAccount != null) {
			result.setAzureAccountHid(azureAccount.getHid());
		} else {
			logWarn(method, "Azure account is not found, id: %s", device.getAzureAccountId());
		}
		result.setEnabled(device.isEnabled());
		Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
		if (gateway != null) {
			result.setGatewayHid(gateway.getHid());
		} else {
			logWarn(method, "gateway is not found, id: %s", device.getGatewayId());
		}
		result.setPrimaryKey(getCryptoService().decrypt(gateway.getApplicationId(), device.getPrimaryKey()));
		result.setSecondaryKey(getCryptoService().decrypt(gateway.getApplicationId(), device.getSecondaryKey()));
		return result;
	}
}
