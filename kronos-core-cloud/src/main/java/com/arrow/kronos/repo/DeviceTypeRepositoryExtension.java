package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.DeviceType;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface DeviceTypeRepositoryExtension extends RepositoryExtension<DeviceType> {

	List<DeviceType> findDeviceTypes(DeviceTypeSearchParams params);

	Page<DeviceType> findDeviceTypes(Pageable pageable, DeviceTypeSearchParams params);

	List<DeviceType> doFindAllByIdsApplicationIdAndEnabled(List<String> ids, String applicationId, boolean enabled);

	List<DeviceType> findByApplicationIdAndNames(String applicationId, String[] names);
	
	List<String> findAllDeviceTypesWithNoTelemetryActions();
}
