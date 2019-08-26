package com.arrow.kronos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.AzureDevice;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.AzureAccountRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

import moonstone.acs.AcsLogicalException;

@Service
public class AzureAccountService extends KronosServiceAbstract {

	@Autowired
	private AzureAccountRepository azureAccountRepository;

	@Autowired
	private AzureDeviceService azureDeviceService;

	public AzureAccountRepository getAzureAccountRepository() {
		return azureAccountRepository;
	}

	public AzureAccount findActiveAccount(Gateway gateway) {
		AzureDevice awsDevice = azureDeviceService.findBy(gateway);
		if (awsDevice != null) {
			return getKronosCache().findAzureAccountById(awsDevice.getAzureAccountId());
		} else {
			List<AzureAccount> accounts = azureAccountRepository
					.findAllByApplicationIdAndEnabled(gateway.getApplicationId(), true);
			if (accounts.size() > 1) {
				throw new AcsLogicalException(
						"more than one AzureAccount is active in the system for this application");
			} else if (accounts.size() == 1) {
				return accounts.get(0);
			} else {
				return null;
			}
		}
	}

	public AzureAccount upsert(AzureAccount azureAccount, String who) {
		Assert.notNull(azureAccount, "azureAccount is null");

		// persist
		if (azureAccount.getId() != null) {
			azureAccount = azureAccountRepository.doSave(azureAccount, who);

			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.AzureAccount.UpdateAzureAccount)
					.applicationId(azureAccount.getApplicationId()).productName(ProductSystemNames.KRONOS)
					.objectId(azureAccount.getId()).by(who));

			// clear cache
			getKronosCache().clearAzureAccount(azureAccount);
		} else {
			// TODO not tested
			azureAccount = azureAccountRepository.doInsert(azureAccount, who);

			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.AwsAccount.CreateAwsAccount)
					.applicationId(azureAccount.getApplicationId()).productName(ProductSystemNames.KRONOS)
					.objectId(azureAccount.getId()).by(who));
		}

		return azureAccount;
	}

	public String buildIotHubConnectionString(AzureAccount account) {
		Assert.notNull(account, "account is null");
		Application application = getCoreCacheService().findApplicationById(account.getApplicationId());
		checkEnabled(application, "application");
		return String.format("HostName=%s;SharedAccessKeyName=%s;SharedAccessKey=%s", account.getHostName(),
				account.getAccessKeyName(), getCryptoService().decrypt(application.getId(), account.getAccessKey()));
	}

	public String buildEventHubConnectionString(AzureAccount account) {
		Assert.notNull(account, "account is null");
		Application application = getCoreCacheService().findApplicationById(account.getApplicationId());
		checkEnabled(application, "application");
		return String.format("Endpoint=%s;SharedAccessKeyName=%s;SharedAccessKey=%s;EntityPath=%s",
				account.getEventHubEndpoint(), account.getAccessKeyName(),
				getCryptoService().decrypt(application.getId(), account.getAccessKey()), account.getEventHubName());
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return azureAccountRepository.deleteByApplicationId(applicationId);
	}
}
