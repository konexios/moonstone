package com.arrow.kronos.data;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class DocumentId implements Serializable {
	private static final long serialVersionUID = 8257811288658159027L;

	@Id
	private String id;

	public DocumentId withId(String id) {
		setId(id);
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
