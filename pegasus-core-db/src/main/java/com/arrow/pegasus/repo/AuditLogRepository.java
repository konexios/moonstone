package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.AuditLog;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String>, AuditLogRepositoryExtension {
	Long deleteByApplicationId(String applicationId);

	Long deleteByTypeAndObjectId(String type, String objectId);
}
