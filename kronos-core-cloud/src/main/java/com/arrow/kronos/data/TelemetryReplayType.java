package com.arrow.kronos.data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "telemetry_replay_type")
public class TelemetryReplayType extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 5098415943567601866L;

	@NotBlank
	private String applicationId;
	private Map<String, String> requiredProperties = new HashMap<>();
	private Map<String, String> optionalProperties = new HashMap<>();

	@Transient
	@JsonIgnore
	private Application applicationRef;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Map<String, String> getRequiredProperties() {
		return requiredProperties;
	}

	public void setRequiredProperties(Map<String, String> requiredProperties) {
		this.requiredProperties = requiredProperties;
	}

	public Map<String, String> getOptionalProperties() {
		return optionalProperties;
	}

	public void setOptionalProperties(Map<String, String> optionalProperties) {
		this.optionalProperties = optionalProperties;
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
		return KronosConstants.KronosPri.TELEMETRY_REPLAY_TYPE;
	}
}