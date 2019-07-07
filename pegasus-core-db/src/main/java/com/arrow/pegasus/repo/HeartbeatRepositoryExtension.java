package com.arrow.pegasus.repo;

import com.arrow.pegasus.data.heartbeat.Heartbeat;

public interface HeartbeatRepositoryExtension extends RepositoryExtension<Heartbeat> {

	long findHeartbeatCount(HeartbeatSearchParams params);
}
