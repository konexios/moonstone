package com.arrow.kronos.data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

@Document(collection = "device_state_trans")

public class DeviceStateTrans extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -4575523731049289680L;

	public enum Status {
		PENDING, RECEIVED, COMPLETE, ERROR
	}

	public enum Type {
		UPDATE, REQUEST
	}

	@NotBlank
	private String deviceId;
	@NotBlank
	private String applicationId;
	@NotNull
	private Type type = Type.UPDATE;
	@NotNull
	private Status status = Status.PENDING;

	private String error;
	private Map<String, DeviceStateValue> states = new HashMap<>();

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

	public Map<String, DeviceStateValue> getStates() {
		return states;
	}

	public void setStates(Map<String, DeviceStateValue> states) {
		this.states = states;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.DEVICE_SHADOW_TRANS;
	}
}
