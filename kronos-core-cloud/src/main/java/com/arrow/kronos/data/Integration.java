package com.arrow.kronos.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

public class Integration implements Serializable {
	private static final long serialVersionUID = 7272694482945676988L;

	private static final boolean DEFAULT_ENABLED = false;

	@NotBlank
	private IntegrationType type;
	private boolean enabled = DEFAULT_ENABLED;
	private Map<String, String> properties = new HashMap<>();

	public IntegrationType getType() {
		return type;
	}

	public void setType(IntegrationType type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}