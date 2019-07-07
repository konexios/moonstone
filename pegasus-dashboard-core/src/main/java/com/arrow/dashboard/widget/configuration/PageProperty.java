package com.arrow.dashboard.widget.configuration;

import com.arrow.dashboard.property.Property;

/**
 * This class defines a property on the {@link Page} inside
 * {@link Configuration}
 * <p>
 * Page property is a wrapper for {@link Property}.<br>
 * {@link Property} is a definition of data model, page property is a property
 * specification inside {@link Page}
 * 
 * @author dantonov
 *
 */
public class PageProperty extends AbstractConfigurationEntry<PageProperty> {
	private static final long serialVersionUID = -3779942110897455960L;

	private String description;
	private boolean required;
	private boolean active;
	private boolean persisted = true;
	private Property<?, ?, ?> property;

	@Override
	protected PageProperty self() {
		return this;
	}

	/**
	 * Method to get page property description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method to check if this page property is required
	 * 
	 * @return
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Method to define this page property as required
	 * 
	 * @param required
	 * @return
	 */
	public PageProperty setRequired(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Method to check if this page property is active
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Method to define this page property as active
	 * 
	 * @param active
	 * @return
	 */
	public PageProperty setActive(boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * Method to check if this property is persisted
	 * 
	 * @return
	 */
	public boolean isPersisted() {
		return persisted;
	}

	/**
	 * Method to define this page property as persisted
	 * 
	 * @param persisted
	 * @return
	 */
	public PageProperty setPersisted(boolean persisted) {
		this.persisted = persisted;
		return this;
	}

	/**
	 * Method to get {@link Property} of the page property
	 * 
	 * @return
	 */
	public Property<?, ?, ?> getProperty() {
		return property;
	}

	public void setProperty(Property<?, ?, ?> property) {
		this.property = property;
	}

	/**
	 * Method to set page property description
	 * 
	 * @param description
	 * @return
	 */
	public PageProperty withDescription(String description) {
		setDescription(description);

		return self();
	}

	public PageProperty withRequired(boolean required) {
		setRequired(required);

		return self();
	}

	public PageProperty withActive(boolean active) {
		setActive(active);

		return self();
	}

	public PageProperty withPersisted(boolean persisted) {
		setPersisted(persisted);

		return self();
	}

	/**
	 * Method to set {@link Property} for this page property
	 * 
	 * @param property
	 * @return
	 */
	public PageProperty withProperty(Property<?, ?, ?> property) {
		this.property = property;

		return self();
	}
}