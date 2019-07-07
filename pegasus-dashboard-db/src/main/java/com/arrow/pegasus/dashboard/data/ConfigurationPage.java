package com.arrow.pegasus.dashboard.data;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

/**
 * Defines configuration page. Created by dantonov on 27.09.2017.
 */
@Document(collection = "pegasus_dd_configuration_page")
public class ConfigurationPage extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 9057858307317738944L;

	@NotBlank
	private String widgetConfigurationId;
	@NotBlank
	private String name;
	@NotBlank
	private String label;
	private String error;
	private int pageNumber;

	public ConfigurationPage() {
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getWidgetConfigurationId() {
		return widgetConfigurationId;
	}

	public void setWidgetConfigurationId(String widgetConfigurationId) {
		this.widgetConfigurationId = widgetConfigurationId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_CONFIGURATION_PAGE;
	}
}
