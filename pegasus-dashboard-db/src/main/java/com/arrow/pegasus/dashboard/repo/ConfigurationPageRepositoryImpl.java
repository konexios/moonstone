package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class ConfigurationPageRepositoryImpl extends RepositoryExtensionAbstract<ConfigurationPage>
        implements ConfigurationPageRepositoryExtension {

	public ConfigurationPageRepositoryImpl() {
		super(ConfigurationPage.class);
	}

	@Override
	public Page<ConfigurationPage> findConfigurationPages(Pageable pageable, ConfigurationPageSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<ConfigurationPage> findConfigurationPages(ConfigurationPageSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}
	
	@Override
	public List<ConfigurationPage> findConfigurationPages(ConfigurationPageSearchParams params, Sort sort) {
		return doFind(doProcessCriteria(buildCriteria(params)).with(sort));
	}

	private List<Criteria> buildCriteria(ConfigurationPageSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "widgetConfigurationId", params.getWidgetConfigurationIds());
		}
		return criteria;
	}
}
