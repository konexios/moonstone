package com.arrow.pegasus.data.heartbeat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;

import com.arrow.pegasus.data.DocumentAbstract;

public abstract class HeartbeatAbstract extends DocumentAbstract {
	private static final long serialVersionUID = 3530738522166655308L;

	@NotNull
	private HeartbeatObjectType objectType;
	@NotBlank
	@Indexed(unique = true)
	private String objectId;
	private long timestamp;

	public HeartbeatObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(HeartbeatObjectType objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
