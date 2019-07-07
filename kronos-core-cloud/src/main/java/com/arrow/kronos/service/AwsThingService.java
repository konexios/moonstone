package com.arrow.kronos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.AwsThing;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.AwsThingRepository;

@Service
public class AwsThingService extends KronosServiceAbstract {
	@Autowired
	private AwsThingRepository awsThingRepository;

	public AwsThingRepository getAwsThingRepository() {
		return awsThingRepository;
	}

	public AwsThing findBy(Gateway gateway) {
		List<AwsThing> existing = awsThingRepository.findAllByGatewayIdAndEnabled(gateway.getId(), true);
		if (existing.size() > 1) {
			throw new AcsLogicalException("more than one active AwsThing is linked to this gateway");
		} else if (existing.size() == 1) {
			return existing.get(0);
		} else {
			return null;
		}
	}

	public void deleteBy(Gateway gateway) {
		String method = "deleteBy";
		Assert.notNull(gateway, "gateway is null");
		Long numDeleted = awsThingRepository.deleteByGatewayId(gateway.getId());
		logInfo(method,
		        "AwsThing objects have been deleted for gateway id=" + gateway.getId() + ", total " + numDeleted);
	}

	public List<AwsThing> findAllByAwsAccountIdAndHost(String awsAccountId, String host) {
		return awsThingRepository.findAllByAwsAccountIdAndHostAndEnabled(awsAccountId, host, true);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return awsThingRepository.deleteByApplicationId(applicationId);
	}
}
