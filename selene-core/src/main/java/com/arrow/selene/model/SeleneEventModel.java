package com.arrow.selene.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SeleneEventModel implements Serializable {
	private static final long serialVersionUID = 3356239772531837875L;

	private String name;
	private String id;
	private String responseQueue;
	private Map<String, String> parameters = new HashMap<>();

	public SeleneEventModel withName(String name) {
		setName(name);
		return this;
	}

	public SeleneEventModel withId(String id) {
		setId(id);
		return this;
	}

	public SeleneEventModel withResponseQueue(String queue) {
		setResponseQueue(queue);
		return this;
	}

	public SeleneEventModel withParameters(Map<String, String> parameters) {
		setParameters(parameters);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResponseQueue() {
		return responseQueue;
	}

	public void setResponseQueue(String responseQueue) {
		this.responseQueue = responseQueue;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
