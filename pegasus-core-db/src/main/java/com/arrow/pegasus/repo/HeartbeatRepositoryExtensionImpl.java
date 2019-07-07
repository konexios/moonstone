package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.pegasus.data.heartbeat.Heartbeat;

public class HeartbeatRepositoryExtensionImpl extends RepositoryExtensionAbstract<Heartbeat>
        implements HeartbeatRepositoryExtension {

    public HeartbeatRepositoryExtensionImpl() {
        super(Heartbeat.class);
    }

    @Override
    public long findHeartbeatCount(HeartbeatSearchParams params) {
        String methodName = "findHeartbeatCount";
        logInfo(methodName, "...");
        List<Criteria> criteria = buildCriteria(params);
        Query query = doProcessCriteria(criteria);
        return doCount(query);
    }

    private List<Criteria> buildCriteria(HeartbeatSearchParams params) {
        List<Criteria> criteria = new ArrayList<>();
        if (params != null) {
            criteria = addCriteria(criteria, "objectType", params.getObjectTypes());
            criteria = addCriteria(criteria, "objectId", params.getObjectIds());
            criteria = addCriteria(criteria, "timestamp", params.getFromTimestamp(), params.getToTimestamp());
        }
        return criteria;
    }
}
