package com.arrow.kronos.action.global;

import java.io.Serializable;

import com.arrow.kronos.data.action.GlobalActionInput.InputType;

public class MessageInputModel implements Serializable {

	private static final long serialVersionUID = 3386223977801591800L;

	private String name;
	private InputType type;
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
