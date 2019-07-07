package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.Zone;

public class ZoneRepositoryExtensionImpl extends RepositoryExtensionAbstract<Zone> implements ZoneRepositoryExtension {

	public ZoneRepositoryExtensionImpl() {
		super(Zone.class);
	}

	@Override
	public List<Zone> findZones(ZoneSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findZones";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(ZoneSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();
		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
		criteria = addCriteria(criteria, "regionId", params.getRegionIds());
		criteria = addCriteria(criteria, "enabled", params.getEnabled());
		criteria = addCriteria(criteria, "hidden", params.getHidden());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		if (!StringUtils.isEmpty(params.getSystemName()))
			criteria.add(Criteria.where("sysytemName").regex(params.getSystemName(), "i"));

		return criteria;
	}
}
