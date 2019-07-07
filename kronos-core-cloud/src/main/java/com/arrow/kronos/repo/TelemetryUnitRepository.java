package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.TelemetryUnit;

@Repository
public interface TelemetryUnitRepository
        extends MongoRepository<TelemetryUnit, String>, TelemetryUnitRepositoryExtension {
	public TelemetryUnit findBySystemName(String systemName);

	public List<TelemetryUnit> findAllByEnabled(boolean enabled);
}