package com.arrow.pegasus.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;
import com.arrow.pegasus.repo.LastHeartbeatRepository;

@Service
public class LastHeartbeatService extends BaseServiceAbstract {

    @Autowired
    private LastHeartbeatRepository lastHeartbeatRepository;

    public LastHeartbeatRepository getLastHeartbeatRepository() {
        return lastHeartbeatRepository;
    }

    public String update(HeartbeatObjectType type, String objectId) {
        String method = "update";
        Assert.hasLength(objectId, "objectId is empty");

        LastHeartbeat last = lastHeartbeatRepository.findByObjectTypeAndObjectId(type, objectId);
        if (last != null) {
            last.setTimestamp(Instant.now().toEpochMilli());
            lastHeartbeatRepository.doSave(last, null);
            logDebug(method, "updated timestamp for %s: %s", type, objectId);
        } else {
            last = new LastHeartbeat();
            last.setObjectType(type);
            last.setObjectId(objectId);
            last.setTimestamp(Instant.now().toEpochMilli());
            lastHeartbeatRepository.doInsert(last, null);
            logDebug(method, "created lastHeartbeat for %s: %s", type, objectId);
        }
        return last.getId();
    }

    public void deleteBy(HeartbeatObjectType objectType, String objectId) {
        String method = "deleteBy";
        Assert.notNull(objectType, "objectType is null");
        Assert.hasLength(objectId, "objectId is empty");

        Long numDeleted = lastHeartbeatRepository.deleteByObjectTypeAndObjectId(objectType, objectId);
        logInfo(method, "Last heartbeat has been deleted for objectType=" + objectType.name() + ", objectId=" + objectId
                + ", total " + numDeleted);
    }
}
