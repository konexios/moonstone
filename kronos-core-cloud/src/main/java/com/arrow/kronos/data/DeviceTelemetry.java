package com.arrow.kronos.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acn.client.model.TelemetryItemType;

public class DeviceTelemetry implements Serializable {
	private static final long serialVersionUID = 841481041832736471L;

	@NotBlank
	private String name;
	private String description;
	@NotNull
	private TelemetryItemType type;
	private String telemetryUnitId;
	private Map<String, String> variables = new HashMap<>();

	@Transient
	@JsonIgnore
	private TelemetryUnit refTelemetryUnit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TelemetryItemType getType() {
		return type;
	}

	public void setType(TelemetryItemType type) {
		this.type = type;
	}

	public String getTelemetryUnitId() {
		return telemetryUnitId;
	}

	public void setTelemetryUnitId(String telemetryUnitId) {
		this.telemetryUnitId = telemetryUnitId;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

	public TelemetryUnit getRefTelemetryUnit() {
		return refTelemetryUnit;
	}

	public void setRefTelemetryUnit(TelemetryUnit refTelemetryUnit) {
		this.refTelemetryUnit = refTelemetryUnit;
	}
}
