package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class SocialEventDeviceSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -2563939772039111582L;
	
	private Set<String> deviceTypeIds;
	private Instant createdBefore;
	private Instant createdAfter;
	private Instant updatedBefore;
	private Instant updatedAfter;
	private Set<String> macAddresses;

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public SocialEventDeviceSearchParams addCreatedBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}

	public Instant getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Instant createdAfter) {
		this.createdAfter = createdAfter;
	}

	public Instant getUpdatedBefore() {
		return updatedBefore;
	}

	public void setUpdatedBefore(Instant updatedBefore) {
		this.updatedBefore = updatedBefore;
	}

	public Instant getUpdatedAfter() {
		return updatedAfter;
	}

	public void setUpdatedAfter(Instant updatedAfter) {
		this.updatedAfter = updatedAfter;
	}


	public Set<String> getMacAddresses() {
		return macAddresses;
	}

	public void setMacAddresses(Set<String> macAddresses) {
		this.macAddresses = macAddresses;
	}
	
    public SocialEventDeviceSearchParams addMacAddresses(String... macAddresses) {
        this.macAddresses = super.addValues(this.macAddresses, macAddresses);

        return this;
    }

	public Set<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(Set<String> deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
	}
	
    public SocialEventDeviceSearchParams addDeviceTypeIds(String... deviceTypeIds) {
        this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);

        return this;
    }
}
