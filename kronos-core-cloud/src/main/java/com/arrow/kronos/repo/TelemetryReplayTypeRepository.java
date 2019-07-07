package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.TelemetryReplayType;

@Repository
public interface TelemetryReplayTypeRepository
        extends MongoRepository<TelemetryReplayType, String>, TelemetryReplayTypeRepositoryExtension {

	public TelemetryReplayType findByName(String name);

	public List<TelemetryReplayType> findByApplicationId(String applicationId);

	public List<TelemetryReplayType> findByEnabled(boolean enabled);

	public List<TelemetryReplayType> findByApplicationIdAndEnabled(String applicationId, boolean enabled);
}