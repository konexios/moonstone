package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.KronosUser;

@Repository
public interface KronosUserRepository extends MongoRepository<KronosUser, String>, KronosUserRepositoryExtension {
	KronosUser findByUserIdAndApplicationId(String userId, String applicationId);

	Long deleteByApplicationId(String applicationId);
}