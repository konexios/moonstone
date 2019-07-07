package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.pegasus.data.event.Event;

public class EventRepositoryExtensionImpl extends RepositoryExtensionAbstract<Event>
		implements EventRepositoryExtension {

	public EventRepositoryExtensionImpl() {
		super(Event.class);
	}

	public long deleteBefore(Instant time) {
		return getOperations().remove(new Query(Criteria.where("createdDate").lt(time)), Event.class).getDeletedCount();
	}

	@Override
	public Page<Event> findEvents(Pageable pageable, EventSearchParams params) {
		String methodName = "findAuditLogs";
		logInfo(methodName, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	private List<Criteria> buildCriteria(EventSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "name", params.getNames());
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
			criteria = addCriteria(criteria, "createdDate", params.getCreatedDateFrom(), params.getCreatedDateTo());
			criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		}
		return criteria;
	}
}
