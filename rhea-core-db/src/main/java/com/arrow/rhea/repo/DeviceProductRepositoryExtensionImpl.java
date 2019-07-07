package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.DeviceProduct;

public class DeviceProductRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceProduct>
        implements DeviceProductRepositoryExtension {
	public DeviceProductRepositoryExtensionImpl() {
		super(DeviceProduct.class);
	}

	@Override
	public Page<DeviceProduct> findDeviceProducts(Pageable pageable, DeviceProductSearchParams params) {
		String methodName = "findDeviceProducts";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>(2);
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			// criteria = addCriteria(criteria, "deviceCategoryId",
			// params.getDeviceCategoryIds());
			criteria = addCriteria(criteria, "deviceCategory", params.getDeviceCategories());
			criteria = addCriteria(criteria, "deviceManufacturerId", params.getDeviceManufacturerIds());
		}

		return doProcessQuery(pageable, criteria);
	}

	@Override
	public List<DeviceProduct> findDeviceProducts(DeviceProductSearchParams params) {
		String methodName = "findDeviceProducts";
		logInfo(methodName, "...");
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(DeviceProductSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			criteria = addCriteria(criteria, "deviceManufacturerId", params.getDeviceManufacturerIds());
			// criteria = addCriteria(criteria, "deviceCategoryId",
			// params.getDeviceCategoryIds());
			criteria = addCriteria(criteria, "deviceCategory", params.getDeviceCategories());
		}
		return criteria;
	}
}
