package com.arrow.pegasus.dashboard.data;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;

/**
 * Defines widget on board or container. Created by dantonov on 27.09.2017.
 */
@Document(collection = "pegasus_dd_widget")
public class Widget extends ContainerAbstract {
	private static final long serialVersionUID = -6879420505384789309L;

	@NotBlank
	private String parentId;
	@NotNull
	private WidgetParentTypes parentType;
	@NotBlank
	private String widgetTypeId;

	public Widget() {
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public WidgetParentTypes getParentType() {
		return parentType;
	}

	public void setParentType(WidgetParentTypes parentType) {
		this.parentType = parentType;
	}

	public String getWidgetTypeId() {
		return widgetTypeId;
	}

	public void setWidgetTypeId(String widgetTypeId) {
		this.widgetTypeId = widgetTypeId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_WIDGET;
	}
}