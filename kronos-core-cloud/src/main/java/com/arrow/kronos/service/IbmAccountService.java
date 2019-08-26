package com.arrow.kronos.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.repo.IbmAccountRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

import moonstone.acs.AcsLogicalException;

@Service
public class IbmAccountService extends KronosServiceAbstract {

	@Autowired
	private IbmAccountRepository ibmAccountRepository;

	public IbmAccountRepository getIbmAccountRepository() {
		return ibmAccountRepository;
	}

	public IbmAccount findActiveAccount(Gateway gateway) {
		List<IbmAccount> accounts = new ArrayList<>();
		if (StringUtils.isNotEmpty(gateway.getUserId())) {
			accounts.addAll(ibmAccountRepository.findAllByApplicationIdAndUserIdAndEnabled(gateway.getApplicationId(),
			        gateway.getUserId(), true));
		}
		if (accounts.size() == 0) {
			accounts.addAll(ibmAccountRepository.findAllByApplicationIdAndUserIdAndEnabled(gateway.getApplicationId(),
			        null, true));
		}
		if (accounts.size() > 1) {
			throw new AcsLogicalException(
			        "more than one IBM account is active in the system for this user/application");
		} else if (accounts.size() == 1) {
			return accounts.get(0);
		} else {
			return null;
		}
	}

	public IbmAccount findActiveAccount(Device device) {
		List<IbmAccount> accounts = new ArrayList<>();
		if (StringUtils.isNotEmpty(device.getUserId())) {
			accounts.addAll(ibmAccountRepository.findAllByApplicationIdAndUserIdAndEnabled(device.getApplicationId(),
			        device.getUserId(), true));
		}
		if (accounts.size() == 0) {
			accounts.addAll(ibmAccountRepository.findAllByApplicationIdAndUserIdAndEnabled(device.getApplicationId(),
			        null, true));
		}
		if (accounts.size() > 1) {
			throw new AcsLogicalException(
			        "more than one IBM account is active in the system for this user/application");
		} else if (accounts.size() == 1) {
			return accounts.get(0);
		} else {
			return null;
		}
	}

	public IbmAccount upsert(IbmAccount ibmAccount, String who) {
		Assert.notNull(ibmAccount, "ibmAccount is null");

		// persist
		if (ibmAccount.getId() != null) {
			ibmAccount = ibmAccountRepository.doSave(ibmAccount, who);

			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.IbmAccount.UpdateIbmAccount)
			        .applicationId(ibmAccount.getApplicationId()).productName(ProductSystemNames.KRONOS)
			        .objectId(ibmAccount.getId()).by(who));

			// clear cache
			getKronosCache().clearIbmAccount(ibmAccount);
		} else {
			// TODO not tested
			ibmAccount = ibmAccountRepository.doInsert(ibmAccount, who);

			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.IbmAccount.CreateIbmAccount)
			        .applicationId(ibmAccount.getApplicationId()).productName(ProductSystemNames.KRONOS)
			        .objectId(ibmAccount.getId()).by(who));
		}

		return ibmAccount;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return ibmAccountRepository.deleteByApplicationId(applicationId);
	}
}
