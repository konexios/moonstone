package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "social_event_device")
public class SocialEventDevice extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 4729685458643013419L;

	@NotBlank
	private String deviceTypeId;
	@NotBlank
	private String macAddress;
	@NotBlank
	private String pinCode;

	@Transient
	@JsonIgnore
	private DeviceType refDeviceType;

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.SOCIAL_EVENT_DEVICE;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public DeviceType getRefDeviceType() {
		return refDeviceType;
	}

	public void setRefDeviceType(DeviceType refDeviceType) {
		this.refDeviceType = refDeviceType;
	}
}
