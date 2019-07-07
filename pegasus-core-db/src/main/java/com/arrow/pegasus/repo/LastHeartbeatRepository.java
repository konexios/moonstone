package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;

@Repository
public interface LastHeartbeatRepository
        extends MongoRepository<LastHeartbeat, String>, LastHeartbeatRepositoryExtension {
    public LastHeartbeat findByObjectTypeAndObjectId(HeartbeatObjectType objectType, String objectId);

    Long deleteByObjectTypeAndObjectId(HeartbeatObjectType objectType, String objectId);
}
