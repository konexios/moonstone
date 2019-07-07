package com.arrow.kronos.data;

public enum IoTProvider {
	// @formatter:off  
	ArrowConnect("Arrow Connect"),
	AWS("AWS"),
	IBM("IBM"),
	AZURE("AZURE");
	// @formatter:on

	private final String description;

	private IoTProvider(String description) {
		this.description = description;
	}

	public String getName() {
		return name();
	}

	public String getDescription() {
		return description;
	}
}
