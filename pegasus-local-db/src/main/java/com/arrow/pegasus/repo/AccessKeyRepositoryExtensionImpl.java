package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;

public class AccessKeyRepositoryExtensionImpl extends RepositoryExtensionAbstract<AccessKey>
		implements AccessKeyRepositoryExtension {

	public AccessKeyRepositoryExtensionImpl() {
		super(AccessKey.class);
	}

	public List<AccessKey> findByPri(String pri) {
		Query query = new Query(Criteria.where("privileges.pri").is(pri));
		return getOperations().find(query, AccessKey.class);
	}

	public AccessKey findOwnerByPri(String pri) {
		Query query = new Query(
				Criteria.where("privileges.pri").is(pri).and("privileges.level").is(AccessLevel.OWNER.name()));
		List<AccessKey> accessKeys = getOperations().find(query, AccessKey.class);

		if (accessKeys == null || accessKeys.isEmpty())
			return null;

		return accessKeys.get(0);
	}

	public Page<AccessKey> findAccessKeys(Pageable pageable, AccessKeySearchParams params) {
		String methodName = "findAccessKeys";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "subscriptionId", params.getSubscriptionIds());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "name", params.getName());
			criteria = addCriteria(criteria, "privileges.level", params.getAccessLevels());
			criteria = addCriteria(criteria, "privileges.pri", params.getPri());
			criteria = addCriteria(criteria, "expiration", params.getExpirationDateFrom(),
					params.getExpirationDateTo());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "_id", params.getIds());
		}

		return doProcessQuery(pageable, criteria);
	}
}
