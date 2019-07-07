package com.arrow.pegasus.repo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.LastLogin;
import com.arrow.pegasus.repo.params.NestedPropertySearchParam;

public class AuditLogRepositoryExtensionImpl extends RepositoryExtensionAbstract<AuditLog>
        implements AuditLogRepositoryExtension {

	public AuditLogRepositoryExtensionImpl() {
		super(AuditLog.class);
	}

	@Override
	public Page<AuditLog> findAuditLogs(Pageable pageable, String[] productName, String[] applicationId, String[] type,
	        String[] objectId, String[] relatedId, Instant createdDateFrom, Instant createdDateTo, String[] createdBy) {

		String methodName = "findAuditLogs";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();

		// productName
		criteria = addCriteria(criteria, "productName", productName);

		// applicationId
		criteria = addCriteria(criteria, "applicationId", applicationId);

		// type
		criteria = addCriteria(criteria, "type", type);

		// objectId
		criteria = addCriteria(criteria, "objectId", objectId);

		// relatedIds
		criteria = addCriteria(criteria, "relatedIds", relatedId);

		// created date range
		criteria = addCriteria(criteria, "createdDate", createdDateFrom, createdDateTo);

		// createdBy
		criteria = addCriteria(criteria, "createdBy", createdBy);

		return doProcessQuery(pageable, criteria);
	}

	@Override
	public Page<AuditLog> findAuditLogs(Pageable pageable, AuditLogSearchParams params) {
		String methodName = "findAuditLogs";
		logInfo(methodName, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<AuditLog> findAuditLogs(AuditLogSearchParams params) {
		String methodName = "findAuditLogs";
		logInfo(methodName, "...");

		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public long findAuditLogCount(AuditLogSearchParams params) {
		String methodName = "findAuditLogCount";
		logInfo(methodName, "...");
		List<Criteria> criteria = buildCriteria(params);
		Query query = doProcessCriteria(criteria);
		return doCount(query);
	}

	@Override
	public List<DistinctCountResult> countDistinctProperty(AuditLogSearchParams params, String property) {
		String method = "countDistinctProperty";
		logInfo(method, "...");
		List<DistinctCountResult> result = doDistinctCount(AuditLog.COLLECTION_NAME, buildCriteria(params), property);
		for (DistinctCountResult item : result) {
			logDebug(method, "---> name: %s, count: %d", item.getName(), item.getCount());
		}
		return result;
	}

	public LastLogin findLastLogin(String userId, String applicationId, String productName) {
		Assert.hasText(userId, "userId is empty");
		// Assert.hasText(applicationId, "applicationId is empty");
		// Assert.hasText(productName, "productName is empty");

		String methodName = "findLastLogin";
		logInfo(methodName, "...");

		// @formatter:off
		Criteria matchOperation = Criteria.where("objectId").is(userId);
		if (!StringUtils.isEmpty(applicationId))
			matchOperation.and("applicationId").is(applicationId);
		if (!StringUtils.isEmpty(productName))
			matchOperation.and("productName").is(productName);

		Aggregation agg = newAggregation(
			match(matchOperation),
			group("objectId").max("createdDate").as("lastLogin")
		);
		// @formatter:on

		AggregationResults<LastLogin> groupResults = getOperations().aggregate(agg, AuditLog.COLLECTION_NAME,
		        LastLogin.class);

		logInfo(methodName, "userId: %s, applicationId: %s, productName: %s, result: %d", userId, applicationId,
		        productName, groupResults.getMappedResults().size());

		return groupResults.getMappedResults().isEmpty() ? null : groupResults.getMappedResults().get(0);
	}

	private List<Criteria> buildCriteria(AuditLogSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "productName", params.getProductNames());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "type", params.getTypes());
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
			criteria = addCriteria(criteria, "createdDate", params.getCreatedDateFrom(), params.getCreatedDateTo());
			criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());

			if (params.getNestedProperties() != null)
				for (NestedPropertySearchParam property : params.getNestedProperties())
					criteria = addCriteria(criteria, property.getPropertyName(), property.getPropertyValues());
		}
		return criteria;
	}
}
