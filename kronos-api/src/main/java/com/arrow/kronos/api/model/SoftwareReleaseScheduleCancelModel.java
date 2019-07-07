package com.arrow.kronos.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.arrow.acn.client.model.AcnDeviceCategory;

public class SoftwareReleaseScheduleCancelModel implements Serializable {

	private static final long serialVersionUID = 3448719024309724815L;

	private Set<String> objectHids = new HashSet<>();
	private AcnDeviceCategory deviceCategory;

	public Set<String> getObjectHids() {
		return this.objectHids;
	}

	public void setObjectHids(Set<String> objectHids) {
		this.objectHids = objectHids;
	}

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}
}
