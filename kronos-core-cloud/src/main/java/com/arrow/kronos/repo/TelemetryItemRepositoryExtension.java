package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface TelemetryItemRepositoryExtension extends RepositoryExtension<TelemetryItem> {
	public List<TelemetryItem> findTelemetryItems(String deviceId, long fromTimestamp, long toTimestamp,
			Direction direction);

	public List<TelemetryItem> findTelemetryItems(String deviceId, String telemetryName, long fromTimestamp,
			long toTimestamp, Direction direction);

	public List<TelemetryItem> findTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp, Direction direction);

	public List<TelemetryItem> findTelemetryItems(String[] deviceIds, String[] telemetryNames, long fromTimestamp,
			long toTimestamp);

	public TelemetryItem findLastTelemetryItem(String deviceId, String telemetryName, long fromTimestamp,
			long toTimestamp);

	public List<TelemetryItem> findLastTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
			long toTimestamp);

	public long findTelemetryItemCount(String deviceId, String telemetryName);

	public long findTelemetryItemCount(String deviceId, String telemetryName, long fromTimestamp, long toTimestamp);

	public long findTelemetryItemCount(String deviceId, long fromTimestamp, long toTimestamp);

	public TelemetryStat findMinTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp);

	public TelemetryStat findMaxTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp);

	public TelemetryStat findAvgTelemetryItem(String deviceId, String telemetryName,
			TelemetryItemType telemetryItemType, long fromTimestamp, long toTimestamp);

	public Page<TelemetryItem> doFindTelemetryItems(Pageable pageRequest, TelemetryItemSearchParams params);

	public List<TelemetryItem> findTelemetryItems(TelemetryItemSearchParams params, Direction direction);

	public long removeByApplicationIdAndTimestamps(String applicationId, long fromTimestamp, long toTimestamp);

	public long removeByDeviceIdAndTimestamps(String deviceId, long fromTimestamp, long toTimestamp);

	public long removeByTimestamps(long fromTimestamp, long toTimestamp);
}
