package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.ApplicationEngine;

public class ApplicationEngineRepositoryExtensionImpl extends RepositoryExtensionAbstract<ApplicationEngine>
        implements ApplicationEngineRepositoryExtension {

	public ApplicationEngineRepositoryExtensionImpl() {
		super(ApplicationEngine.class);
	}

	@Override
	public List<ApplicationEngine> findApplicationEngines(ApplicationEngineSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findApplicationEngines";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(ApplicationEngineSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();
		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
		criteria = addCriteria(criteria, "enabled", params.getEnabled());
		criteria = addCriteria(criteria, "productId", params.getProductIds());
		criteria = addCriteria(criteria, "zoneId", params.getZoneIds());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		return criteria;
	}
}
