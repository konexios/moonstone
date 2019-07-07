package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.repo.params.TempTokenSearchParams;

public class TempTokenRepositoryExtensionImpl extends RepositoryExtensionAbstract<TempToken>
        implements TempTokenRepositoryExtension {

	public TempTokenRepositoryExtensionImpl() {
		super(TempToken.class);
	}

	public TempToken findByHid(String hid) {

		TempTokenSearchParams params = new TempTokenSearchParams();
		params.addHids(hid);

		List<TempToken> result = doProcessQuery(buildCriteria(params));

		return (result.size() == 1 ? result.get(0) : null);
	}

	@Override
	public Page<TempToken> findBy(Pageable pageable, TempTokenSearchParams params) {
		String methodName = "findBy";
		logInfo(methodName, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<TempToken> findBy(TempTokenSearchParams params) {
		String methodName = "findBy";
		logInfo(methodName, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(TempTokenSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "properties.applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "properties.companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "properties.softwareReleaseTransId", params.getSoftwareReleaseTransIds());
			criteria = addCriteria(criteria, "expired", params.getExpired());
		}
		return criteria;
	}
}
