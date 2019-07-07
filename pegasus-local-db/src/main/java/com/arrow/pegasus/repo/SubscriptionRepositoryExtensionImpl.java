package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.repo.params.SubscriptionSearchParams;

public class SubscriptionRepositoryExtensionImpl extends RepositoryExtensionAbstract<Subscription>
        implements SubscriptionRepositoryExtension {

	public SubscriptionRepositoryExtensionImpl() {
		super(Subscription.class);
	}

	public Page<Subscription> findSubscriptions(Pageable pageable, SubscriptionSearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findSubscriptions";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	public List<Subscription> findSubscriptions(SubscriptionSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findSubscriptions";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	public long findSubscriptionCount(SubscriptionSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findSubscriptionCount";
		logInfo(method, "...");

		List<Criteria> criteria = buildCriteria(params);
		Query query = doProcessCriteria(criteria);
		return doCount(query);
	}

	private List<Criteria> buildCriteria(SubscriptionSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();

		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
		criteria = addCriteria(criteria, "enabled", params.isEnabled());

		if (params.getCreatedBefore() != null) {
			criteria = addCriteria(criteria, "createdDate", null, params.getCreatedBefore());
		}

		return criteria;
	}
}
