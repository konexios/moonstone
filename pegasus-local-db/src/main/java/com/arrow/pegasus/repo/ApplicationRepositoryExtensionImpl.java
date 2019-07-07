package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;

public class ApplicationRepositoryExtensionImpl extends RepositoryExtensionAbstract<Application>
		implements ApplicationRepositoryExtension {

	public ApplicationRepositoryExtensionImpl() {
		super(Application.class);
	}

	@Override
	public List<Application> findApplications(ApplicationSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findApplications";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public Page<Application> findApplications(Pageable pageable, ApplicationSearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findApplications";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCriteria(params));
		// String method = "findApplications";
		// logInfo(method, "...");
		// List<Criteria> criteria = new ArrayList<Criteria>();
		// if (params != null) {
		// criteria = addCriteria(criteria, "companyId",
		// params.getCompanyIds());
		// criteria = addCriteria(criteria, "productId",
		// params.getProductIds());
		// criteria = addCriteria(criteria, "subscriptionId",
		// params.getSubscriptionIds());
		// criteria = addCriteria(criteria, "code", params.getCodes());
		// // if (!params.includeDisabled()) {
		// // criteria = addCriteria(criteria, "enabled", true);
		// // }
		// }
		// return doProcessQuery(pageable, criteria);
	}

	public List<Application> findByProductIdOrProductExtensionId(String productId, Boolean enabled,
			String... companyIds) {
		Assert.hasText(productId, "productId is empty");

		String method = "findByProductIdOrProductExtensionId";
		logInfo(method, "...");

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(new Criteria().orOperator(Criteria.where("productId").is(productId),
				Criteria.where("productExtensionIds").is(productId)));

		criteria = addCriteria(criteria, "companyId", companyIds);
		criteria = addCriteria(criteria, "enabled", enabled);

		return doFind(doProcessCriteria(criteria));
	}

	public long findApplicationCount(ApplicationSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findApplicationCount";
		logInfo(method, "...");

		List<Criteria> criteria = buildCriteria(params);
		return doCount(doProcessCriteria(criteria));
	}

	private List<Criteria> buildCriteria(ApplicationSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();

		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		if (!StringUtils.isEmpty(params.getCode()))
			criteria.add(Criteria.where("code").regex(params.getCode(), "i"));

		criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
		criteria = addCriteria(criteria, "subscriptionId", params.getSubscriptionIds());
		criteria = addCriteria(criteria, "zoneId", params.getZoneIds());
		criteria = addCriteria(criteria, "productId", params.getProductIds());
		criteria = addCriteria(criteria, "productExtensionIds", params.getProductExtensionIds());
		criteria = addCriteria(criteria, "apiSigningRequired", params.getApiSigningRequired());

		if (params.isEnabled() != null) {
			criteria = addCriteria(criteria, "enabled", params.isEnabled());
		}

		// special cases
		criteria = addCriteria(criteria, "code", params.getCodes());
		// if (!params.includeDisabled() && params.isEnabled() == null) {
		// criteria = addCriteria(criteria, "enabled", true);
		// }

		if (params.getCreatedBefore() != null) {
			criteria = addCriteria(criteria, "createdDate", null, params.getCreatedBefore());
		}

		return criteria;
	}
}
