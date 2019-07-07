package com.arrow.pegasus.dashboard.data;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;

@Document(collection = "pegasus_dd_widget_type")
public class WidgetType extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -8716519146898985679L;

	@NotBlank
	private String className;
	@NotBlank
	private String directive;
	private WidgetSizes sizes;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDirective() {
		return directive;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}

	public WidgetSizes getSizes() {
		return sizes;
	}

	public void setSizes(WidgetSizes sizes) {
		this.sizes = sizes;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_WIDGET_TYPE;
	}
}