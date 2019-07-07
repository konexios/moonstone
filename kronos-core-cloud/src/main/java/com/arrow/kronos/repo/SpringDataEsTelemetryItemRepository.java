package com.arrow.kronos.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.EsTelemetryItem;

@Repository
public interface SpringDataEsTelemetryItemRepository
        extends ElasticsearchRepository<EsTelemetryItem, String>, SpringDataEsTelemetryItemRepositoryExtension {

	int deleteByApplicationId(String applicationId);

	int deleteByDeviceId(String deviceId);

	int deleteByApplicationIdAndTimestampGreaterThanEqualAndTimestampLessThanEqual(String applicationId,
	        long fromTimestamp, long toTimestamp);

	int deleteByDeviceIdAndTimestampGreaterThanEqualAndTimestampLessThanEqual(String deviceId, long fromTimestamp,
	        long toTimestamp);

	/**
	 * Remove telemetry items older than provided timestamp
	 * @param timestamp
	 * @return number of items deleted
	 */
	long deleteByTimestampLessThan(long timestamp);
}
