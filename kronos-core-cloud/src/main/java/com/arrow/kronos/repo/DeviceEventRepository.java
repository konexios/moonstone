package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.DeviceEvent;

@Repository
public interface DeviceEventRepository extends MongoRepository<DeviceEvent, String>, DeviceEventRepositoryExtension {
    Long deleteByDeviceId(String deviceId);

    List<DeviceEvent> findAllByDeviceId(String deviceId);

    Long deleteByApplicationId(String applicationId);
}
