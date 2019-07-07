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

import com.arrow.acn.client.model.AwsAccountModel;
import com.arrow.acn.client.model.AwsThingModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AwsThing;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.AwsThingSearchParams;
import com.arrow.kronos.service.AwsAccountService;
import com.arrow.kronos.service.AwsThingService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;

@RestController
@RequestMapping("/api/v1/kronos/integration/aws")
public class AwsIntegrationApi extends BaseApiAbstract {

	@Autowired
	private AwsAccountService awsAccountService;
	@Autowired
	private AwsThingService awsThingService;

	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public ListResultModel<AwsAccountModel> findAccounts(
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		List<AwsAccount> accounts = null;
		if (StringUtils.isNotEmpty(enabled)) {
			accounts = awsAccountService.getAwsAccountRepository()
					.findAllByApplicationIdAndEnabled(accessKey.getApplicationId(), Boolean.parseBoolean(enabled));
		} else {
			accounts = awsAccountService.getAwsAccountRepository().findAllByApplicationId(accessKey.getApplicationId());
		}
		List<AwsAccountModel> data = accounts.stream()
				.map(awsAccount -> buildAwsAccountModel(awsAccount, accessKey.getRefApplication()))
				.collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<AwsAccountModel>().withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/things", method = RequestMethod.GET)
	public ListResultModel<AwsThingModel> findThings(
			@RequestParam(name = "awsAccountHid", required = false) String awsAccountHid,
			@RequestParam(name = "gatewayHid", required = false) String gatewayHid,
			@RequestParam(name = "enabled", required = false) String enabled) {

		AccessKey accessKey = validateApplicationOwner();

		AwsThingSearchParams params = new AwsThingSearchParams();
		String awsAccountId = null;
		if (StringUtils.isNotEmpty(awsAccountHid)) {
			AwsAccount awsAccount = getKronosCache().findAwsAccountByHid(awsAccountHid);
			Assert.notNull(awsAccount, "awsAccount is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(awsAccount.getApplicationId()),
					"application does not match");
			awsAccountId = awsAccount.getId();
			params.addAwsAccountIds(awsAccountId);
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
		Assert.isTrue(awsAccountId != null || gatewayId != null, "missing search criteria");
		if (StringUtils.isNotEmpty(enabled)) {
			params.setEnabled(Boolean.parseBoolean(enabled));
		}

		List<AwsThingModel> data = awsThingService.getAwsThingRepository().findAwsThings(params).stream()
				.map(thing -> buildAwsThingModel(thing)).collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<AwsThingModel>().withData(data).withSize(data.size());
	}

	private AwsAccountModel buildAwsAccountModel(AwsAccount account, Application application) {
		Assert.notNull(account, "account is null");
		Assert.notNull(application, "application is null");

		String method = "buildAwsAccountModel";
		AwsAccountModel result = buildModel(new AwsAccountModel(), account);
		result.setEnabled(account.isEnabled());
		result.setAccessKey(getCryptoService().decrypt(application.getId(), account.getAccessKey()));
		result.setApplicationHid(application.getHid());
		result.setDefaultPolicyArn(account.getDefaultPolicyArn());
		result.setDefaultPolicyName(account.getDefaultPolicyName());
		result.setLogin(getCryptoService().decrypt(application.getId(), account.getLogin()));
		result.setRegion(account.getRegion());
		result.setSecretKey(getCryptoService().decrypt(application.getId(), account.getSecretKey()));
		if (account.getUserId() != null) {
			User user = getCoreCacheService().findUserById(account.getUserId());
			if (user != null) {
				result.setUserHid(user.getHid());
			} else {
				logWarn(method, "user is not found, id: %s", account.getUserId());
			}
		}
		return result;
	}

	private AwsThingModel buildAwsThingModel(AwsThing thing) {
		Assert.notNull(thing, "thing is null");

		String method = "buildAwsThingModel";
		AwsThingModel result = buildModel(new AwsThingModel(), thing);
		AwsAccount awsAccount = getKronosCache().findAwsAccountById(thing.getAwsAccountId());
		if (awsAccount != null) {
			result.setAwsAccountHid(awsAccount.getHid());
		} else {
			logWarn(method, "awsAccount is not found, id: %s", thing.getAwsAccountId());
		}
		Gateway gateway = getKronosCache().findGatewayById(thing.getGatewayId());
		if (gateway != null) {
			result.setGatewayHid(gateway.getHid());
		} else {
			logWarn(method, "gateway is not found, id: %s", thing.getGatewayId());
		}
		result.setCertArn(thing.getCertArn());
		result.setCertId(thing.getCertId());
		result.setCertPem(getCryptoService().decrypt(gateway.getApplicationId(), thing.getCertPem()));
		result.setHost(thing.getHost());
		result.setPolicyName(thing.getPolicyName());
		result.setPrivateKey(getCryptoService().decrypt(gateway.getApplicationId(), thing.getPrivateKey()));
		result.setPublicKey(getCryptoService().decrypt(gateway.getApplicationId(), thing.getPublicKey()));
		result.setThingArn(thing.getThingArn());
		result.setThingName(thing.getThingName());
		result.setEnabled(thing.isEnabled());
		return result;
	}
}
