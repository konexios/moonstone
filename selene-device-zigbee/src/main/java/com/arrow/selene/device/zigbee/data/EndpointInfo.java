package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class EndpointInfo implements Serializable {
	private static final long serialVersionUID = 4622640044646272126L;

	private int id;
	private String applicationProfile;
	private String deviceProfile;
	private Map<Integer, ClusterInfo> clusters = new TreeMap<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplicationProfile() {
		return applicationProfile;
	}

	public EndpointInfo withApplicationProfile(String applicationProfile) {
		this.applicationProfile = applicationProfile;
		return this;
	}

	public String getDeviceProfile() {
		return deviceProfile;
	}

	public EndpointInfo withDeviceProfile(String deviceProfile) {
		this.deviceProfile = deviceProfile;
		return this;
	}

	public Map<Integer, ClusterInfo> getClusters() {
		return clusters;
	}
}
