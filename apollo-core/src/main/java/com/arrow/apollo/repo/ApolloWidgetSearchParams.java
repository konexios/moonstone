package com.arrow.apollo.repo;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.apollo.data.ApolloWidgetTypeCategories;
import com.arrow.apollo.data.IconTypes;
import com.arrow.pegasus.repo.params.DefinitionDocumentParamsAbstract;

public class ApolloWidgetSearchParams extends DefinitionDocumentParamsAbstract {
	private static final long serialVersionUID = -8921695542452620627L;

	private Set<String> widgetTypeIds;
	private EnumSet<ApolloWidgetTypeCategories> categories;
	private EnumSet<IconTypes> iconTypes;

	public Set<String> getWidgetTypeIds() {
		return super.getValues(widgetTypeIds);
	}

	public ApolloWidgetSearchParams addWidgetTypeIds(String... widgetTypeIds) {
		this.widgetTypeIds = super.addValues(this.widgetTypeIds, widgetTypeIds);

		return this;
	}

	public EnumSet<ApolloWidgetTypeCategories> getCategories() {
		return categories;
	}

	public void setCategories(EnumSet<ApolloWidgetTypeCategories> categories) {
		this.categories = categories;
	}

	public EnumSet<IconTypes> getIconTypes() {
		return iconTypes;
	}

	public void setIconTypes(EnumSet<IconTypes> iconTypes) {
		this.iconTypes = iconTypes;
	}
}
