package com.arrow.kronos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmGateway;
import com.arrow.kronos.repo.IbmGatewayRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class IbmGatewayService extends KronosServiceAbstract {

	@Autowired
	private IbmGatewayRepository ibmGatewayRepository;

	public IbmGatewayRepository getIbmGatewayRepository() {
		return ibmGatewayRepository;
	}

	public IbmGateway findBy(Gateway gateway) {
		List<IbmGateway> existing = ibmGatewayRepository.findAllByGatewayIdAndEnabled(gateway.getId(), true);
		if (existing.size() > 1) {
			throw new AcsLogicalException("more than one active IBMGateway is linked to this gateway");
		} else if (existing.size() == 1) {
			return existing.get(0);
		} else {
			return null;
		}
	}

	public void deleteBy(Gateway gateway) {
		String method = "deleteBy";
		Assert.notNull(gateway, "gateway is null");
		Long numDeleted = ibmGatewayRepository.deleteByGatewayId(gateway.getId());
		logInfo(method,
		        "IBM Gateway objects have been deleted for gateway id=" + gateway.getId() + ", total " + numDeleted);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return ibmGatewayRepository.deleteByApplicationId(applicationId);
	}
}
