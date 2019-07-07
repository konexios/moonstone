package com.arrow.apollo.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.apollo.ApolloConstants;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "apollo_widget")
public class ApolloWidget extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -8691453991520610795L;

	@Indexed
	@NotBlank
	private String widgetTypeId;
	@Indexed
	@NotNull
	private ApolloWidgetTypeCategories category;
	@Indexed
	@NotNull
	private IconTypes iconType;
	@NotBlank
	private String icon;

	@Transient
	@JsonIgnore
	private WidgetType refWidgetType;

	public String getWidgetTypeId() {
		return widgetTypeId;
	}

	public void setWidgetTypeId(String widgetTypeId) {
		this.widgetTypeId = widgetTypeId;
	}

	public ApolloWidgetTypeCategories getCategory() {
		return category;
	}

	public void setCategory(ApolloWidgetTypeCategories category) {
		this.category = category;
	}

	public IconTypes getIconType() {
		return iconType;
	}

	public void setIconType(IconTypes iconType) {
		this.iconType = iconType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public WidgetType getRefWidgetType() {
		return refWidgetType;
	}

	public void setRefWidgetType(WidgetType refWidgetType) {
		this.refWidgetType = refWidgetType;
	}

	@Override
	protected String getProductPri() {
		return ApolloConstants.ApolloPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return ApolloConstants.ApolloPri.WIDGET;
	}
}