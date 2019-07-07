package com.arrow.rhea.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.repo.DeviceProductRepository;

@Service
public class DeviceProductService extends RheaServiceAbstract {

	@Autowired
	private DeviceProductRepository deviceProductRepository;

	public DeviceProductRepository getDeviceProductRepository() {
		return deviceProductRepository;
	}

	public DeviceProduct create(DeviceProduct deviceProduct, String who) {
		Assert.notNull(deviceProduct, "device product is null");
		Assert.hasText(deviceProduct.getDeviceManufacturerId(), "deviceManufacturerId is empty");
		// Assert.hasText(deviceProduct.getDeviceCategoryId(), "deviceCategoryId
		// is empty");
		Assert.notNull(deviceProduct.getDeviceCategory(), "deviceCategory is null");

		String method = "create";
		logInfo(method, "...");

		// persist
		deviceProduct = deviceProductRepository.doInsert(deviceProduct, who);
		deviceProduct = populateRefs(deviceProduct);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.DeviceProduct.CreateDeviceProduct)
		        .productName(ProductSystemNames.RHEA).objectId(deviceProduct.getId()).by(who));

		return deviceProduct;
	}

	public DeviceProduct update(DeviceProduct deviceProduct, String who) {
		Assert.notNull(deviceProduct, "device product is null");

		String method = "update";
		logInfo(method, "...");

		// persist
		deviceProduct = deviceProductRepository.doSave(deviceProduct, who);
		deviceProduct = populateRefs(deviceProduct);

		getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.DeviceProduct.UpdateDeviceProduct)
		        .productName(ProductSystemNames.RHEA).objectId(deviceProduct.getId()).by(who));

		getRheaCacheService().clearDeviceProduct(deviceProduct);

		return deviceProduct;
	}

	public DeviceProduct populateRefs(DeviceProduct deviceProduct) {
		if (deviceProduct != null) {
			if (deviceProduct.getRefCompany() == null && !StringUtils.isEmpty(deviceProduct.getCompanyId())) {
				deviceProduct.setRefCompany(getCoreCacheService().findCompanyById(deviceProduct.getCompanyId()));
			}

			if (deviceProduct.getRefDeviceManufacturer() == null
			        && !StringUtils.isEmpty(deviceProduct.getDeviceManufacturerId())) {
				deviceProduct.setRefDeviceManufacturer(
				        getRheaCacheService().findDeviceManufacturerById(deviceProduct.getDeviceManufacturerId()));
			}

			// if (deviceProduct.getRefDeviceCategory() == null
			// && !StringUtils.isEmpty(deviceProduct.getDeviceCategoryId())) {
			// deviceProduct.setRefDeviceCategory(
			// getRheaCacheService().findDeviceCategoryById(deviceProduct.getDeviceCategoryId()));
			// }
		}

		return deviceProduct;
	}
}