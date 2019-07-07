package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class WidgetTypeRepositoryImpl extends RepositoryExtensionAbstract<WidgetType>
        implements WidgetTypeRepositoryExtension {

	public WidgetTypeRepositoryImpl() {
		super(WidgetType.class);
	}

	public Page<WidgetType> findWidgetTypes(Pageable pageable, WidgetTypeSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	public List<WidgetType> findWidgetTypes(WidgetTypeSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(WidgetTypeSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "productId", params.getProductIds());
			criteria = addCriteria(criteria, "category", params.getCategories());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}
		return criteria;
	}
}
