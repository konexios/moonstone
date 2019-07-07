package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.DeviceProduct;

public interface DeviceProductRepositoryExtension extends RepositoryExtension<DeviceProduct> {
	Page<DeviceProduct> findDeviceProducts(Pageable pageable, DeviceProductSearchParams params);

	List<DeviceProduct> findDeviceProducts(DeviceProductSearchParams params);
}
