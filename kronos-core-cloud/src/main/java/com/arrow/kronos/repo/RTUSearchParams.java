package com.arrow.kronos.repo;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.acn.client.model.RightToUseStatus;

public class RTUSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 3405784362510255315L;

	private Set<String> deviceTypeIds;
	private EnumSet<RightToUseStatus> statuses;


	public EnumSet<RightToUseStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<RightToUseStatus> statuses) {
		this.statuses = statuses;
	}
	
    public RTUSearchParams addDeviceTypeIds(String... deviceTypeIds) {
        this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);

        return this;
    }

	public Set<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(Set<String> deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
	}
}