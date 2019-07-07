package com.arrow.rhea.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.DeviceManufacturer;

public interface DeviceManufacturerRepositoryExtension extends RepositoryExtension<DeviceManufacturer> {
	Page<DeviceManufacturer> findDeviceManufacturers(Pageable pageable, DeviceManufacturerSearchParams params);
}