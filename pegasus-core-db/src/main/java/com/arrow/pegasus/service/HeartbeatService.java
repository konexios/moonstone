package com.arrow.pegasus.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.heartbeat.Heartbeat;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.repo.HeartbeatRepository;

@Service
public class HeartbeatService extends BaseServiceAbstract {

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    public HeartbeatRepository getHeartbeatRepository() {
        return heartbeatRepository;
    }

    public void create(HeartbeatObjectType type, String objectId) {
        String method = "create";
        Assert.hasLength(objectId, "objectId is empty");

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setObjectType(type);
        heartbeat.setObjectId(objectId);
        heartbeat.setTimestamp(Instant.now().toEpochMilli());
        heartbeatRepository.doSave(heartbeat, null);
        logDebug(method, "created heartbeat for: %s: %s", type, objectId);
    }

    public void deleteBy(HeartbeatObjectType objectType, String objectId) {
        String method = "deleteBy";
        Assert.notNull(objectType, "objectType is null");
        Assert.hasLength(objectId, "objectId is empty");

        Long numDeleted = heartbeatRepository.deleteByObjectTypeAndObjectId(objectType, objectId);
        logInfo(method, "Heartbeats have been deleted for objectType=" + objectType.name() + ", objectId=" + objectId
                + ", total " + numDeleted);
    }
}
