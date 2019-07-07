package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.repo.params.PrivilegeSearchParams;

public class PrivilegeRepositoryExtensionImpl extends RepositoryExtensionAbstract<Privilege>
        implements PrivilegeRepositoryExtension {

	public PrivilegeRepositoryExtensionImpl() {
		super(Privilege.class);
	}

	public Page<Privilege> findPrivileges(Pageable pageable, PrivilegeSearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findPrivileges";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	public List<Privilege> findPrivileges(PrivilegeSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findPrivileges";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(PrivilegeSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();

		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
		criteria = addCriteria(criteria, "hidden", params.getHidden());
		criteria = addCriteria(criteria, "productId", params.getProductIds());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		if (params.getEnabled() != null)
			criteria = addCriteria(criteria, "enabled", params.getEnabled());

		if (!StringUtils.isEmpty(params.getSystemName()))
			criteria.add(Criteria.where("systemName").regex(params.getSystemName(), "i"));

		return criteria;
	}
}
