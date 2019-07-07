package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.TelemetryItem;

@Repository
public interface TelemetryItemRepository
        extends MongoRepository<TelemetryItem, String>, TelemetryItemRepositoryExtension {
	List<TelemetryItem> findByTelemetryId(String telemetryId);

	List<TelemetryItem> findByDeviceIdAndName(String deviceId, String name);

	Long deleteByDeviceId(String deviceId);

	Long deleteByApplicationId(String applicationId);
}
