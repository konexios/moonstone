package com.arrow.kronos.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.DeviceStateTrans;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface DeviceStateTransRepositoryExtension extends RepositoryExtension<DeviceStateTrans> {

	Page<DeviceStateTrans> findDeviceStateTrans(Pageable pageable, DeviceStateTransSearchParams params);
}