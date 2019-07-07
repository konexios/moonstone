/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.arrow.acs.AcsUtils;

public class DeviceRegistrationModel implements Serializable {
	private static final long serialVersionUID = -3090704979892056203L;

	private String uid;
	private String type;
	private String name;
	private String userHid;
	private String gatewayHid;
	private Map<String, String> info = new HashMap<>();
	private Map<String, String> properties = new HashMap<>();
	private boolean isEnabled = true;
	private Set<String> tags = new HashSet<>();
	private String nodeHid;
	private String softwareName;
	private String softwareVersion;

	public void trim() {
		uid = AcsUtils.trimToNull(uid);
		type = AcsUtils.trimToNull(type);
		name = AcsUtils.trimToNull(name);
		userHid = AcsUtils.trimToNull(userHid);
		gatewayHid = AcsUtils.trimToNull(gatewayHid);
		nodeHid = AcsUtils.trimToNull(nodeHid);
		softwareName = AcsUtils.trimToNull(softwareName);
		softwareVersion = AcsUtils.trimToNull(softwareVersion);
	}

	public DeviceRegistrationModel withUid(String uid) {
		setUid(uid);
		return this;
	}

	public DeviceRegistrationModel withType(String type) {
		setType(type);
		return this;
	}

	public DeviceRegistrationModel withName(String name) {
		setName(name);
		return this;
	}

	public DeviceRegistrationModel withUserHid(String userHid) {
		setUserHid(userHid);
		return this;
	}

	public DeviceRegistrationModel withGatewayHid(String gatewayHid) {
		setGatewayHid(gatewayHid);
		return this;
	}

	public DeviceRegistrationModel withInfo(Map<String, String> info) {
		setInfo(info);
		return this;
	}

	public DeviceRegistrationModel withProperties(Map<String, String> properties) {
		setProperties(properties);
		return this;
	}

	public DeviceRegistrationModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public DeviceRegistrationModel withTags(Set<String> tags) {
		setTags(tags);
		return this;
	}

	public DeviceRegistrationModel withNodeHid(String nodeHid) {
		setNodeHid(nodeHid);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	public String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getNodeHid() {
		return nodeHid;
	}

	public void setNodeHid(String nodeHid) {
		this.nodeHid = nodeHid;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gatewayHid == null) ? 0 : gatewayHid.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((userHid == null) ? 0 : userHid.hashCode());
		result = prime * result + (isEnabled ? 1 : 0);
		result = prime * result + (tags == null ? 0 : tags.hashCode());
		result = prime * result + (nodeHid == null ? 0 : nodeHid.hashCode());
		result = prime * result + ((softwareName == null) ? 0 : softwareName.hashCode());
		result = prime * result + ((softwareVersion == null) ? 0 : softwareVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceRegistrationModel other = (DeviceRegistrationModel) obj;
		if (gatewayHid == null) {
			if (other.gatewayHid != null)
				return false;
		} else if (!gatewayHid.equals(other.gatewayHid))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (userHid == null) {
			if (other.userHid != null)
				return false;
		} else if (!userHid.equals(other.userHid))
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags)) {
			return false;
		}
		if (nodeHid == null) {
			if (other.nodeHid != null)
				return false;
		} else if (!nodeHid.equals(other.nodeHid)) {
			return false;
		}
		if (softwareName == null) {
			if (other.softwareName != null)
				return false;
		} else if (!softwareName.equals(other.softwareName)) {
			return false;
		}
		if (softwareVersion == null) {
			if (other.softwareVersion != null)
				return false;
		} else if (!softwareVersion.equals(other.softwareVersion)) {
			return false;
		}
		return true;
	}
}
