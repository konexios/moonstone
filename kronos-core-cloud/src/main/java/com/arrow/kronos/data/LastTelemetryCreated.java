package com.arrow.kronos.data;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class LastTelemetryCreated implements Serializable {
	private static final long serialVersionUID = -4474798997944591222L;

	@Id
	private String name;
	@Field
	private Instant value;

	public LastTelemetryCreated withName(String name) {
		setName(name);
		return this;
	}

	public LastTelemetryCreated withValue(Instant value) {
		setValue(value);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getValue() {
		return value;
	}

	public void setValue(Instant value) {
		this.value = value;
	}
}
