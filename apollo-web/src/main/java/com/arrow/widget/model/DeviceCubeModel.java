package com.arrow.widget.model;

import java.io.Serializable;

public class DeviceCubeModel implements Serializable {
	private static final long serialVersionUID = 630692626646345267L;

	private String cubeValues;

	public String getCubeValues() {
		return cubeValues;
	}

	public void setCubeValues(String cubeValues) {
		this.cubeValues = cubeValues;
	}
}
