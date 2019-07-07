package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Telemetry;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class TelemetryRepositoryExtensionImpl extends RepositoryExtensionAbstract<Telemetry>
        implements TelemetryRepositoryExtension {
    public TelemetryRepositoryExtensionImpl() {
        super(Telemetry.class);
    }

    public Page<Telemetry> doFindTelemetries(PageRequest pageable, TelemetrySearchParams params) {
        String method = "doFindTelemetriesWithPartition";

        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
        }
        Query query = doProcessCriteria(criteria);
        long count = getOperations().count(query, Telemetry.COLLECTION_NAME);
        query.with(pageable);
        List<Telemetry> list = getOperations().find(query, Telemetry.class, Telemetry.COLLECTION_NAME);

        logInfo(method, "total: %d, page: %d, limit: %d, skip: %d", count, list.size(), pageable.getPageSize(),
                pageable.getOffset());

        return new PageImpl<Telemetry>(list, pageable, count);
    }

    public List<Telemetry> findTelemetries(TelemetrySearchParams params, Direction direction) {
        String method = "findTelemetries";

        if (direction == null)
            direction = Direction.ASC;

        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
            criteria = addCriteria(criteria, "timestamp", params.getFromTimestamp(), params.getToTimestamp());
        }

        Query query = doProcessCriteria(criteria);
        query.with(new Sort(direction, "timestamp"));
        List<Telemetry> list = getOperations().find(query, Telemetry.class, Telemetry.COLLECTION_NAME);

        logInfo(method, "total: %d", list.size());

        return list;
    }

    public long removeByTimestamps(long fromTimestamp, long toTimestamp) {
        String methodName = "removeByTimestamps";
        logInfo(methodName, "fromTs: %d,  toTs: %d", fromTimestamp, toTimestamp);
        List<Criteria> criteria = new ArrayList<>();
        addCriteria(criteria, "timestamp", fromTimestamp, toTimestamp);
        Query query = doProcessCriteria(criteria);
        return getOperations().remove(query, getDocumentClass()).getDeletedCount();
    }

    public long removeByDeviceIdAndTimestamps(String deviceId, long fromTimestamp, long toTimestamp) {
        String methodName = "removeByDeviceIdAndTimestamps";
        Assert.hasText(deviceId, "deviceId is empty");
        logInfo(methodName, "deviceId: %s, fromTs: %d,  toTs: %d", deviceId, fromTimestamp, toTimestamp);

        List<Criteria> criteria = new ArrayList<>();
        addCriteria(criteria, "deviceId", deviceId);
        addCriteria(criteria, "timestamp", fromTimestamp, toTimestamp);
        Query query = doProcessCriteria(criteria);
        return getOperations().remove(query, getDocumentClass()).getDeletedCount();
    }

    public long removeByApplicationIdAndTimestamps(String applicationId, long fromTimestamp, long toTimestamp) {
        String methodName = "removeByApplicationIdAndTimestamps";
        Assert.hasText(applicationId, "applicationId is empty");
        logInfo(methodName, "applicationId: %s, fromTs: %d,  toTs: %d", applicationId, fromTimestamp, toTimestamp);

        List<Criteria> criteria = new ArrayList<>();
        addCriteria(criteria, "applicationId", applicationId);
        addCriteria(criteria, "timestamp", fromTimestamp, toTimestamp);
        Query query = doProcessCriteria(criteria);
        return getOperations().remove(query, getDocumentClass()).getDeletedCount();
    }
}
