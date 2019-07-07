package com.arrow.kronos.data.action;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class GlobalActionInput implements Serializable {

	private static final long serialVersionUID = -2068273575053334836L;

	private final static boolean DEFAULT_REQUIRED = false;

	public enum InputType {
		String, List, Map, Long
	}

	@NotBlank
	private String name;
	@NotBlank
	private InputType type;
	private boolean required = DEFAULT_REQUIRED;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputType getType() {
		return type;
	}

	public void setType(InputType type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
