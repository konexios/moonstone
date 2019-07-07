package com.arrow.kronos.repo;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.DeviceEvent;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface DeviceEventRepositoryExtension extends RepositoryExtension<DeviceEvent> {
	public long incrementCounter(String id);

	public List<DeviceEvent> findDeviceEvents(DeviceEventSearchParams params);

	public Page<DeviceEvent> findDeviceEvents(Pageable pageable, DeviceEventSearchParams params);

	public long findDeviceEventCount(String deviceId, Instant fromTimestamp, Instant toTimestamp);

	List<String> doFindDeviceActionTypeIds(Collection<String> deviceIds);
}