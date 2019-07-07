package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.SoftwareRelease;

public class SoftwareReleaseRepositoryExtensionImpl extends RepositoryExtensionAbstract<SoftwareRelease>
        implements SoftwareReleaseRepositoryExtension {
	public SoftwareReleaseRepositoryExtensionImpl() {
		super(SoftwareRelease.class);
	}

	@Override
	public Page<SoftwareRelease> findSoftwareReleases(Pageable pageable, SoftwareReleaseSearchParams params) {
		String methodName = "findSoftwareReleases";
		logInfo(methodName, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SoftwareRelease> findSoftwareReleases(SoftwareReleaseSearchParams params) {
		String methodName = "findSoftwareReleases";
		logInfo(methodName, "...");
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(SoftwareReleaseSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "softwareProductId", params.getSoftwareProductIds());
			criteria = addCriteria(criteria, "noLongerSupported", params.getNoLongerSupported());
			criteria = addCriteria(criteria, "deviceTypeIds", params.getDeviceTypeIds());
			criteria = addCriteria(criteria, "upgradeableFromIds", params.getUpgradeableFromIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			criteria = addCriteria(criteria, "rtuType", params.getRightToUseTypes());
		}
		return criteria;
	}
}
