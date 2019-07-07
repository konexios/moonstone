package com.arrow.pegasus.dashboard.data;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

/**
 * Defines widget configuration. Created by dantonov on 27.09.2017.
 */
@Document(collection = "pegasus_dd_widget_configuration")
public class WidgetConfiguration extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 8069626304225822362L;

	private static final double DEFAULT_VERSION = 0.1;
	private static final int DEFAULT_PAGE = 0;
	private static final boolean DEFAULT_CLOSED = false;

	@NotBlank
	private String widgetId;
	@NotBlank
	private String name;
	@NotBlank
	private String label;
	private double version = DEFAULT_VERSION;
	private int currentPage = DEFAULT_PAGE;
	private int changedPage = DEFAULT_PAGE;
	private boolean closed = DEFAULT_CLOSED;
	private String error;

	public WidgetConfiguration() {
	}

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

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getChangedPage() {
		return changedPage;
	}

	public void setChangedPage(int changedPage) {
		this.changedPage = changedPage;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_WIDGET_CONFIGURATION;
	}
}
