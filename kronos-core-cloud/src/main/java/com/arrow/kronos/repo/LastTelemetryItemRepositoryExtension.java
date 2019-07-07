package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.LastTelemetryCreated;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface LastTelemetryItemRepositoryExtension extends RepositoryExtension<LastTelemetryItem> {

	public Page<LastTelemetryItem> findLastTelemetryItems(Pageable pageable, LastTelemetryItemSearchParams params);
	
	public List<TelemetryStat> findMaxLastTelemetryItemTimestamps(String... deviceId);
	
	public List<LastTelemetryCreated> findMaxLastTelemetryItemCreatedDates(String... deviceId);
}
