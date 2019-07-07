package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface GatewayRepositoryExtension extends RepositoryExtension<Gateway> {

    List<Gateway> doFindByApplicationIdAndGatewayIds(String applicationId, String... gatewayIds);

    Page<Gateway> findGateways(Pageable pageable, GatewaySearchParams params);

    List<Gateway> findGateways(GatewaySearchParams params);

    List<String> findAggregatedField(String applicationId, String fieldName);

    List<String> findAggregatedOsNames(String applicationId);

    List<String> findAggregatedSoftwareNames(String applicationId);

    List<String> findAggregatedSoftwareVersions(String applicationId);

    List<String> findAggregatedField(String applicationId, String fieldName, String userId);

    List<String> findAggregatedOsNames(String applicationId, String userId);

    List<String> findAggregatedSoftwareNames(String applicationId, String userId);

    List<String> findAggregatedSoftwareVersions(String applicationId, String userId);

    List<String> findAggregatedNodeIds(String applicationId, String userId);

    long findGatewayCount(GatewaySearchParams params);

    List<TelemetryStat> countGatewaysByType(String[] gatewayTypeIds, boolean enabled);

    List<String> findGatewayIds(GatewaySearchParams params, Sort sort);

    List<Gateway> findByApplicationIdAndDeviceTypeIdAndEnabled(String applicationId, String[] deviceTypeIds,
            boolean enabled);
}
