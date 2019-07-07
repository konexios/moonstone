package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface DeviceRepositoryExtension extends RepositoryExtension<Device> {
    Page<Device> doFindDevices(Pageable pageable, DeviceSearchParams params);

    List<String> doFindAggregatedDeviceTypeIds(String applicationId, String userId);

    List<String> doFindAggregatedGatewayIds(String applicationId, String userId);

    List<String> doFindAggregatedNodeIds(String applicationId, String userId);

    List<Device> doFindByApplicationIdAndDeviceTypeIdAndEnabled(String applicationId, String[] deviceTypeIds,
            boolean enabled);

    List<TelemetryStat> doCountDevicesByType(String[] deviceTypeIds, boolean enabled);

    long doFindDeviceCount(DeviceSearchParams params);

    List<Device> doFindAllDevices(DeviceSearchParams params);

    List<String> doFindDeviceIds(DeviceSearchParams params, Sort sort);

    List<Device> doFindAllDevicesWithNoTelemetryActions(List<String> deviceTypeIdsWithNoTelemetryActions);
}
