package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class WidgetConfigurationRepositoryImpl extends RepositoryExtensionAbstract<WidgetConfiguration>
        implements WidgetConfigurationRepositoryExtension {

	public WidgetConfigurationRepositoryImpl() {
		super(WidgetConfiguration.class);
	}

	@Override
	public Page<WidgetConfiguration> findWidgetConfigurations(Pageable pageable,
	        WidgetConfigurationSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<WidgetConfiguration> findWidgetConfigurations(WidgetConfigurationSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(WidgetConfigurationSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "widgetId", params.getWidgetIds());
		}
		return criteria;
	}
}
