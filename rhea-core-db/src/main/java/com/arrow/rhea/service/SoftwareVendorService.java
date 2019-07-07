package com.arrow.rhea.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.SoftwareVendor;
import com.arrow.rhea.repo.SoftwareVendorRepository;

@Service
public class SoftwareVendorService extends RheaServiceAbstract {

	@Autowired
	private SoftwareVendorRepository softwareVendorRepository;

	public SoftwareVendorRepository getSoftwareVendorRepository() {
		return softwareVendorRepository;
	}

	public SoftwareVendor create(SoftwareVendor softwareVendor, String who) {
		Assert.notNull(softwareVendor, "software vendor is null");

		String method = "create";
		logInfo(method, "...");

		// persist
		softwareVendor = softwareVendorRepository.doInsert(softwareVendor, who);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.SoftwareVendor.CreateSoftwareVendor)
		        .productName(ProductSystemNames.RHEA).objectId(softwareVendor.getId()).by(who));

		return softwareVendor;
	}

	public SoftwareVendor update(SoftwareVendor softwareVendor, String who) {
		Assert.notNull(softwareVendor, "software vendor is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		softwareVendor = softwareVendorRepository.doSave(softwareVendor, who);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.SoftwareVendor.UpdateSoftwareVendor)
		        .productName(ProductSystemNames.RHEA).objectId(softwareVendor.getId()).by(who));

		getRheaCacheService().clearSoftwareVendor(softwareVendor);

		return softwareVendor;
	}

	public SoftwareVendor populateRefs(SoftwareVendor softwareVendor) {
		if (softwareVendor != null) {
			if (softwareVendor.getRefCompany() == null && !StringUtils.isEmpty(softwareVendor.getCompanyId())) {
				softwareVendor.setRefCompany(getCoreCacheService().findCompanyById(softwareVendor.getCompanyId()));
			}
		}

		return softwareVendor;
	}
}