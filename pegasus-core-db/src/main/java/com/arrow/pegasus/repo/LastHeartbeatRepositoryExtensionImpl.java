package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.data.heartbeat.LastHeartbeat;

public class LastHeartbeatRepositoryExtensionImpl extends RepositoryExtensionAbstract<LastHeartbeat>
        implements LastHeartbeatRepositoryExtension {

	public LastHeartbeatRepositoryExtensionImpl() {
		super(LastHeartbeat.class);
	}

	@Override
	public Page<LastHeartbeat> findLastHeartbeats(Pageable pageable, LastHeartbeatSearchParams params) {
		String methodName = "findLastHeartbeats";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "objectType", params.getObjectTypes());
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
		}

		return doProcessQuery(pageable, criteria);
	}

	public List<LastHeartbeat> findLastHeartbeats(LastHeartbeatSearchParams params) {
		String methodName = "findLastHeartbeats";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "objectType", params.getObjectTypes());
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
		}

		return doProcessQuery(criteria);
	}
}
