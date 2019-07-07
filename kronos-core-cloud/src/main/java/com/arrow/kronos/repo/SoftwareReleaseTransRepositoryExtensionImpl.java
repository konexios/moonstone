package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class SoftwareReleaseTransRepositoryExtensionImpl extends RepositoryExtensionAbstract<SoftwareReleaseTrans>
        implements SoftwareReleaseTransRepositoryExtension {

	public SoftwareReleaseTransRepositoryExtensionImpl() {
		super(SoftwareReleaseTrans.class);
	}

	@Override
	public Page<SoftwareReleaseTrans> findSoftwareReleaseTrans(Pageable pageable,
	        SoftwareReleaseTransSearchParams params) {
		String method = "findSoftwareReleaseTrans";
		logInfo(method, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SoftwareReleaseTrans> findSoftwareReleaseTrans(SoftwareReleaseTransSearchParams params) {
		String method = "findSoftwareReleaseTrans";
		logInfo(method, "...");
		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public long findSoftwareReleaseTransCount(SoftwareReleaseTransSearchParams params) {
		String method = "findSoftwareReleaseTransCount";
		logDebug(method, "...");
		return doCount(doProcessCriteria(buildCriteria(params)));
	}

	private List<Criteria> buildCriteria(SoftwareReleaseTransSearchParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
			criteria = addCriteria(criteria, "deviceCategory", params.getDeviceCategories());
			criteria = addCriteria(criteria, "softwareReleaseScheduleId", params.getSoftwareReleaseScheduleIds());
			criteria = addCriteria(criteria, "fromSoftwareReleaseId", params.getFromSoftwareReleaseIds());
			criteria = addCriteria(criteria, "toSoftwareReleaseId", params.getToSoftwareReleaseIds());
			criteria = addCriteria(criteria, "status", params.getStatuses());
			criteria = addCriteria(criteria, "relatedSoftwareReleaseTransId",
			        params.getRelatedSoftwareReleaseTransIds());
			if (!StringUtils.isEmpty(params.getError()))
				criteria.add(Criteria.where("error").regex(params.getError(), "i"));

			// not in
			criteria = addNotCriteria(criteria, "softwareReleaseScheduleId", params.getNotSoftwareReleaseScheduleIds());
		}
		return criteria;
	}
}