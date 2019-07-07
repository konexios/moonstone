package com.arrow.kronos.data;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class TestProcedureStep implements Serializable {
	private static final long serialVersionUID = 589169210526870993L;

	@NotBlank
	private String id;
	@NotBlank
	private String name;
	private String description;
	private int sortOrder;

	public TestProcedureStep() {
	}

	public TestProcedureStep(String id, String name, String description, int sortOrder) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.sortOrder = sortOrder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
}
