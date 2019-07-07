package com.arrow.kronos.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "telemetry_replay")
public class TelemetryReplay extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -5938896646765499812L;

	@NotBlank
	private String applicationId;
	@NotBlank
	private String telemetryReplayTypeId;
	@NotBlank
	private String name;
	private String description;
	@NotNull
	private Instant start;
	@NotNull
	private Instant end;
	private Map<String, String> properties = new HashMap<>();
	private Map<String, String> relatedDevices = new HashMap<>();

	@Transient
	@JsonIgnore
	private Application applicationRef;
	@Transient
	@JsonIgnore
	private TelemetryReplayType telemetryReplayTypeRef;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getTelemetryReplayTypeId() {
		return telemetryReplayTypeId;
	}

	public void setTelemetryReplayTypeId(String telemetryReplayTypeId) {
		this.telemetryReplayTypeId = telemetryReplayTypeId;
	}

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

	public Instant getStart() {
		return start;
	}

	public void setStart(Instant start) {
		this.start = start;
	}

	public Instant getEnd() {
		return end;
	}

	public void setEnd(Instant end) {
		this.end = end;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getRelatedDevices() {
		return relatedDevices;
	}

	public void setRelatedDevices(Map<String, String> relatedDevices) {
		this.relatedDevices = relatedDevices;
	}

	public TelemetryReplayType getTelemetryReplayTypeRef() {
		return telemetryReplayTypeRef;
	}

	public void setTelemetryReplayTypeRef(TelemetryReplayType telemetryReplayTypeRef) {
		this.telemetryReplayTypeRef = telemetryReplayTypeRef;
	}

	public Application getApplicationRef() {
		return applicationRef;
	}

	public void setApplicationRef(Application applicationRef) {
		this.applicationRef = applicationRef;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.TELEMETRY_REPLAY;
	}
}
