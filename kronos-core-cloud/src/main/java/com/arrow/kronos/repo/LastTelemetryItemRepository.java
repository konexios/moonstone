package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.LastTelemetryItem;

@Repository
public interface LastTelemetryItemRepository
        extends MongoRepository<LastTelemetryItem, String>, LastTelemetryItemRepositoryExtension {
    List<LastTelemetryItem> findByDeviceIdAndName(String deviceId, String name);

    List<LastTelemetryItem> findByDeviceId(String deviceId);

    Long deleteByDeviceId(String deviceId);

    Long deleteByApplicationId(String applicationId);
}
