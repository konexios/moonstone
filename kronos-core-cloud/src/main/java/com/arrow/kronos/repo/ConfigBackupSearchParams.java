package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.kronos.data.ConfigBackup;
import com.arrow.pegasus.repo.params.TsDocumentSearchParams;

public class ConfigBackupSearchParams extends TsDocumentSearchParams implements Serializable {

	private static final long serialVersionUID = -1228763003662657935L;

	private Set<String> objectIds;
	private EnumSet<ConfigBackup.Type> types;
	private Set<String> names;
	private Instant createdBefore;
	private Instant createdAfter;

	public Set<String> getObjectIds() {
		return objectIds;
	}

	public ConfigBackupSearchParams addObjectIds(String... objectIds) {
		this.objectIds = addValues(this.objectIds, objectIds);
		return this;
	}

	public EnumSet<ConfigBackup.Type> getTypes() {
		return types;
	}

	public ConfigBackupSearchParams addTypes(EnumSet<ConfigBackup.Type> types) {
		if (this.types == null) {
			this.types = types;
		} else if (types != null) {
			types.forEach(this.types::add);
		}
		return this;
	}

	public Set<String> getNames() {
		return names;
	}

	public ConfigBackupSearchParams addNames(String... names) {
		this.names = addValues(this.names, names);
		return this;
	}

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public ConfigBackupSearchParams addCreatedBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}

	public Instant getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Instant createdAfter) {
		this.createdAfter = createdAfter;
	}

	public ConfigBackupSearchParams addCreatedAfter(Instant createdAfter) {
		setCreatedAfter(createdAfter);
		return this;
	}
}