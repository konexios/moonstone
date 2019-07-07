package com.arrow.kronos.data.action;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class GlobalActionProperty implements Serializable {

	private static final long serialVersionUID = -1881669763883357159L;

	@NotBlank
	private String parameterName;
	@NotBlank
	private String parameterType;
	@NotBlank
	private String parameterValue;

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
}
