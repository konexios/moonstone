package com.arrow.pegasus.repo;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.repo.params.SearchParamsAbstract;

public class HeartbeatSearchParams extends SearchParamsAbstract {

	private static final long serialVersionUID = 1554155321255744450L;

	private EnumSet<HeartbeatObjectType> objectTypes;
	private Set<String> objectIds;
	private long fromTimestamp;
	private long toTimestamp;

	public EnumSet<HeartbeatObjectType> getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(EnumSet<HeartbeatObjectType> objectTypes) {
		this.objectTypes = objectTypes;
	}

	public HeartbeatSearchParams addObjectTypes(HeartbeatObjectType... objectTypes) {
		if (objectTypes == null || objectTypes.length <= 0)
			return this;
		if (this.objectTypes == null) {
			this.objectTypes = EnumSet.noneOf(HeartbeatObjectType.class);
		}
		for (HeartbeatObjectType t : objectTypes) {
			this.objectTypes.add(t);
		}
		return this;
	}

	public Set<String> getObjectIds() {
		return objectIds;
	}

	public HeartbeatSearchParams addObjectIds(String... objectIds) {
		this.objectIds = super.addValues(this.objectIds, objectIds);
		return this;
	}

	public long getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(long fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public HeartbeatSearchParams addFromTimestamp(long fromTimestamp) {
		setFromTimestamp(fromTimestamp);
		return this;
	}

	public long getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	public HeartbeatSearchParams addToTimestamp(long toTimestamp) {
		setToTimestamp(toTimestamp);
		return this;
	}
}
