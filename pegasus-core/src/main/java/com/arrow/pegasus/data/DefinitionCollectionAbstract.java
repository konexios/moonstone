package com.arrow.pegasus.data;

import javax.validation.constraints.NotBlank;

import com.arrow.pegasus.CoreConstant;

public abstract class DefinitionCollectionAbstract extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = 8514062243280955984L;

	@NotBlank
	private String name;
	@NotBlank
	private String description;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
