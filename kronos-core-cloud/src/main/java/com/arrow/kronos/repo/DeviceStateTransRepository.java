package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.DeviceStateTrans;

@Repository
public interface DeviceStateTransRepository
        extends MongoRepository<DeviceStateTrans, String>, DeviceStateTransRepositoryExtension {
	Long deleteByDeviceId(String deviceId);

	List<DeviceStateTrans> findAllByDeviceId(String deviceId);

	Long deleteByApplicationId(String applicationId);
}