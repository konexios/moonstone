package com.arrow.rhea.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.DeviceCategory;

public interface DeviceCategoryRepositoryExtension extends RepositoryExtension<DeviceCategory> {
	Page<DeviceCategory> findDeviceCategories(Pageable pageable, DeviceCategorySearchParams params);
}