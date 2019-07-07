package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.Device;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String>, DeviceRepositoryExtension {
	// Device findByUidAndApplicationId(String uid, String applicationId);

	List<Device> findAllByUserId(String userId);

	List<Device> findAllByGatewayIdAndEnabled(String gatewayId, boolean enabled);

	List<Device> findAllByGatewayId(String gatewayId);

	List<Device> findAllByDeviceTypeIdAndEnabled(String deviceTypeId, boolean enabled);

	List<Device> findAllByApplicationIdAndNodeId(String applicationId, String nodeId);

	List<Device> findAllByApplicationIdAndNodeIdAndUserId(String applicationId, String nodeId, String userId);

	List<Device> findAllByApplicationIdAndEnabled(String applicationId, boolean enabled);

	List<Device> findAllByNodeIdAndEnabled(String nodeId, boolean enabled);

	// Device findByUid(String uid);

	Device findByApplicationIdAndUid(String applicationId, String uid);

	List<Device> findAllByDeviceTypeIdAndApplicationIdAndEnabled(String deviceTypeId, String applicationId,
			boolean enabled);

	Long deleteByApplicationId(String applicationId);
}
