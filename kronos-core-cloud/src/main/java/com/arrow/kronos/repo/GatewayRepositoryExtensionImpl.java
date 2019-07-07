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

import com.arrow.kronos.data.DocumentId;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.mongodb.client.DistinctIterable;

public class GatewayRepositoryExtensionImpl extends RepositoryExtensionAbstract<Gateway>
        implements GatewayRepositoryExtension {

    public GatewayRepositoryExtensionImpl() {
        super(Gateway.class);
    }

    @Override
    public List<Gateway> doFindByApplicationIdAndGatewayIds(String applicationId, String... gatewayIds) {
        Query query = new Query(Criteria.where("applicationId").is(applicationId).and("id").in((Object[]) gatewayIds));
        return getOperations().find(query, Gateway.class);
    }

    public Page<Gateway> findGateways(Pageable pageable, GatewaySearchParams params) {
        return doProcessQuery(pageable, buildCriteria(params));
    }

    @Override
    public List<Gateway> findGateways(GatewaySearchParams params) {
        return doProcessQuery(buildCriteria(params));
    }

    public List<String> findAggregatedField(String applicationId, String fieldName) {
        return findAggregatedFieldIds(applicationId, fieldName);
    }

    public List<String> findAggregatedOsNames(String applicationId) {
        return findAggregatedFieldIds(applicationId, "osName");
    }

    public List<String> findAggregatedSoftwareNames(String applicationId) {
        return findAggregatedFieldIds(applicationId, "softwareName");
    }

    public List<String> findAggregatedSoftwareVersions(String applicationId) {
        return findAggregatedFieldIds(applicationId, "softwareVersion");
    }

    public List<String> findAggregatedField(String applicationId, String fieldName, String userId) {
        return findAggregatedFieldIds(applicationId, userId, fieldName);
    }

    public List<String> findAggregatedOsNames(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "osName");
    }

    public List<String> findAggregatedSoftwareNames(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "softwareName");
    }

    public List<String> findAggregatedSoftwareVersions(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "softwareVersion");
    }

    @SuppressWarnings("unchecked")
    private List<String> findAggregatedFieldIds(String applicationId, String userId, String field) {

        Query query = new Query(Criteria.where("applicationId").is(applicationId).and("userId").is(userId));
        DistinctIterable<String> result = getOperations()
                .getCollection(getOperations().getCollectionName(Gateway.class))
                .distinct(field, query.getQueryObject(), String.class);
        return IteratorUtils.toList(result.iterator());
    }

    public long findGatewayCount(GatewaySearchParams params) {
        String methodName = "findGatewayCount";
        logInfo(methodName, "...");
        List<Criteria> criteria = buildCriteria(params);
        return doCount(doProcessCriteria(criteria));
    }

    @Override
    public List<TelemetryStat> countGatewaysByType(String[] gatewayTypeIds, boolean enabled) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("deviceTypeId").in((Object[]) gatewayTypeIds).and("enabled").is(enabled)),
                group("deviceTypeId").count().as("value"));

        return getOperations().aggregate(aggregation, Gateway.class, TelemetryStat.class).getMappedResults();
    }

    @Override
    public List<String> findGatewayIds(GatewaySearchParams params, Sort sort) {
        Query query = this.doProcessCriteria(buildCriteria(params)).with(sort);
        query.fields().include("id");
        return getOperations().find(query, DocumentId.class, getOperations().getCollectionName(getDocumentClass()))
                .stream().map(docId -> docId.getId()).collect(Collectors.toList());
    }

    @Override
    public List<String> findAggregatedNodeIds(String applicationId, String userId) {
        return findAggregatedFieldIds(applicationId, userId, "nodeId");
    }

    @SuppressWarnings("unchecked")
    private List<String> findAggregatedFieldIds(String applicationId, String field) {

        Query query = new Query(Criteria.where("applicationId").is(applicationId));
        DistinctIterable<String> result = getOperations()
                .getCollection(getOperations().getCollectionName(Gateway.class))
                .distinct(field, query.getQueryObject(), String.class);
        return IteratorUtils.toList(result.iterator());
    }

    protected List<Criteria> buildCriteria(GatewaySearchParams params) {
        String method = "buildCriteria";
        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
            criteria = addCriteria(criteria, "hid", params.getHids());
            criteria = addCriteria(criteria, "uid", params.getUids());
            criteria = addCriteria(criteria, "userId", params.getUserIds());
            criteria = addCriteria(criteria, "type", params.getGatewayTypes());
            criteria = addCriteria(criteria, "deviceTypeId", params.getDeviceTypeIds());
            criteria = addCriteria(criteria, "nodeId", params.getNodeIds());
            criteria = addCriteria(criteria, "osName", params.getOsNames());
            criteria = addCriteria(criteria, "softwareName", params.getSoftwareNames());
            criteria = addCriteria(criteria, "softwareVersion", params.getSoftwareVersions());
            criteria = addCriteria(criteria, "softwareReleaseId", params.getSoftwareReleaseIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
            if (params.getCreatedBefore() != null && params.getCreatedAfter() != null) {
                logInfo(method, "createdAfter: %s, createdBefore: %s", params.getCreatedAfter(),
                        params.getCreatedBefore());
                criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), params.getCreatedBefore());
            } else if (params.getCreatedBefore() != null) {
                criteria = addCriteria(criteria, "createdDate", null, params.getCreatedBefore());
            } else if (params.getCreatedAfter() != null) {
                criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), null);
            }
            if (params.getUpdatedBefore() != null && params.getUpdatedAfter() != null) {
                logInfo(method, "updatedAfter: %s, updatedBefore: %s", params.getUpdatedAfter(),
                        params.getUpdatedBefore());
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
            criteria = addCriteria(criteria, "id", params.getIds());
        }
        return criteria;
    }

    public List<Gateway> findByApplicationIdAndDeviceTypeIdAndEnabled(String applicationId, String[] deviceTypeIds,
            boolean enabled) {
        String methodName = "findGateways";
        logInfo(methodName, "...");
        List<Criteria> criteria = new ArrayList<Criteria>();
        criteria = addCriteria(criteria, "applicationId", applicationId);
        criteria = addCriteria(criteria, "deviceTypeId", deviceTypeIds);
        criteria = addCriteria(criteria, "enabled", enabled);

        Query query = doProcessCriteria(criteria);

        return doFind(query);
    }
}
