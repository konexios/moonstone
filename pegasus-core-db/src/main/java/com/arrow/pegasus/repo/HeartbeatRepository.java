package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.heartbeat.Heartbeat;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;

@Repository
public interface HeartbeatRepository extends MongoRepository<Heartbeat, String>, HeartbeatRepositoryExtension {
	Long deleteByObjectTypeAndObjectId(HeartbeatObjectType objectType, String objectId);
}
