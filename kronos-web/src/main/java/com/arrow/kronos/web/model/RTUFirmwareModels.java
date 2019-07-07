package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.AvailableFirmwareVersion;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.rhea.data.RTURequest;

public class RTUFirmwareModels {

	public static class RTURequestFirmwareModel extends CoreDocumentModel{
		private static final long serialVersionUID = 3456782850563131128L;

		private String ownerName;
		private String ownerEmail;
		private AvailableFirmwareVersion availableFirmwareVersion;
		private String status;
		
		public RTURequestFirmwareModel() {
			super(null, null);
		}

		public RTURequestFirmwareModel(RTURequest rtuRequest) {
			super(rtuRequest.getId(), rtuRequest.getHid());
		}

		public AvailableFirmwareVersion getAvailableFirmwareVersion() {
			return availableFirmwareVersion;
		}

		public void setAvailableFirmwareVersion(AvailableFirmwareVersion availableFirmwareVersion) {
				this.availableFirmwareVersion = availableFirmwareVersion;
		}

		public RTURequestFirmwareModel withAvailableFirmwareVersion(
		        AvailableFirmwareVersion availableFirmwareVersion) {
			setAvailableFirmwareVersion(availableFirmwareVersion);
			
			return this;
		}
		
		public RTURequestFirmwareModel withOwnerName(String ownerName) {
			setOwnerName(ownerName);
			return this;
		}
		
		public RTURequestFirmwareModel withOwnerEmail(String ownerEmail) {
			setOwnerEmail(ownerEmail);
			return this;
		}
		
		public RTURequestFirmwareModel withStatus(String status) {
			setStatus(status);
			return this;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

		public String getOwnerEmail() {
			return ownerEmail;
		}

		public void setOwnerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
	
	public static class RTUAvailableFirmwareModel extends CoreDocumentModel{
		private static final long serialVersionUID = 3456782850563131128L;

		private DeviceTypeOption deviceType;
		private long numberOfAssets;
		private String hardwareVersionName;
		private List<AvailableFirmwareVersion> availableFirmwareVersions;
		private AvailableFirmwareVersion currentFirmwareVersion;
		
		public RTUAvailableFirmwareModel() {
			super(null, null);
		}

		public RTUAvailableFirmwareModel(RTURequest rtuRequest) {
			super(rtuRequest.getId(), rtuRequest.getHid());
		}

		public long getNumberOfAssets() {
			return numberOfAssets;
		}

		public void setNumberOfAssets(long numberOfAssets) {
			this.numberOfAssets = numberOfAssets;
		}

		public String getHardwareVersionName() {
			return hardwareVersionName;
		}

		public void setHardwareVersionName(String hardwareVersionName) {
			this.hardwareVersionName = hardwareVersionName;
		}

		public List<AvailableFirmwareVersion> getAvailableFirmwareVersion() {
			return availableFirmwareVersions;
		}

		public void setAvailableFirmwareVersion(List<AvailableFirmwareVersion> availableFirmwareVersions) {
				this.availableFirmwareVersions = availableFirmwareVersions;
		}

		public RTUAvailableFirmwareModel withDeviceType(DeviceTypeOption deviceType) {
			setDeviceType(deviceType);
			return this;
		}


		public RTUAvailableFirmwareModel withNumberOfAssets(long numberOfAssets) {
			setNumberOfAssets(numberOfAssets);

			return this;
		}

		public RTUAvailableFirmwareModel withHardwareVersionName(String hardwareVersionName) {
			setHardwareVersionName(hardwareVersionName);

			return this;
		}

		public RTUAvailableFirmwareModel withCurrentFirmwareVersion(
				AvailableFirmwareVersion currentFirmwareVersion) {
			setCurrentFirmwareVersion(currentFirmwareVersion);
			
			return this;
		}

		public RTUAvailableFirmwareModel addAvailableFirmwareVersion(
		        AvailableFirmwareVersion availableFirmwareVersion) {
			if(this.availableFirmwareVersions == null) {
				this.availableFirmwareVersions = new ArrayList<>();
			}
			this.availableFirmwareVersions.add(availableFirmwareVersion);
			return this;
		}

		public DeviceTypeOption getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceTypeOption deviceType) {
			this.deviceType = deviceType;
		}

		public AvailableFirmwareVersion getCurrentFirmwareVersion() {
			return currentFirmwareVersion;
		}

		public void setCurrentFirmwareVersion(AvailableFirmwareVersion currentFirmwareVersion) {
			this.currentFirmwareVersion = currentFirmwareVersion;
		}
		
		public String getDeviceTypeName() {
			return this.deviceType.getName();
		}

		public RTUAvailableFirmwareModel withAvailableFirmwareVersions(List<AvailableFirmwareVersion> availableFirmwareVersions) {
			setAvailableFirmwareVersion(availableFirmwareVersions);
			return this;
		}
	}
	
	public static class RTURequestCreateModel implements Serializable{
		private static final long serialVersionUID = 3456782850563131128L;

		private DeviceTypeOption deviceType;
		private AvailableFirmwareVersion currentFirmwareVersion;
		
		public DeviceTypeOption getDeviceType() {
			return deviceType;
		}
		public void setDeviceType(DeviceTypeOption deviceType) {
			this.deviceType = deviceType;
		}
		public AvailableFirmwareVersion getCurrentFirmwareVersion() {
			return currentFirmwareVersion;
		}
		public void setCurrentFirmwareVersion(AvailableFirmwareVersion currentFirmwareVersion) {
			this.currentFirmwareVersion = currentFirmwareVersion;
		}
		
	}
}
