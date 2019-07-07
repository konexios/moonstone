package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.Telemetry;

@Repository
public interface TelemetryRepository extends MongoRepository<Telemetry, String>, TelemetryRepositoryExtension {
	Long deleteByDeviceId(String deviceId);

	Long deleteByApplicationId(String applicationId);
}
