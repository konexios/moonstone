package com.arrow.kronos.repo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.mongodb.client.result.UpdateResult;

public class DeviceEventRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceEvent>
        implements DeviceEventRepositoryExtension {
    public DeviceEventRepositoryExtensionImpl() {
        super(DeviceEvent.class);
    }

    @Override
    public long incrementCounter(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("counter", 1);
        UpdateResult result = getOperations().updateFirst(query, update, DeviceEvent.class);
        return result.getModifiedCount();
    }

    @Override
    public Page<DeviceEvent> findDeviceEvents(Pageable pageable, DeviceEventSearchParams params) {
        String methodName = "findDeviceEvents";
        logDebug(methodName, "...");
        return doProcessQuery(pageable, buildCriteria(params));
    }

    @Override
    public List<DeviceEvent> findDeviceEvents(DeviceEventSearchParams params) {
        String methodName = "findDeviceEvents";
        logDebug(methodName, "...");
        return doProcessQuery(buildCriteria(params));
    }

    public long findDeviceEventCount(String deviceId, Instant fromTimestamp, Instant toTimestamp) {

        String methodName = "findDeviceEventCount";
        logDebug(methodName, "...");

        List<Criteria> criteria = new ArrayList<>();
        addCriteria(criteria, "deviceId", deviceId);
        addCriteria(criteria, "createdDate", fromTimestamp, toTimestamp);

        Query query = doProcessCriteria(criteria);

        return getOperations().count(query, getDocumentClass());
    }

    @Override
    public List<String> doFindDeviceActionTypeIds(Collection<String> deviceIds) {
        String methodName = "doFindDeviceActionTypeIds";
        logInfo(methodName, "...");

        Assert.notEmpty(deviceIds, "deviceIds is empty");

        Aggregation agg = newAggregation(match(Criteria.where("deviceId").in(deviceIds)), group("deviceActionTypeId"));

        AggregationResults<TelemetryStat> groupResults = getOperations().aggregate(agg, DeviceEvent.COLLECTION_NAME,
                TelemetryStat.class);
        return groupResults.getMappedResults().stream().map(stat -> stat.getName()).collect(Collectors.toList());
    }

    private List<Criteria> buildCriteria(DeviceEventSearchParams params) {
        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
            criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
            criteria = addCriteria(criteria, "deviceActionTypeId", params.getDeviceActionTypeIds());
            criteria = addCriteria(criteria, "status", params.getStatuses());
            criteria = addCriteria(criteria, "createdDate", params.getCreatedDateFrom(), params.getCreatedDateTo());
        }
        return criteria;
    }
}