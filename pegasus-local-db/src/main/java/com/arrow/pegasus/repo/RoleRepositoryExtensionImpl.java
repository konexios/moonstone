package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.repo.params.RoleSearchParams;

public class RoleRepositoryExtensionImpl extends RepositoryExtensionAbstract<Role> implements RoleRepositoryExtension {

	public RoleRepositoryExtensionImpl() {
		super(Role.class);
	}

	/**
	 * This method was implemented prior to the implementation of
	 * RoleSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findRoles(com.arrow.pegasus.repo.params.RoleSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<Role> findByApplicationIds(String... applicationIds) {
		String methodName = "findByApplicationIds";
		logInfo(methodName, "...");

		RoleSearchParams params = new RoleSearchParams();
		params.addApplicationIds(applicationIds);

		return findRoles(params);
	}

	/**
	 * This method was implemented prior to the implementation of
	 * RoleSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findRoles(com.arrow.pegasus.repo.params.RoleSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<Role> findByApplicationIdsAndEnabled(boolean enabled, String... applicationIds) {
		String methodName = "findByApplicationIdsAndEnabled";
		logInfo(methodName, "...");

		RoleSearchParams params = new RoleSearchParams();
		params.setEnabled(enabled);
		params.addApplicationIds(applicationIds);

		return findRoles(params);
	}

	public Page<Role> findRoles(Pageable pageable, RoleSearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findRoles";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	public List<Role> findRoles(RoleSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findRoles";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(RoleSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();

		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
		criteria = addCriteria(criteria, "enabled", params.getEnabled());
		criteria = addCriteria(criteria, "editable", params.getEditable());
		criteria = addCriteria(criteria, "hidden", params.getHidden());
		criteria = addCriteria(criteria, "productId", params.getProductIds());
		criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
		criteria = addCriteria(criteria, "privilegeIds", params.getPrivilegeIds());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		return criteria;
	}
}
