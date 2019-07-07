package com.arrow.widget.provider;

import java.util.List;

import org.elasticsearch.search.sort.SortOrder;

import com.arrow.kronos.data.EsTelemetryItem;

public interface EsTelemetryItemDataProvider {

    public List<EsTelemetryItem> findAllTelemetryItems(String deviceId, String[] telemetryNames, long fromTimestamp,
            long toTimestamp, SortOrder sortOrder);
}