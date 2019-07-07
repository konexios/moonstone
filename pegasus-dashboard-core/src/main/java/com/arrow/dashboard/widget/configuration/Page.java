package com.arrow.dashboard.widget.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a page inside {@link Configuration}
 * <p>
 * Page is a set of properties, grouped by common meaning<br>
 * User navigates by pages in the configuration (next, previous etc.)
 * 
 * @author dantonov
 *
 */
public class Page extends AbstractConfigurationEntry<Page> {
	private static final long serialVersionUID = 2063217587423807693L;

	private List<PageProperty> properties = new ArrayList<>();

	@Override
	protected Page self() {
		return this;
	}

	/**
	 * Method to add {@link PageProperty}
	 * 
	 * @param property
	 * @return
	 */
	public Page addProperty(PageProperty property) {
		if (properties == null) {
			properties = new ArrayList<>();
		}
		properties.add(property);
		return this;
	}

	/**
	 * Method to get {@link PageProperty} by name
	 * 
	 * @param name
	 * @return
	 */
	public PageProperty getPageProperty(String name) {
		return properties.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
	}

	/**
	 * Method to get all page properties
	 * 
	 * @return
	 */
	public List<PageProperty> getProperties() {
		return properties;
	}
}
