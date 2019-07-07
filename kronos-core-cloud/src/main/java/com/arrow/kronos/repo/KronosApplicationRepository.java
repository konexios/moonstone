package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.KronosApplication;

@Repository
public interface KronosApplicationRepository
        extends MongoRepository<KronosApplication, String>, KronosApplicationRepositoryExtension {
	KronosApplication findByApplicationId(String applicationId);

	Long deleteByApplicationId(String applicationId);
}