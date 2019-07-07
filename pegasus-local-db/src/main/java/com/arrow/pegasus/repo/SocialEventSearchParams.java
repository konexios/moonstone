package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class SocialEventSearchParams extends AuditableDocumentSearchParams {

	private static final long serialVersionUID = -6934149463032661005L;

	private String name;
	private Instant startDateFrom;
	private Instant startDateTo;
	private Instant endDateFrom;
	private Instant endDateTo;
	private Instant createdDateFrom;
	private Instant createdDateTo;
	private Set<String> zoneIds;

	public String getName() {
		return name;
	}

	public SocialEventSearchParams setName(String name) {
		this.name = name;
		return this;
	}

	public Instant getStartDateFrom() {
		return startDateFrom;
	}

	public void setStartDateFrom(Instant startDateFrom) {
		this.startDateFrom = startDateFrom;
	}

	public Instant getStartDateTo() {
		return startDateTo;
	}

	public void setStartDateTo(Instant startDateTo) {
		this.startDateTo = startDateTo;
	}

	public Instant getEndDateFrom() {
		return endDateFrom;
	}

	public void setEndDateFrom(Instant endDateFrom) {
		this.endDateFrom = endDateFrom;
	}

	public Instant getEndDateTo() {
		return endDateTo;
	}

	public void setEndDateTo(Instant endDateTo) {
		this.endDateTo = endDateTo;
	}

	public Instant getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Instant createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public Instant getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(Instant createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public Set<String> getZoneIds() {
		return zoneIds;
	}

	public SocialEventSearchParams addZoneIds(String... zoneIds) {
		this.zoneIds = super.addValues(this.zoneIds, zoneIds);
		return this;
	}
}
