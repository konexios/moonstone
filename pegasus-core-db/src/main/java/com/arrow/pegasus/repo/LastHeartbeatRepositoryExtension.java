package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.heartbeat.LastHeartbeat;

public interface LastHeartbeatRepositoryExtension extends RepositoryExtension<LastHeartbeat> {
	public Page<LastHeartbeat> findLastHeartbeats(Pageable pageable, LastHeartbeatSearchParams params);

	public List<LastHeartbeat> findLastHeartbeats(LastHeartbeatSearchParams params);
}
