package com.arrow.dashboard.runtime.model;

import java.util.Map;

import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels.RuntimeConfigurationPatch;

public class WidgetRuntimeConfigurationPatch {

	private Map<String, String> properties;

	public WidgetRuntimeConfigurationPatch(RuntimeConfigurationPatch runtimeConfigurationPatch) {
		super();
		if (runtimeConfigurationPatch != null) {
			this.properties = runtimeConfigurationPatch.getProperties();
		}
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "WidgetRuntimeConfigurationPatch [properties=" + properties + "]";
	}
}
