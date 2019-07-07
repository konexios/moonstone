package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.PlatformConfig;

@Repository
public interface PlatformConfigRepository extends MongoRepository<PlatformConfig, String>, PlatformConfigRepositoryExtension {
}
