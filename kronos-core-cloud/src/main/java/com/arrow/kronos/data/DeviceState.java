package com.arrow.kronos.data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "device_state")
@CompoundIndexes({
		@CompoundIndex(name = "applicationId_deviceId", background = true, def = "{'applicationId' : 1, 'deviceId' : 1}") })
public class DeviceState extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -7025716460642792425L;

	@NotBlank
	private String deviceId;
	@NotBlank
	private String applicationId;

	private Map<String, DeviceStateValue> states = new HashMap<>();

	@Transient
	@JsonIgnore
	private Device refDevice;

	public Device getRefDevice() {
		return refDevice;
	}

	public void setRefDevice(Device refDevice) {
		this.refDevice = refDevice;
	}

	public Map<String, DeviceStateValue> getStates() {
		return states;
	}

	public void setStates(Map<String, DeviceStateValue> states) {
		this.states = states;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.DEVICE_SHADOW;
	}
}
