package com.arrow.kronos.data;

import java.util.HashSet;
import java.util.Set;

import moonstone.acn.client.model.AcnDeviceCategory;

public class EligibleFirmwareChangeGroup {
	public String deviceTypeId;
	public String rheaDeviceTypeId;
	public String softwareReleaseId;
	public Set<String> newSoftwareReleaseIds = new HashSet<>();
	public long numberOfDevices;
	public AcnDeviceCategory category;

	public EligibleFirmwareChangeGroup(String deviceTypeId, String rheaDeviceTypeId, String softwareReleaseId,
			AcnDeviceCategory category) {
		this.deviceTypeId = deviceTypeId;
		this.rheaDeviceTypeId = rheaDeviceTypeId;
		this.softwareReleaseId = softwareReleaseId;
		this.category = category;
	}

	public void setRheaDeviceTypeId(String rheaDeviceTypeId) {
		this.rheaDeviceTypeId = rheaDeviceTypeId;
	}

	public void setNumberOfDevices(long l) {
		this.numberOfDevices = l;
	}

	public long getNumberOfDevices() {
		return numberOfDevices;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceTypeId == null) ? 0 : deviceTypeId.hashCode());
		result = prime * result + ((rheaDeviceTypeId == null) ? 0 : rheaDeviceTypeId.hashCode());
		result = prime * result + ((softwareReleaseId == null) ? 0 : softwareReleaseId.hashCode());
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
		EligibleFirmwareChangeGroup other = (EligibleFirmwareChangeGroup) obj;
		if (deviceTypeId == null) {
			if (other.deviceTypeId != null)
				return false;
		} else if (!deviceTypeId.equals(other.deviceTypeId))
			return false;
		if (rheaDeviceTypeId == null) {
			if (other.rheaDeviceTypeId != null)
				return false;
		} else if (!rheaDeviceTypeId.equals(other.rheaDeviceTypeId))
			return false;
		if (softwareReleaseId == null) {
			if (other.softwareReleaseId != null)
				return false;
		} else if (!softwareReleaseId.equals(other.softwareReleaseId))
			return false;
		if (newSoftwareReleaseIds == null) {
			if (other.newSoftwareReleaseIds != null)
				return false;
		} else if (!newSoftwareReleaseIds.equals(other.newSoftwareReleaseIds))
			return false;
		return true;
	}
}