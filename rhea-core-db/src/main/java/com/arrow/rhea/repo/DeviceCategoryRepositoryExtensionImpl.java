package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.DeviceCategory;

public class DeviceCategoryRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceCategory>
        implements DeviceCategoryRepositoryExtension {
	public DeviceCategoryRepositoryExtensionImpl() {
		super(DeviceCategory.class);
	}

	@Override
	public Page<DeviceCategory> findDeviceCategories(Pageable pageable, DeviceCategorySearchParams params) {
		String methodName = "findDeviceCategories";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>(2);
		if (params != null) {
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}

		return doProcessQuery(pageable, criteria);
	}
}