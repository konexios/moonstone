package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.arrow.kronos.data.Telemetry;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface TelemetryRepositoryExtension extends RepositoryExtension<Telemetry> {
	public Page<Telemetry> doFindTelemetries(PageRequest pageRequest, TelemetrySearchParams params);

	public List<Telemetry> findTelemetries(TelemetrySearchParams params, Direction direction);

	public long removeByApplicationIdAndTimestamps(String applicationId, long fromTimestamp, long toTimestamp);

	public long removeByDeviceIdAndTimestamps(String deviceId, long fromTimestamp, long toTimestamp);

	public long removeByTimestamps(long fromTimestamp, long toTimestamp);
}
