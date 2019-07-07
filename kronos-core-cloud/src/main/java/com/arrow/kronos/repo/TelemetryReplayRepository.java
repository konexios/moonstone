package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.TelemetryReplay;

@Repository
public interface TelemetryReplayRepository
        extends MongoRepository<TelemetryReplay, String>, TelemetryReplayRepositoryExtension {
	Long deleteByApplicationId(String applicationId);
}