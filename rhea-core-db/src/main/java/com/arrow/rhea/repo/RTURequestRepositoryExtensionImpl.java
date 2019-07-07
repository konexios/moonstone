package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.RTURequest;

public class RTURequestRepositoryExtensionImpl extends RepositoryExtensionAbstract<RTURequest>
        implements RTURequestRepositoryExtension {
	public RTURequestRepositoryExtensionImpl() {
		super(RTURequest.class);
	}

	private List<Criteria> buildCriteria(RTURequestSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "status", params.getStatuses());
			criteria = addCriteria(criteria, "softwareReleaseId", params.getSoftwareReleaseIds());
		}
		return criteria;
	}

	@Override
	public Page<RTURequest> findRTURequests(Pageable pageable, RTURequestSearchParams params) {
		String methodName = "findRTURequests";
		logInfo(methodName, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<RTURequest> findRTURequests(RTURequestSearchParams params) {
		String methodName = "findRTURequests";
		logInfo(methodName, "...");
		return doProcessQuery(buildCriteria(params));
	}
}
