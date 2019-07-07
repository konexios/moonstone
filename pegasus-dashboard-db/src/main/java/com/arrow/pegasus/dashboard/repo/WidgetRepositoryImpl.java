package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class WidgetRepositoryImpl extends RepositoryExtensionAbstract<Widget> implements WidgetRepositoryExtension {

	public WidgetRepositoryImpl() {
		super(Widget.class);
	}

	@Override
	public Page<Widget> findWidgets(Pageable pageable, WidgetSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<Widget> findWidgets(WidgetSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(WidgetSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "parentId", params.getParentIds());
			criteria = addCriteria(criteria, "widgetTypeId", params.getWidgetTypeIds());
		}
		return criteria;
	}
}
