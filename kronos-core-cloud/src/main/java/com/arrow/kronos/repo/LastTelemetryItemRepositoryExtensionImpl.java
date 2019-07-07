package com.arrow.kronos.repo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.kronos.data.LastTelemetryCreated;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class LastTelemetryItemRepositoryExtensionImpl extends RepositoryExtensionAbstract<LastTelemetryItem>
        implements LastTelemetryItemRepositoryExtension {

	public LastTelemetryItemRepositoryExtensionImpl() {
		super(LastTelemetryItem.class);
	}

	@Override
	public Page<LastTelemetryItem> findLastTelemetryItems(Pageable pageable, LastTelemetryItemSearchParams params) {
		String methodName = "findLastTelemetryItems";
		logDebug(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>(4);
		if (params != null) {
			criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
			criteria = addCriteria(criteria, "telemetryIds", params.getTelemetryIds());
			criteria = addCriteria(criteria, "name", params.getNames());
			criteria = addCriteria(criteria, "type", params.getTypes());
		}

		return doProcessQuery(pageable, criteria);
	}

	@Override
	public List<TelemetryStat> findMaxLastTelemetryItemTimestamps(String... deviceId) {

		String method = "findMaxLastTelemetryItemTimestamps";
		logDebug(method, "...");

		Assert.notEmpty(deviceId, "deviceId is empty");

		// @formatter:off
		Aggregation agg = newAggregation(
			match(Criteria.where("deviceId").in((Object[])deviceId)),
			// do not get items with timestamp in the future
			// match(Criteria.where("timestamp").lte(Instant.now().toEpochMilli())),
			group("deviceId").max("timestamp").as("value")
		);
		// @formatter:on

		AggregationResults<TelemetryStat> groupResults = getOperations().aggregate(agg,
		        LastTelemetryItem.COLLECTION_NAME, TelemetryStat.class);

		logDebug(method, "deviceId: %s, result: %d", String.join(",", deviceId),
		        groupResults.getMappedResults().size());

		return groupResults.getMappedResults().isEmpty() ? null : groupResults.getMappedResults();
	}

	@Override
	public List<LastTelemetryCreated> findMaxLastTelemetryItemCreatedDates(String... deviceId) {

		String method = "findMaxLastTelemetryItemCreatedDates";
		logDebug(method, "...");

		Assert.notEmpty(deviceId, "deviceId is empty");

		// @formatter:off
		Aggregation agg = newAggregation(
			match(Criteria.where("deviceId").in((Object[])deviceId)),
			group("deviceId").max("createdDate").as("value")
		);
		// @formatter:on

		AggregationResults<LastTelemetryCreated> groupResults = getOperations().aggregate(agg,
		        LastTelemetryItem.COLLECTION_NAME, LastTelemetryCreated.class);

		logDebug(method, "deviceId: %s, result: %d", String.join(",", deviceId),
		        groupResults.getMappedResults().size());

		return groupResults.getMappedResults().isEmpty() ? null : groupResults.getMappedResults();
	}
}
