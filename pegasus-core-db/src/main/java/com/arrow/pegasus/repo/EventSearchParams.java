package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class EventSearchParams extends AuditableDocumentSearchParams {
	private static final long serialVersionUID = -7467150195442967029L;

	private Set<String> applicationIds;
	private Set<String> names;
	private Set<String> objectIds;
	private Instant createdDateFrom;
	private Instant createdDateTo;

	public EventSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = addValues(this.applicationIds, applicationIds);
		return this;
	}

	public EventSearchParams addNames(String... names) {
		this.names = addValues(this.names, names);
		return this;
	}

	public EventSearchParams addObjectIds(String... objectIds) {
		this.objectIds = addValues(this.objectIds, objectIds);
		return this;
	}

	public EventSearchParams addCreatedDateFrom(Instant createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
		return this;
	}

	public EventSearchParams addCreatedDateTo(Instant createdDateTo) {
		this.createdDateTo = createdDateTo;
		return this;
	}

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public Set<String> getNames() {
		return names;
	}

	public Set<String> getObjectIds() {
		return objectIds;
	}

	public Instant getCreatedDateFrom() {
		return createdDateFrom;
	}

	public Instant getCreatedDateTo() {
		return createdDateTo;
	}
}
