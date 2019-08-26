package com.arrow.kronos.repo;

import java.util.List;

import org.elasticsearch.search.sort.SortOrder;

import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.TelemetryStat;

import moonstone.acn.client.model.TelemetryItemType;

public interface SpringDataEsTelemetryItemRepositoryExtension {

    EsTelemetryItem index(EsTelemetryItem item);

    void bulkIndex(List<EsTelemetryItem> items);

    List<EsTelemetryItem> findAllTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
            long toTimestamp, SortOrder sortOrder);

    List<EsTelemetryItem> findAllTelemetryItems(EsTelemetryItemSearchParams params, SortOrder sortOrder);

    List<EsTelemetryItem> findAllTelemetryItems(EsTelemetryItemSearchParams params);

    List<EsTelemetryItem> findTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
            long toTimestamp, int hitSize, SortOrder sortOrder);

    List<EsTelemetryItem> findTelemetryItems(EsTelemetryItemSearchParams params, int hitSize, SortOrder sortOrder);

    EsTelemetryItem findLastTelemetryItem(String deviceId, String telemetryName, long fromTimestamp, long toTimestamp);

    List<EsTelemetryItem> findLastTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
            long toTimestamp);

    List<EsTelemetryItem> findLastTelemetryItems(EsTelemetryItemSearchParams params);

    List<EsTelemetryItem> findLastTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
            long toTimestamp, int hitSize);

    List<EsTelemetryItem> findLastTelemetryItems(EsTelemetryItemSearchParams params, int hitSize);

    long findTelemetryItemCount(String deviceId, String telemetryName, long fromTimestamp, long toTimestamp);

    long findTelemetryItemCount(EsTelemetryItemSearchParams params);

    TelemetryStat findAvgTelemetryItem(String deviceId, String telemetryName, TelemetryItemType telemetryItemType,
            long fromTimestamp, long toTimestamp);

    TelemetryStat findMinTelemetryItem(String deviceId, String telemetryName, TelemetryItemType telemetryItemType,
            long fromTimestamp, long toTimestamp);

    TelemetryStat findMaxTelemetryItem(String deviceId, String telemetryName, TelemetryItemType telemetryItemType,
            long fromTimestamp, long toTimestamp);

    boolean strTelemetryItemExists(String deviceId, String telemetryName, String telemetryValue);

    void remove(String applicationId);

    void remove(String applicationId, String deviceId);

    void remove(String applicationId, String deviceId, long fromTimestamp, long toTimestamp);

    void remove(String applicationId, String deviceId, long fromTimestamp, long toTimestamp, long scrollTimeout,
            int scrollSize);
}
