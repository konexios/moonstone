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
import java.util.Map;

public class CreateGatewayModel implements Serializable {
	private static final long serialVersionUID = -7283768476053595318L;

	public enum GatewayType {
		Local, Cloud, Mobile
	}

	private String uid;
	private String name;
	private GatewayType type;
	private String deviceType;
	private String userHid;
	private String osName;
	private String softwareName;
	private String softwareVersion;
	private String sdkVersion;
	private String applicationHid;

	protected Map<String, String> info = new HashMap<>();
	protected Map<String, String> properties = new HashMap<>();

	public CreateGatewayModel withUid(String uid) {
		setUid(uid);
		return this;
	}

	public CreateGatewayModel withName(String name) {
		setName(name);
		return this;
	}

	public CreateGatewayModel withType(GatewayType type) {
		setType(type);
		return this;
	}

	public CreateGatewayModel withDeviceType(String deviceType) {
		setDeviceType(deviceType);
		return this;
	}

	public CreateGatewayModel withUserHid(String userHid) {
		setUserHid(userHid);
		return this;
	}

	public CreateGatewayModel withOsName(String osName) {
		setOsName(osName);
		return this;
	}

	public CreateGatewayModel withSoftwareName(String softwareName) {
		setSoftwareName(softwareName);
		return this;
	}

	public CreateGatewayModel withSoftwareVersion(String softwareVersion) {
		setSoftwareVersion(softwareVersion);
		return this;
	}

	public CreateGatewayModel withSdkVersion(String sdkVersion) {
		setSdkVersion(sdkVersion);
		return this;
	}

	public CreateGatewayModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public CreateGatewayModel withInfo(Map<String, String> info) {
		setInfo(info);
		return this;
	}

	public CreateGatewayModel withProperties(Map<String, String> properties) {
		setProperties(properties);
		return this;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GatewayType getType() {
		return type;
	}

	public void setType(GatewayType type) {
		this.type = type;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
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

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationHid == null) ? 0 : applicationHid.hashCode());
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((osName == null) ? 0 : osName.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((sdkVersion == null) ? 0 : sdkVersion.hashCode());
		result = prime * result + ((softwareName == null) ? 0 : softwareName.hashCode());
		result = prime * result + ((softwareVersion == null) ? 0 : softwareVersion.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((userHid == null) ? 0 : userHid.hashCode());
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
		CreateGatewayModel other = (CreateGatewayModel) obj;
		if (applicationHid == null) {
			if (other.applicationHid != null)
				return false;
		} else if (!applicationHid.equals(other.applicationHid))
			return false;
		if (deviceType == null) {
			if (other.deviceType != null)
				return false;
		} else if (!deviceType.equals(other.deviceType))
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
		if (osName == null) {
			if (other.osName != null)
				return false;
		} else if (!osName.equals(other.osName))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (sdkVersion == null) {
			if (other.sdkVersion != null)
				return false;
		} else if (!sdkVersion.equals(other.sdkVersion))
			return false;
		if (softwareName == null) {
			if (other.softwareName != null)
				return false;
		} else if (!softwareName.equals(other.softwareName))
			return false;
		if (softwareVersion == null) {
			if (other.softwareVersion != null)
				return false;
		} else if (!softwareVersion.equals(other.softwareVersion))
			return false;
		if (type != other.type)
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
		return true;
	}
}
