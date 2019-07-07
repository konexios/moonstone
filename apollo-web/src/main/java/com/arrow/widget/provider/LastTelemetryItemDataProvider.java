package com.arrow.widget.provider;

import java.util.List;

import org.springframework.data.domain.Page;

import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.repo.LastTelemetryItemSearchParams;

public interface LastTelemetryItemDataProvider {

	List<LastTelemetryItem> findByDeviceId(String deviceId);

	Page<LastTelemetryItem> findLastTelemetryItems(LastTelemetryItemSearchParams params);

	LastTelemetryItem findLastTelemetryItem(LastTelemetryItemSearchParams params);
}