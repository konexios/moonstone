package com.arrow.pegasus.dashboard.data;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

/**
 * Defines configuration page property value Created by dantonov on 27.09.2017.
 */
@Document(collection = "pegasus_dd_property_value")
public class PropertyValue extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -5431708838574560744L;

	@NotBlank
	private String pagePropertyId;
	private Object value;
	private String type;
	private int version;

	public PropertyValue() {
	}

	public PropertyValue(PropertyValue propertyValue) {
		this.pagePropertyId = propertyValue.getPagePropertyId();
	}

	public String getPagePropertyId() {
		return pagePropertyId;
	}

	public void setPagePropertyId(String pagePropertyId) {
		this.pagePropertyId = pagePropertyId;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_PROPERTY_VALUE;
	}
}
