package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.DeviceActionType;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface DeviceActionTypeRepositoryExtension extends RepositoryExtension<DeviceActionType> {
    Page<DeviceActionType> findDeviceActionTypes(Pageable pageable, KronosDocumentSearchParams params);

    List<DeviceActionType> findByApplicationIdAndSystemNames(String applicationId, String[] systemNames);
}
