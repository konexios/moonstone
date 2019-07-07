package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;
import com.arrow.pegasus.repo.params.NestedPropertySearchParam;

public class AuditLogSearchParams extends AuditableDocumentSearchParams {

	private static final long serialVersionUID = -7566518355561212133L;

	private Set<String> productNames;
	private Set<String> applicationIds;
	private Set<String> types;
	private Set<String> objectIds;
	private Instant createdDateFrom;
	private Instant createdDateTo;
	private List<NestedPropertySearchParam> nestedProperties;

	public Set<String> getProductNames() {
		return productNames;
	}

	public AuditLogSearchParams addProductNames(String... productNames) {
		this.productNames = addValues(this.productNames, productNames);
		return this;
	}

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public AuditLogSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = addValues(this.applicationIds, applicationIds);
		return this;
	}

	public Set<String> getTypes() {
		return types;
	}

	public AuditLogSearchParams addTypes(String... types) {
		this.types = addValues(this.types, types);
		return this;
	}

	public Set<String> getObjectIds() {
		return objectIds;
	}

	public AuditLogSearchParams addObjectIds(String... objectIds) {
		this.objectIds = addValues(this.objectIds, objectIds);
		return this;
	}

	public Instant getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Instant createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public AuditLogSearchParams addCreatedDateFrom(Instant createdDateFrom) {
		setCreatedDateFrom(createdDateFrom);
		return this;
	}

	public Instant getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(Instant createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public AuditLogSearchParams addCreatedDateTo(Instant createdDateTo) {
		setCreatedDateTo(createdDateTo);
		return this;
	}

	public List<NestedPropertySearchParam> getNestedProperties() {
		return nestedProperties;
	}

	public AuditLogSearchParams addNestedProperties(NestedPropertySearchParam... nestedProperties) {
		if (nestedProperties != null) {
			if (this.nestedProperties == null)
				this.nestedProperties = new ArrayList<>();

			for (NestedPropertySearchParam nestedProperty : nestedProperties)
				if (nestedProperty != null)
					this.nestedProperties.add(nestedProperty);
		}

		return this;
	}
}
