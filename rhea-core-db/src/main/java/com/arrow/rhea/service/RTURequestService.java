package com.arrow.rhea.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.repo.RTURequestRepository;

@Service
public class RTURequestService extends RheaServiceAbstract {

	@Autowired
	private RTURequestRepository rtuRequestRepository;

	public RTURequestRepository getRTURequestRepository() {
		return rtuRequestRepository;
	}

	public RTURequest create(RTURequest rtuRequest, String who) {
		Assert.notNull(rtuRequest, "Right to update Request is null");
		Assert.hasText(who, "who is empty");

		String method = "create";
		logInfo(method, "...");
			
		// persist
		rtuRequest = rtuRequestRepository.doInsert(rtuRequest, who);
		rtuRequest = populateRefs(rtuRequest);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.RTURequest.CreateRTURequest)
		        .productName(ProductSystemNames.RHEA).objectId(rtuRequest.getId()).by(who));

		return rtuRequest;
	}

	public RTURequest update(RTURequest rtuRequest, String who) {
		Assert.notNull(rtuRequest, "Right to update Request is null");
		Assert.hasText(who, "who is empty");

		String method = "update";
		logInfo(method, "...");

		// persist
		rtuRequest = rtuRequestRepository.doSave(rtuRequest, who);
		rtuRequest = populateRefs(rtuRequest);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.RTURequest.UpdateRTURequest)
		        .productName(ProductSystemNames.RHEA).objectId(rtuRequest.getId()).by(who));

		getRheaCacheService().clearRTURequest(rtuRequest);

		return rtuRequest;
	}

	public RTURequest populateRefs(RTURequest rtuRequest) {
		if (rtuRequest != null) {
			if (rtuRequest.getRefCompany() == null && !StringUtils.isEmpty(rtuRequest.getCompanyId())) {
				rtuRequest.setRefCompany(getCoreCacheService().findCompanyById(rtuRequest.getCompanyId()));
			}
			
			if (rtuRequest.getRefSoftwareRelease() == null && !StringUtils.isEmpty(rtuRequest.getSoftwareReleaseId())) {
				rtuRequest.setRefSoftwareRelease(getRheaCacheService().findSoftwareReleaseById(rtuRequest.getSoftwareReleaseId()));
			}
		}

		return rtuRequest;
	}
}