package com.arrow.dashboard.widget.configuration;

import java.util.LinkedList;
import java.util.Optional;

import com.arrow.dashboard.property.Property;
import com.arrow.dashboard.widget.annotation.Widget;

import moonstone.acs.Loggable;

/**
 * This class defines {@link Widget} configuration
 * <p>
 * Configuration has its parameters and a set of {@link Page}s with
 * {@link Property} properties<br>
 * Widget specifies its configuration and this configuration may be changed by
 * the user<br>
 * Configuration is a 'persistence' model for the widget instance. It contains
 * all parameters for widget to be considered as 'configured'
 * <p>
 * Configuration instance is <b>mutable</b> by its nature. It is up to widget to
 * change configuration as it needed. It is like temporarily object to manage
 * configuration process. Configuration persistence is managed by the framework.
 * 
 * @author dantonov
 *
 */
public class Configuration extends AbstractConfigurationEntry<Configuration> {
	private static final long serialVersionUID = 1513183445342462316L;

	private double version;
	private LinkedList<Page> pages = new LinkedList<>();
	private int currentPage;
	private int changedPage;
	private boolean closed;

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public Configuration withVersion(double version) {
		setVersion(version);

		return self();
	}

	/**
	 * Method to close the configuration
	 * <p>
	 * Closed configuration is considered as 'approved' by the widget instance
	 * and has been persisted by the framework
	 */
	public void close() {
		closed = true;
	}

	/**
	 * Method to open configuration
	 */
	public void open() {
		closed = false;
	}

	/**
	 * Method to check if the configuration is closed
	 * 
	 * @return
	 */
	public boolean isClosed() {
		return closed;
	}

	@Override
	protected Configuration self() {
		return this;
	}

	/**
	 * Method to add a new page into configuration
	 * 
	 * @param page
	 *            {@link Page}
	 * @return
	 */
	public Configuration addPage(Page page) {
		if (pages == null) {
			pages = new LinkedList<>();
		}
		pages.add(page);
		return this;
	}

	/**
	 * Method to replace a {@link Page} to another one, by the page name
	 * 
	 * @param pageName
	 * @param page
	 * @return
	 */
	public Configuration replacePage(String pageName, Page page) {
		Optional<Page> pageToReplace = pages.stream().filter(p -> p.getName().equals(pageName)).findFirst();
		if (pageToReplace.isPresent()) {
			int position = pages.indexOf(pageToReplace.get());
			pages.remove(pageToReplace.get());
			pages.add(position, page);
		}
		return this;
	}

	/**
	 * Method to remove a page by the name
	 * 
	 * @param pageName
	 * @return
	 */
	public Configuration removePage(String pageName) {
		Optional<Page> pageToReplace = pages.stream().filter(p -> p.getName().equals(pageName)).findFirst();
		if (pageToReplace.isPresent()) {
			pages.remove(pageToReplace.get());
		}
		return this;
	}

	/**
	 * Method to set 'current page' parameter<br>
	 * Refer to {@link Configuration#getCurrentPage()}
	 * 
	 * @param currentPage
	 * @return
	 */
	public Configuration setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		return this;
	}

	/**
	 * Method to set 'changed page' parameter<br>
	 * Refer to {@link Configuration#getChangedPage()}
	 * 
	 * @param currentPage
	 * @return
	 */
	public Configuration setChangedPage(int changedPage) {
		this.changedPage = changedPage;
		return this;
	}

	/**
	 * Method to return {@link Page} pages
	 * 
	 * @return
	 */
	public LinkedList<Page> getPages() {
		return pages;
	}

	/**
	 * Method to get a page by name
	 * 
	 * @param pageName
	 * @return
	 */
	public Page getPage(String pageName) {
		return pages.stream().filter(page -> page.getName().equals(pageName)).findFirst().orElse(null);
	}

	/**
	 * Method to get 'current page'
	 * <p>
	 * Current page shows the page user 'is configuring right now'<br>
	 * Current page is show as an active page for the user on front end
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Method to get 'changed page'
	 * <p>
	 * Changed page shows the lowest page modified by the user in configuration
	 * <br>
	 * Changed page is differ than current page only when user navigated back to
	 * previous pages and 'potentially' has changed something where<br>
	 * This case is very important to be handled since next pages/properties
	 * depends on previous, by entered data
	 * 
	 * @return
	 */
	public int getChangedPage() {
		return changedPage;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Configuration configuration, String pageName, String propertyName) {

		Loggable loggable = new Loggable() {
		};

		String method = "getValue";
		loggable.logDebug(method, "pageName: %s, propertyName: %s", pageName, propertyName);

		T result = null;

		Optional<Page> page = configuration.pages.stream().filter(p -> p.getName().equals(pageName)).findFirst();
		if (page.isPresent()) {
			loggable.logDebug(method, "page: %s, property size: %s", page.get().getName(),
					page.get().getProperties().size());

			Optional<PageProperty> pageProperty = page.get().getProperties().stream()
					.filter(p -> p.getName().equals(propertyName)).findFirst();
			if (pageProperty.isPresent()) {
				loggable.logDebug(method, "page: %s, property: %s", page.get().getName(), pageProperty.get().getName());

				Object value = pageProperty.get().getProperty().getValue();

				if (value != null)
					try {
						result = (T) value;
					} catch (ClassCastException castException) {
					}
			}
		}
		return result;
	}

}
