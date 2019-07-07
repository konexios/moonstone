package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.TelemetryStat;

public class SpringDataEsTelemetryItemRepositoryExtensionImpl extends EsRepositoryExtensionAbstract<EsTelemetryItem>
		implements SpringDataEsTelemetryItemRepositoryExtension {

	private static final Comparator<EsTelemetryItem> ASC_COMPARATOR = new Comparator<EsTelemetryItem>() {
		@Override
		public int compare(EsTelemetryItem o1, EsTelemetryItem o2) {
			long diff = o1.getTimestamp() - o2.getTimestamp();
			return diff < 0 ? -1 : (diff > 0 ? 1 : 0);
		}
	};

	private static final Comparator<EsTelemetryItem> DESC_COMPARATOR = new Comparator<EsTelemetryItem>() {
		@Override
		public int compare(EsTelemetryItem o1, EsTelemetryItem o2) {
			long diff = o2.getTimestamp() - o1.getTimestamp();
			return diff < 0 ? -1 : (diff > 0 ? 1 : 0);
		}
	};

	public SpringDataEsTelemetryItemRepositoryExtensionImpl() {
		super(EsTelemetryItem.class);
	}

	@Override
	public EsTelemetryItem index(EsTelemetryItem item) {
		Assert.notNull(item, "item is null");
		try {
			IndexQueryBuilder builder = new IndexQueryBuilder().withObject(item);
			getElasticsearchTemplate().index(builder.build());
			return item;
		} catch (Exception e) {
			throw new AcsLogicalException("Error indexing document", e);
		}
	}

	@Override
	public void bulkIndex(List<EsTelemetryItem> items) {
		Assert.notNull(items, "items is null");
		String method = "bulkIndex";
		if (!items.isEmpty()) {
			try {
				logDebug(method, "indexing %d items ...", items.size());
				IndexQueryBuilder builder = new IndexQueryBuilder();
				List<IndexQuery> queries = new ArrayList<>();
				items.forEach(item -> queries.add(builder.withObject(item).build()));
				getElasticsearchTemplate().bulkIndex(queries);
			} catch (Exception e) {
				throw new AcsLogicalException("Error indexing document", e);
			}
		}
	}

	@Override
	public List<EsTelemetryItem> findAllTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp, SortOrder sortOrder) {
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
				.addTelemetryNames(telemetryNames).addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp);
		return findAllTelemetryItems(params, sortOrder);
	}

	@Override
	public List<EsTelemetryItem> findAllTelemetryItems(EsTelemetryItemSearchParams params, SortOrder sortOrder) {
		String method = "findAllTelemetryItems";
		logDebug(method, "...");
		try {
			List<EsTelemetryItem> items = doScroll(buildSearchQuery(params));
			if (sortOrder != null) {
				items.sort(sortOrder == SortOrder.ASC ? ASC_COMPARATOR : DESC_COMPARATOR);
			}
			return items;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public List<EsTelemetryItem> findAllTelemetryItems(EsTelemetryItemSearchParams params) {
		return findAllTelemetryItems(params, null);
	}

	@Override
	public List<EsTelemetryItem> findTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp, int hitSize, SortOrder sortOrder) {
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
				.addTelemetryNames(telemetryNames).addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp);
		return findTelemetryItems(params, hitSize, sortOrder);
	}

	@Override
	public List<EsTelemetryItem> findTelemetryItems(EsTelemetryItemSearchParams params, int hitSize,
			SortOrder sortOrder) {
		Assert.isTrue(hitSize > 0, "invalid hitSize");
		String method = "findTelemetryItems";
		logDebug(method, "...");
		try {
			SearchQuery query = buildSearchQuery(params, sortOrder);
			query.setPageable(PageRequest.of(0, hitSize));
			return doSearch(query);
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public EsTelemetryItem findLastTelemetryItem(String deviceId, String telemetryName, long fromTimestamp,
			long toTimestamp) {
		String method = "findLastTelemetryItem";
		logDebug(method, "...");
		try {
			EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
					.addTelemetryNames(telemetryName).addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp);
			SearchQuery query = buildSearchQuery(params, SortOrder.DESC);
			query.setPageable(PageRequest.of(0, 1));
			List<EsTelemetryItem> items = doSearch(query);
			return items != null && !items.isEmpty() ? items.get(0) : null;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public List<EsTelemetryItem> findLastTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp) {
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
				.addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp).addTelemetryNames(telemetryNames);
		return findLastTelemetryItems(params);
	}

	public List<EsTelemetryItem> findLastTelemetryItems(EsTelemetryItemSearchParams params) {
		String method = "findLastTelemetryItems";
		logDebug(method, "...");
		try {
			Map<String, EsTelemetryItem> lastTelemetryItemMap = new HashMap<>();

			for (EsTelemetryItem item : findAllTelemetryItems(params, SortOrder.DESC)) {
				EsTelemetryItem foundItem = lastTelemetryItemMap.get(item.getName());
				if (foundItem == null) {
					lastTelemetryItemMap.put(item.getName(), item);
				}
			}
			return lastTelemetryItemMap.isEmpty() ? Collections.emptyList()
					: new ArrayList<>(lastTelemetryItemMap.values());
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public List<EsTelemetryItem> findLastTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp, int hitSize) {
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
				.addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp).addTelemetryNames(telemetryNames);
		return findLastTelemetryItems(params, hitSize);
	}

	@Override
	public List<EsTelemetryItem> findLastTelemetryItems(EsTelemetryItemSearchParams params, int hitSize) {
		String method = "findLastTelemetryItems";
		logDebug(method, "...");
		try {
			Map<String, EsTelemetryItem> lastTelemetryItemMap = new HashMap<>();

			for (EsTelemetryItem item : findTelemetryItems(params, hitSize, SortOrder.DESC)) {
				EsTelemetryItem foundItem = lastTelemetryItemMap.get(item.getName());
				if (foundItem == null) {
					lastTelemetryItemMap.put(item.getName(), item);
				}
			}
			return lastTelemetryItemMap.isEmpty() ? Collections.emptyList()
					: new ArrayList<>(lastTelemetryItemMap.values());
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public long findTelemetryItemCount(String deviceId, String telemetryName, long fromTimestamp, long toTimestamp) {
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
				.addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp).addTelemetryNames(telemetryName);
		return findTelemetryItemCount(params);
	}

	@Override
	public long findTelemetryItemCount(EsTelemetryItemSearchParams params) {
		String method = "findTelemetryItemCount";
		logDebug(method, "...");
		try {
			long count = doCount(buildSearchQuery(params), this.getDocumentClass());
			logDebug(method, "count: %s", count);
			return count;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public TelemetryStat findAvgTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp) {
		String method = "findAvgTelemetryItem";
		logDebug(method, "...");
		try {
			EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
					.addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp).addTelemetryNames(telemetryName);
			AvgAggregationBuilder avgBuilder = AggregationBuilders.avg("avg").field(telemetryItemType.getFieldName());
			Aggregations aggregations = doAggregation(buildAggregationQuery(params, avgBuilder));
			if (aggregations != null) {
				Avg aggregation = (Avg) aggregations.get("avg");
				if (aggregation != null) {
					return new TelemetryStat().withName(telemetryName)
							.withValue(String.valueOf(aggregation.getValue()));
				}
			}
			return null;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public TelemetryStat findMinTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp) {
		String method = "findMinTelemetryItem";
		logDebug(method, "...");
		try {
			EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
					.addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp).addTelemetryNames(telemetryName);
			MinAggregationBuilder minBuilder = AggregationBuilders.min("min").field(telemetryItemType.getFieldName());
			Aggregations aggregations = doAggregation(buildAggregationQuery(params, minBuilder));
			if (aggregations != null) {
				Min aggregation = (Min) aggregations.get("min");
				if (aggregation != null) {
					return new TelemetryStat().withName(telemetryName)
							.withValue(String.valueOf(aggregation.getValue()));
				}
			}
			return null;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public TelemetryStat findMaxTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp) {
		String method = "findMaxTelemetryItem";
		logDebug(method, "...");
		try {
			EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addDeviceId(deviceId)
					.addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp).addTelemetryNames(telemetryName);
			MaxAggregationBuilder maxBuilder = AggregationBuilders.max("max").field(telemetryItemType.getFieldName());
			Aggregations aggregations = doAggregation(buildAggregationQuery(params, maxBuilder));
			if (aggregations != null) {
				Max aggregation = (Max) aggregations.get("max");
				if (aggregation != null) {
					return new TelemetryStat().withName(telemetryName)
							.withValue(String.valueOf(aggregation.getValue()));
				}
			}
			return null;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public boolean strTelemetryItemExists(String deviceId, String telemetryName, String telemetryValue) {
		String method = "strTelemetryItemExists";
		logDebug(method, "...");
		try {
			BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("deviceId", deviceId))
					.must(QueryBuilders.matchQuery("name", telemetryName))
					.must(QueryBuilders.matchQuery("strValue", telemetryValue));
			NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(query);
			return doCount(queryBuilder.build(), getDocumentClass()) > 0;
		} catch (Exception e) {
			throw new AcsLogicalException("Error querying Elasticsearch", e);
		}
	}

	@Override
	public void remove(String applicationId) {
		remove(applicationId, null, -1, -1);
	}

	@Override
	public void remove(String applicationId, String deviceId) {
		remove(applicationId, deviceId, -1, -1);
	}

	@Override
	public void remove(String applicationId, String deviceId, long fromTimestamp, long toTimestamp) {
		String method = "remove";
		logDebug(method, "...");
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addApplicationId(applicationId)
				.addDeviceId(deviceId).addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp);
		DeleteQuery query = new DeleteQuery();
		query.setQuery(buildQuery(params));
		doDelete(query, getDocumentClass());
	}

	@Override
	public void remove(String applicationId, String deviceId, long fromTimestamp, long toTimestamp, long scrollTimeout,
			int scrollSize) {
		String method = "remove";
		logDebug(method, "...");
		EsTelemetryItemSearchParams params = new EsTelemetryItemSearchParams().addApplicationId(applicationId)
				.addDeviceId(deviceId).addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp);
		DeleteQuery query = new DeleteQuery();
		query.setQuery(buildQuery(params));
		doDelete(query, getDocumentClass());
	}

	private QueryBuilder buildQuery(EsTelemetryItemSearchParams params) {
		Assert.notNull(params, "params is null");

		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if (StringUtils.isNotEmpty(params.getApplicationId())) {
			query.must(QueryBuilders.matchQuery("applicationId", params.getApplicationId()));
		}
		if (StringUtils.isNotEmpty(params.getDeviceId())) {
			query.must(QueryBuilders.matchQuery("deviceId", params.getDeviceId()));
		}
		if (params.getTelemetryNames() != null) {
			BoolQueryBuilder nameQuery = QueryBuilders.boolQuery();
			for (String name : params.getTelemetryNames()) {
				nameQuery.should(QueryBuilders.matchQuery("name", name));
			}
			query.must(nameQuery);
		}
		Long fromTimestamp = params.getFromTimestamp();
		Long toTimestamp = params.getToTimestamp();
		if (fromTimestamp != null && fromTimestamp > 0 && toTimestamp != null && toTimestamp > 0) {
			query.must(QueryBuilders.rangeQuery("timestamp").gte(fromTimestamp).lte(toTimestamp));
		} else if (fromTimestamp != null && fromTimestamp > 0) {
			query.must(QueryBuilders.rangeQuery("timestamp").gte(fromTimestamp));
		} else if (toTimestamp != null && toTimestamp > 0) {
			query.must(QueryBuilders.rangeQuery("timestamp").lte(fromTimestamp));
		}
		return query;
	}

	private SearchQuery buildSearchQuery(EsTelemetryItemSearchParams params) {
		return buildSearchQuery(params, null);
	}

	private SearchQuery buildSearchQuery(EsTelemetryItemSearchParams params, SortOrder sortOrder) {
		String method = "buildSearchQuery";
		QueryBuilder query = buildQuery(params);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(query);
		if (sortOrder != null) {
			queryBuilder.withSort(SortBuilders.fieldSort("timestamp").order(sortOrder));
		}
		logDebug(method, "searchQuery: %s", query);
		return queryBuilder.build();
	}

	private SearchQuery buildAggregationQuery(EsTelemetryItemSearchParams params,
			AbstractAggregationBuilder<?> aggregationBuilder) {
		QueryBuilder query = buildQuery(params);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(query)
				.addAggregation(aggregationBuilder);
		return queryBuilder.build();
	}
}
