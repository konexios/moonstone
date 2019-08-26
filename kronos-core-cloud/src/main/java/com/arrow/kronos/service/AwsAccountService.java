package com.arrow.kronos.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AwsThing;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.AwsAccountRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.AcsSystemException;
import moonstone.acs.AcsUtils;

@Service
public class AwsAccountService extends KronosServiceAbstract {

	private final static String CA_CERT_FILE = "/aws/ca-cert.crt";

	@Autowired
	private AwsAccountRepository awsAccountRepository;
	@Autowired
	private AwsThingService awsThingService;

	public AwsAccountRepository getAwsAccountRepository() {
		return awsAccountRepository;
	}

	public AwsAccount findActiveAccount(Gateway gateway) {
		AwsThing thing = awsThingService.findBy(gateway);
		if (thing != null) {
			return getKronosCache().findAwsAccountById(thing.getAwsAccountId());
		} else {
			List<AwsAccount> accounts = new ArrayList<>();
			if (StringUtils.isNotEmpty(gateway.getUserId())) {
				accounts.addAll(awsAccountRepository.findAllByUserIdAndEnabled(gateway.getUserId(), true));
			}
			if (accounts.size() == 0) {
				accounts.addAll(
						awsAccountRepository.findAllByApplicationIdAndEnabled(gateway.getApplicationId(), true));
			}
			if (accounts.size() > 1) {
				throw new AcsLogicalException(
						"more than one AWS account is active in the system for this user/application");
			} else if (accounts.size() == 1) {
				return accounts.get(0);
			} else {
				return null;
			}
		}
	}

	public String loadCaCert() {
		try {
			return AcsUtils.streamToString(getClass().getResourceAsStream(CA_CERT_FILE), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new AcsSystemException("unable to load ca-cert: " + CA_CERT_FILE);
		}
	}

	public AwsAccount upsert(AwsAccount awsAccount, String who) {
		Assert.notNull(awsAccount, "awsAccount is null");

		// persist
		if (awsAccount.getId() != null) {
			awsAccount = awsAccountRepository.doSave(awsAccount, who);

			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.AwsAccount.UpdateAwsAccount)
					.applicationId(awsAccount.getApplicationId()).productName(ProductSystemNames.KRONOS)
					.objectId(awsAccount.getId()).by(who));

			// clear cache
			getKronosCache().clearAwsAccount(awsAccount);
		} else {
			// TODO not tested
			awsAccount = awsAccountRepository.doInsert(awsAccount, who);

			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.AwsAccount.CreateAwsAccount)
					.applicationId(awsAccount.getApplicationId()).productName(ProductSystemNames.KRONOS)
					.objectId(awsAccount.getId()).by(who));
		}

		return awsAccount;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return awsAccountRepository.deleteByApplicationId(applicationId);
	}
}
