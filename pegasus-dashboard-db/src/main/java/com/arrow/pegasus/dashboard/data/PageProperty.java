package com.arrow.pegasus.dashboard.data;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

/**
 * Defines configuration page property Created by dantonov on 27.09.2017.
 */
@Document(collection = "pegasus_dd_page_property")
public class PageProperty extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 8101549165321200385L;

	@NotBlank
	private String configurationPageId;
	@NotBlank
	private String name;
	@NotBlank
	private String label;
	private String description;
	private boolean required;
	private boolean active;
	private String error;
	private int propertyNumber;

	public PageProperty() {
	}

	public String getConfigurationPageId() {
		return configurationPageId;
	}

	public void setConfigurationPageId(String configurationPageId) {
		this.configurationPageId = configurationPageId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(int propertyNumber) {
		this.propertyNumber = propertyNumber;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_PAGE_PROPERTY;
	}
}
