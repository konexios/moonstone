package com.arrow.kronos.repo;

//imports as static
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

import moonstone.acn.client.model.TelemetryItemType;

public class TelemetryItemRepositoryExtensionImpl extends RepositoryExtensionAbstract<TelemetryItem>
		implements TelemetryItemRepositoryExtension {
	public TelemetryItemRepositoryExtensionImpl() {
		super(TelemetryItem.class);
	}

	@Override
	public List<TelemetryItem> findTelemetryItems(String deviceId, long fromTimestamp, long toTimestamp,
			Direction direction) {

		String methodName = "findTelemetryItems";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("deviceId").is(deviceId));
		criteria.add(Criteria.where("timestamp").gte(fromTimestamp)
				.andOperator(Criteria.where("timestamp").lte(toTimestamp)));

		Query query = doProcessCriteria(criteria);
		query.with(new Sort(direction, "timestamp"));

		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);

		logInfo(methodName, "deviceId: %s, fromTimestamp: %d, toTimestamp: %d, result: %d", deviceId, fromTimestamp,
				toTimestamp, list.size());

		return list;
	}

	public List<TelemetryItem> findTelemetryItems(String deviceId, String telemetryName, long fromTimestamp,
			long toTimestamp, Direction direction) {
		String methodName = "findTelemetryItems";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("deviceId").is(deviceId));
		criteria.add(Criteria.where("name").is(telemetryName));
		criteria.add(Criteria.where("timestamp").gte(fromTimestamp)
				.andOperator(Criteria.where("timestamp").lte(toTimestamp)));

		Query query = doProcessCriteria(criteria);
		query.with(new Sort(direction, "timestamp"));

		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);

		logInfo(methodName, "deviceId: %s, telemetryName: %s, fromTimestamp: %d, toTimestamp: %d, result: %d", deviceId,
				telemetryName, fromTimestamp, toTimestamp, list.size());

		return list;
	}

	public List<TelemetryItem> findTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp, Direction direction) {
		String methodName = "findTelemetryItems";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("deviceId").is(deviceId));
		criteria = addCriteria(criteria, "name", telemetryNames);
		criteria.add(Criteria.where("timestamp").gte(fromTimestamp)
				.andOperator(Criteria.where("timestamp").lte(toTimestamp)));

		Query query = doProcessCriteria(criteria);
		query.with(new Sort(direction, "timestamp"));

		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);

		logInfo(methodName, "deviceId: %s, telemetryNames: %s, fromTimestamp: %d, toTimestamp: %d, result: %d",
				deviceId, telemetryNames, fromTimestamp, toTimestamp, list.size());

		return list;
	}

	public List<TelemetryItem> findTelemetryItems(String[] deviceIds, String[] telemetryNames, long fromTimestamp,
			long toTimestamp) {
		String methodName = "findTelemetryItems";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("timestamp").gte(fromTimestamp)
				.andOperator(Criteria.where("timestamp").lte(toTimestamp)));
		criteria = addCriteria(criteria, "deviceId", deviceIds);
		criteria = addCriteria(criteria, "name", telemetryNames);

		Query query = doProcessCriteria(criteria);
		query.withHint("timestamp");

		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);

		logInfo(methodName, "deviceIds size: %s, telemetryNames: %s, fromTimestamp: %d, toTimestamp: %d, result: %d",
				deviceIds.length, telemetryNames, fromTimestamp, toTimestamp, list.size());

		return list;
	}

	public TelemetryItem findLastTelemetryItem(String deviceId, String telemetryName, long fromTimestamp,
			long toTimestamp) {
		String methodName = "findLastTelemetryItem";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("deviceId").is(deviceId));
		criteria.add(Criteria.where("name").is(telemetryName));
		criteria.add(Criteria.where("timestamp").gte(fromTimestamp)
				.andOperator(Criteria.where("timestamp").lte(toTimestamp)));

		Query query = doProcessCriteria(criteria);
		query.with(new PageRequest(0, 1, new Sort(Direction.DESC, "timestamp")));

		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);

		logInfo(methodName, "deviceId: %s, telemetryName: %s, fromTimestamp: %d, toTimestamp: %d, result: %d", deviceId,
				telemetryName, fromTimestamp, toTimestamp, list.size());

		return list.isEmpty() ? null : list.get(0);
	}

	public List<TelemetryItem> findLastTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp) {
		String methodName = "findLastTelemetryItems";
		logInfo(methodName, "...");

		Map<String, TelemetryItem> lastTelemetryItemMap = new HashMap<>();

		for (TelemetryItem item : findTelemetryItems(deviceId, telemetryNames, fromTimestamp, toTimestamp,
				Direction.DESC)) {
			TelemetryItem foundTelemetryItem = lastTelemetryItemMap.get(item.getName());
			if (foundTelemetryItem == null)
				lastTelemetryItemMap.put(item.getName(), item);
		}

		return lastTelemetryItemMap.isEmpty() ? Collections.emptyList()
				: new ArrayList<>(lastTelemetryItemMap.values());
	}

	public long findTelemetryItemCount(String deviceId, String telemetryName) {

		String methodName = "findTelemetryItemCount";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<>();
		addCriteria(criteria, "deviceId", deviceId);
		addCriteria(criteria, "name", telemetryName);

		Query query = doProcessCriteria(criteria);

		return getOperations().count(query, getDocumentClass(), TelemetryItem.COLLECTION_NAME);
	}

	public long findTelemetryItemCount(String deviceId, String telemetryName, long fromTimestamp, long toTimestamp) {

		String methodName = "findTelemetryItemCount";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<>();
		addCriteria(criteria, "deviceId", deviceId);
		addCriteria(criteria, "name", telemetryName);
		addCriteria(criteria, "timestamp", fromTimestamp, toTimestamp);

		Query query = doProcessCriteria(criteria);

		return getOperations().count(query, getDocumentClass(), TelemetryItem.COLLECTION_NAME);
	}

	public long findTelemetryItemCount(String deviceId, long fromTimestamp, long toTimestamp) {
		String methodName = "findTelemetryItemCount";
		logInfo(methodName, "...");

		List<Criteria> criteria = new ArrayList<>();
		addCriteria(criteria, "deviceId", deviceId);
		addCriteria(criteria, "timestamp", fromTimestamp, toTimestamp);

		Query query = doProcessCriteria(criteria);

		return getOperations().count(query, getDocumentClass(), TelemetryItem.COLLECTION_NAME);
	}

	public TelemetryStat findMinTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp) {

		String methodName = "findMinTelemetryItem";
		logInfo(methodName, "...");

		Assert.hasText(deviceId, "deviceId is empty");
		Assert.hasText(telemetryName, "telemetryName is empty");
		Assert.notNull(telemetryItemType, "telemetryItemType is null");
		Assert.notNull(fromTimestamp, "fromTimestamp is null");
		Assert.notNull(toTimestamp, "toTimestamp is null");

		// @formatter:off
		Aggregation agg = newAggregation(
				match(Criteria.where("deviceId").is(deviceId).and("name").is(telemetryName)
						.andOperator(Criteria.where("timestamp").gte(fromTimestamp)
								.andOperator(Criteria.where("timestamp").lte(toTimestamp)))),
				group("name").min(telemetryItemType.getFieldName()).as("value"));
		// @formatter:on

		AggregationResults<TelemetryStat> groupResults = getOperations().aggregate(agg, TelemetryItem.COLLECTION_NAME,
				TelemetryStat.class);
		return groupResults.getMappedResults().isEmpty() ? null : groupResults.getMappedResults().get(0);
	}

	public TelemetryStat findMaxTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp) {

		String methodName = "findMaxTelemetryItem";
		logInfo(methodName, "...");

		Assert.hasText(deviceId, "deviceId is empty");
		Assert.hasText(telemetryName, "telemetryName is empty");
		Assert.notNull(telemetryItemType, "telemetryItemType is null");
		Assert.notNull(fromTimestamp, "fromTimestamp is null");
		Assert.notNull(toTimestamp, "toTimestamp is null");

		// @formatter:off
		Aggregation agg = newAggregation(
				match(Criteria.where("deviceId").is(deviceId).and("name").is(telemetryName)
						.andOperator(Criteria.where("timestamp").gte(fromTimestamp)
								.andOperator(Criteria.where("timestamp").lte(toTimestamp)))),
				group("name").max(telemetryItemType.getFieldName()).as("value"));
		// @formatter:on

		AggregationResults<TelemetryStat> groupResults = getOperations().aggregate(agg, TelemetryItem.COLLECTION_NAME,
				TelemetryStat.class);

		logInfo(methodName, "deviceId: %s, telemetryName: %s, fromTimestamp: %d, toTimestamp: %d, result: %d", deviceId,
				telemetryName, fromTimestamp, toTimestamp, groupResults.getMappedResults().size());

		return groupResults.getMappedResults().isEmpty() ? null : groupResults.getMappedResults().get(0);
	}

	public TelemetryStat findAvgTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp) {

		String methodName = "findAvgTelemetryItem";
		logInfo(methodName, "...");

		Assert.hasText(deviceId, "deviceId is empty");
		Assert.hasText(telemetryName, "telemetryName is empty");
		Assert.notNull(telemetryItemType, "telemetryItemType is null");
		Assert.notNull(fromTimestamp, "fromTimestamp is null");
		Assert.notNull(toTimestamp, "toTimestamp is null");

		// @formatter:off
		Aggregation agg = newAggregation(
				match(Criteria.where("deviceId").is(deviceId).and("name").is(telemetryName)
						.andOperator(Criteria.where("timestamp").gte(fromTimestamp)
								.andOperator(Criteria.where("timestamp").lte(toTimestamp)))),
				group("name").avg(telemetryItemType.getFieldName()).as("value"));
		// @formatter:on

		AggregationResults<TelemetryStat> groupResults = getOperations().aggregate(agg, TelemetryItem.COLLECTION_NAME,
				TelemetryStat.class);
		return groupResults.getMappedResults().isEmpty() ? null : groupResults.getMappedResults().get(0);
	}

	@Override
	public Page<TelemetryItem> doFindTelemetryItems(Pageable pageRequest, TelemetryItemSearchParams params) {
		String method = "doFindTelemetryItemsWithPartition";

		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "timestamp", params.getFromTimestamp(), params.getToTimestamp());
			criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
			criteria = addCriteria(criteria, "name", params.getNames());
		}
		Query query = doProcessCriteria(criteria);
		long count = getOperations().count(query, TelemetryItem.COLLECTION_NAME);
		query.with(pageRequest);
		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);
		logInfo(method, "total: %d, page: %d, limit: %d, skip: %d", count, list.size(), pageRequest.getPageSize(),
				pageRequest.getOffset());

		return new PageImpl<TelemetryItem>(list, pageRequest, count);
	}

	public List<TelemetryItem> findTelemetryItems(TelemetryItemSearchParams params, Direction direction) {
		String method = "findTelemetryItems";

		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "telemetryId", params.getTelemetryIds());
			criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
			criteria = addCriteria(criteria, "timestamp", params.getFromTimestamp(), params.getToTimestamp());
			criteria = addCriteria(criteria, "name", params.getNames());
		}
		Query query = doProcessCriteria(criteria);
		query.with(new Sort(direction, "timestamp"));
		List<TelemetryItem> list = getOperations().find(query, TelemetryItem.class, TelemetryItem.COLLECTION_NAME);

		logInfo(method, "total: %d", list.size());

		return list == null ? Collections.emptyList() : list;
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

	public long removeByTimestamps(long fromTimestamp, long toTimestamp) {
		String methodName = "removeByTimestamps";
		logInfo(methodName, "fromTs: %d,  toTs: %d", fromTimestamp, toTimestamp);
		List<Criteria> criteria = new ArrayList<>();
		addCriteria(criteria, "timestamp", fromTimestamp, toTimestamp);
		Query query = doProcessCriteria(criteria);
		return getOperations().remove(query, getDocumentClass()).getDeletedCount();
	}
}
