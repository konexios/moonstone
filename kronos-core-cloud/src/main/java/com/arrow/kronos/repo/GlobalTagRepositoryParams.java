package com.arrow.kronos.repo;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.kronos.data.GlobalTagType;
import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class GlobalTagRepositoryParams extends AuditableDocumentSearchParams {

	private static final long serialVersionUID = 7988619102903005454L;

	private String name;
	private EnumSet<GlobalTagType> tagTypes;
	private Instant createdDateFrom;
	private Instant createdDateTo;
	private Set<String> objectTypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GlobalTagRepositoryParams addName(String name) {
		setName(name);
		return this;
	}

	public EnumSet<GlobalTagType> getTagTypes() {
		return tagTypes;
	}

	public GlobalTagRepositoryParams addTagTypes(EnumSet<GlobalTagType> types) {
		if (this.tagTypes == null) {
			this.tagTypes = types;
		} else {
			types.forEach(this.tagTypes::add);
		}
		return this;
	}

	public Instant getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Instant createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public GlobalTagRepositoryParams addCreatedDateFrom(Instant createdDateFrom) {
		setCreatedDateFrom(createdDateFrom);
		return this;
	}

	public Instant getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(Instant createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public GlobalTagRepositoryParams addCreatedDateTo(Instant createdDateTo) {
		setCreatedDateTo(createdDateTo);
		return this;
	}

	public Set<String> getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(Set<String> objectTypes) {
		this.objectTypes = objectTypes;
	}

	public GlobalTagRepositoryParams addObjectTypes(String... objectTypes) {
		this.objectTypes = addValues(this.objectTypes, objectTypes);
		return this;
	}
}
