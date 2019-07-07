package com.arrow.dashboard.widget.configuration;

import java.io.Serializable;

public abstract class AbstractConfigurationEntry<T extends AbstractConfigurationEntry<T>> implements Serializable {
	private static final long serialVersionUID = -4149972223804331293L;

	private String name;
	private String label;
	private String error;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Method to get entity error
	 * 
	 * @return
	 */
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Method to check if the entity has an error
	 * 
	 * @return
	 */
	public boolean hasError() {
		return error == null;
	}

	/**
	 * Method to remove error
	 */
	public void clearError() {
		this.error = null;
	}

	protected abstract T self();

	public T withName(String name) {
		setName(name);

		return self();
	}

	public T withLabel(String label) {
		setLabel(label);

		return self();
	}

	public T withError(String error) {
		setError(error);

		return self();
	}
}
