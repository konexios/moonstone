package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.rhea.data.DeviceType;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface DeviceTypeRepositoryExtension extends RepositoryExtension<DeviceType> {
    Page<DeviceType> findDeviceTypes(Pageable pageable, DeviceTypeSearchParams params);

    List<DeviceType> doFindAllByIdsCompanyIdAndEnabled(List<String> ids, String applicationId, boolean enabled);
    
    List<DeviceType> findByCompanyIdAndNames(String applicationId, String[] names);

    List<DeviceType> findDeviceTypes(DeviceTypeSearchParams params);
}
