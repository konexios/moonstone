package com.arrow.selene.engine.state;

import java.io.Serializable;
import java.time.Instant;

import com.arrow.acs.JsonUtils;

public class State implements Serializable {
	private static final long serialVersionUID = -4200950970075687527L;

	private String value;
	private Instant timestamp;

	public State withValue(String value) {
		setValue(value);
		return this;
	}

	public State withTimestamp(Instant timestamp) {
		setTimestamp(timestamp);
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
