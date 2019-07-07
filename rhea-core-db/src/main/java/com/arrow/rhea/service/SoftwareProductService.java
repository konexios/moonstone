package com.arrow.rhea.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.repo.SoftwareProductRepository;

@Service
public class SoftwareProductService extends RheaServiceAbstract {

	@Autowired
	private SoftwareProductRepository softwareProductRepository;

	public SoftwareProductRepository getSoftwareProductRepository() {
		return softwareProductRepository;
	}

	public SoftwareProduct create(SoftwareProduct softwareProduct, String who) {
		Assert.notNull(softwareProduct, "softwareProduct release is null");
		Assert.hasText(softwareProduct.getCompanyId(), "applicationId is empty");
		Assert.hasText(softwareProduct.getSoftwareVendorId(), "softwareVendorId is empty");

		String method = "create";
		logInfo(method, "...");

		// persist
		softwareProduct = softwareProductRepository.doInsert(softwareProduct, who);
		softwareProduct = populateRefs(softwareProduct);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.SoftwareProduct.CreateSoftwareProduct)
		        .productName(ProductSystemNames.RHEA).objectId(softwareProduct.getId()).by(who));

		return softwareProduct;
	}

	public SoftwareProduct update(SoftwareProduct softwareProduct, String who) {
		Assert.notNull(softwareProduct, "softwareProduct is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		softwareProduct = softwareProductRepository.doSave(softwareProduct, who);
		softwareProduct = populateRefs(softwareProduct);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.SoftwareProduct.UpdateSoftwareProduct)
		        .productName(ProductSystemNames.RHEA).objectId(softwareProduct.getId()).by(who));

		getRheaCacheService().clearSoftwareProduct(softwareProduct);

		return softwareProduct;
	}

	public SoftwareProduct populateRefs(SoftwareProduct softwareProduct) {
		if (softwareProduct != null) {
			if (softwareProduct.getRefCompany() == null && !StringUtils.isEmpty(softwareProduct.getCompanyId())) {
				softwareProduct.setRefCompany(getCoreCacheService().findCompanyById(softwareProduct.getCompanyId()));
			}

			if (softwareProduct.getRefSoftwareVendor() == null
			        && !StringUtils.isEmpty(softwareProduct.getSoftwareVendorId())) {
				softwareProduct.setRefSoftwareVendor(
				        getRheaCacheService().findSoftwareVendorById(softwareProduct.getSoftwareVendorId()));
			}
		}

		return softwareProduct;
	}
}