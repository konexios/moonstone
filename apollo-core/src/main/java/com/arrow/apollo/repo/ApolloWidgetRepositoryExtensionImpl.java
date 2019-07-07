package com.arrow.apollo.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.apollo.data.ApolloWidget;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class ApolloWidgetRepositoryExtensionImpl extends RepositoryExtensionAbstract<ApolloWidget>
        implements ApolloWidgetRepositoryExtension {

	public ApolloWidgetRepositoryExtensionImpl() {
		super(ApolloWidget.class);
	}

	@Override
	public long countApolloWidgets(ApolloWidgetSearchParams params) {
		return doCount(doProcessCriteria(buildCriteria(params)));
	}

	@Override
	public List<ApolloWidget> findApolloWidgets(ApolloWidgetSearchParams params, Sort sort) {
		return doFind(doProcessCriteria(buildCriteria(params)).with(sort));
	}

	private List<Criteria> buildCriteria(ApolloWidgetSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "id", params.getIds());
			criteria = addCriteria(criteria, "widgetTypeId", params.getWidgetTypeIds());
			criteria = addCriteria(criteria, "category", params.getCategories());
			criteria = addCriteria(criteria, "iconType", params.getIconTypes());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}
		return criteria;
	}
}