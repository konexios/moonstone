package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;

@Document(collection = "telemetry_unit")
public class TelemetryUnit extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 8860100099385340912L;

	@NotBlank
	private String systemName;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.TELEMETRY_UNIT;
	}
}
