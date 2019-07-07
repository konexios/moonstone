package com.arrow.pegasus.data.profile;

import java.io.Serializable;

public class ProductFeature implements Serializable {
	private static final long serialVersionUID = -2461302651070032387L;

	private String name;
	private String description;
	private String systemName;

	public ProductFeature() {
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
