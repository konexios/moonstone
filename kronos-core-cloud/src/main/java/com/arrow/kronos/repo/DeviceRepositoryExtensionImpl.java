package com.arrow.kronos.repo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DocumentId;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.mongodb.client.DistinctIterable;

public class DeviceRepositoryExtensionImpl extends RepositoryExtensionAbstract<Device>
        implements DeviceRepositoryExtension {
    public DeviceRepositoryExtensionImpl() {
        super(Device.class);
    }

    @Override
    public Page<Device> doFindDevices(Pageable pageable, DeviceSearchParams params) {
        return doProcessQuery(pageable, buildCriteria(params));
    }

    @Override
    public List<Device> doFindAllDevices(DeviceSearchParams params) {
        return doProcessQuery(buildCriteria(params));
    }

    @Override
    public List<String> doFindAggregatedDeviceTypeIds(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "deviceTypeId");
    }

    @Override
    public List<String> doFindAggregatedGatewayIds(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "gatewayId");
    }

    @Override
    public List<String> doFindAggregatedNodeIds(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "nodeId");
    }

    @SuppressWarnings("unchecked")
    private List<String> findAggregatedFieldIds(String applicationId, String userId, String field) {

        Query query = new Query(Criteria.where("applicationId").is(applicationId).and("userId").is(userId));
        DistinctIterable<String> result = getOperations().getCollection(getOperations().getCollectionName(Device.class))
                .distinct(field, query.getQueryObject(), String.class);
        return IteratorUtils.toList(result.iterator());
    }

    public List<Device> doFindByApplicationIdAndDeviceTypeIdAndEnabled(String applicationId, String[] deviceTypeIds,
            boolean enabled) {
        String methodName = "findDevices";
        logInfo(methodName, "...");
        List<Criteria> criteria = new ArrayList<Criteria>();
        criteria = addCriteria(criteria, "applicationId", applicationId);
        criteria = addCriteria(criteria, "deviceTypeId", deviceTypeIds);
        criteria = addCriteria(criteria, "enabled", enabled);

        Query query = doProcessCriteria(criteria);

        return doFind(query);
    }

    @Override
    public List<TelemetryStat> doCountDevicesByType(String[] deviceTypeIds, boolean enabled) {
        // @formatter:off
		Aggregation aggregation = newAggregation(
				match(Criteria.where("deviceTypeId").in((Object[])deviceTypeIds)
						.and("enabled").is(enabled)),
				group("deviceTypeId").count().as("value"));
		// @formatter:on

        return getOperations().aggregate(aggregation, Device.class, TelemetryStat.class).getMappedResults();
    }

    @Override
    public long doFindDeviceCount(DeviceSearchParams params) {
        String methodName = "findDeviceCount";
        logDebug(methodName, "...");
        List<Criteria> criteria = buildCriteria(params);
        return doCount(doProcessCriteria(criteria));
    }

    @Override
    public List<String> doFindDeviceIds(DeviceSearchParams params, Sort sort) {
        Query query = this.doProcessCriteria(buildCriteria(params)).with(sort);
        query.fields().include("id");
        return getOperations().find(query, DocumentId.class, getOperations().getCollectionName(getDocumentClass()))
                .stream().map(docId -> docId.getId()).collect(Collectors.toList());
    }

    @Override
    public List<Device> doFindAllDevicesWithNoTelemetryActions(List<String> deviceTypeIdsWithNoTelemetryActions) {

        List<Criteria> criteria = new ArrayList<Criteria>();
        criteria.add(Criteria.where("enabled").is(true));

        Criteria noTelemetryActionsInDevice = Criteria.where("actions")
                .elemMatch(Criteria.where("noTelemetry").is(true));

        if (deviceTypeIdsWithNoTelemetryActions != null && !deviceTypeIdsWithNoTelemetryActions.isEmpty()) {
            // include device type into criteria
            Criteria noTelemetryActionsCriteria = new Criteria();
            noTelemetryActionsCriteria.orOperator(noTelemetryActionsInDevice,
                    Criteria.where("deviceTypeId").in(deviceTypeIdsWithNoTelemetryActions));
            criteria.add(noTelemetryActionsCriteria);
        } else {
            // search only devices
            criteria.add(noTelemetryActionsInDevice);
        }

        Query query = doProcessCriteria(criteria);

        return doFind(query);
    }

    private List<Criteria> buildCriteria(DeviceSearchParams params) {
        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
            criteria = addCriteria(criteria, "hid", params.getHids());
            criteria = addCriteria(criteria, "deviceTypeId", params.getDeviceTypeIds());
            criteria = addCriteria(criteria, "gatewayId", params.getGatewayIds());
            criteria = addCriteria(criteria, "nodeId", params.getNodeIds());
            criteria = addCriteria(criteria, "partitionId", params.getPartitionIds());
            criteria = addCriteria(criteria, "uid", params.getUids());

            if (params.getUid() != null) {
                criteria.add(Criteria.where("uid").regex(params.getUid(), "i"));
            }

            criteria = addCriteria(criteria, "userId", params.getUserIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
            criteria = addCriteria(criteria, "tags", params.getTags());
            criteria = addCriteria(criteria, "softwareReleaseId", params.getSoftwareReleaseIds());
            criteria = addCriteria(criteria, "id", params.getIds());
            criteria = addCriteria(criteria, "softwareName", params.getSoftwareNames());
            criteria = addCriteria(criteria, "softwareVersion", params.getSoftwareVersions());

            if (params.getCreatedBefore() != null && params.getCreatedAfter() != null) {
                criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), params.getCreatedBefore());
            } else if (params.getCreatedBefore() != null) {
                criteria = addCriteria(criteria, "createdDate", null, params.getCreatedBefore());
            } else if (params.getCreatedAfter() != null) {
                criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), null);
            }

            if (params.getUpdatedBefore() != null && params.getUpdatedAfter() != null) {
                criteria = addCriteria(criteria, "lastModifiedDate", params.getUpdatedAfter(),
                        params.getUpdatedBefore());
            } else if (params.getUpdatedBefore() != null) {
                criteria = addCriteria(criteria, "lastModifiedDate", null, params.getUpdatedBefore());
            } else if (params.getUpdatedAfter() != null) {
                criteria = addCriteria(criteria, "lastModifiedDate", params.getUpdatedAfter(), null);
            }

            if (params.getSoftwareReleaseIdDefined() != null) {
                if (params.getSoftwareReleaseIdDefined()) {
                    criteria.add(Criteria.where("softwareReleaseId").ne(null));
                } else {
                    criteria.add(Criteria.where("softwareReleaseId").is(null));
                }
            }
        }
        return criteria;
    }

}
