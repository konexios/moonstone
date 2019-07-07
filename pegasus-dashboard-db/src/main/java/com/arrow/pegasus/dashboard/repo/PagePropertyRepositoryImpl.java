package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class PagePropertyRepositoryImpl extends RepositoryExtensionAbstract<PageProperty>
        implements PagePropertyRepositoryExtension {
	public PagePropertyRepositoryImpl() {
		super(PageProperty.class);
	}

	@Override
	public Page<PageProperty> findPageProperties(Pageable pageable, PagePropertySearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<PageProperty> findPageProperties(PagePropertySearchParams params) {
		Assert.notNull(params, "params is null");

		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public List<PageProperty> findPageProperties(PagePropertySearchParams params, Sort sort) {
		Assert.notNull(params, "params is null");
		Assert.notNull(sort, "sort is null");

		return doFind(doProcessCriteria(buildCriteria(params)).with(sort));
	}

	private List<Criteria> buildCriteria(PagePropertySearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "configurationPageId", params.getConfigurationPageIds());
		}
		return criteria;
	}
}
