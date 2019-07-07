package com.arrow.selene.data;

public class BaseEntity extends EntityAbstract {
	private static final long serialVersionUID = -317110276809777310L;

	private boolean enabled = true;
	private long createdTs;
	private long modifiedTs;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(long createdTs) {
		this.createdTs = createdTs;
	}

	public long getModifiedTs() {
		return modifiedTs;
	}

	public void setModifiedTs(long modifiedTs) {
		this.modifiedTs = modifiedTs;
	}
}
