package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.DeviceState;

@Repository
public interface DeviceStateRepository extends MongoRepository<DeviceState, String>, DeviceStateRepositoryExtension {
	DeviceState findByApplicationIdAndDeviceId(String applicationId, String deviceId);

	List<DeviceState> deleteByApplicationIdAndDeviceId(String applicationId, String deviceId);

	List<DeviceState> findAllByApplicationIdAndDeviceId(String applicationId, String deviceId);

	Long deleteByApplicationId(String applicationId);
}