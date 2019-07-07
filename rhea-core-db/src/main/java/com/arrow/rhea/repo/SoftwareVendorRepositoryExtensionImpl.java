package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.SoftwareVendor;

public class SoftwareVendorRepositoryExtensionImpl extends RepositoryExtensionAbstract<SoftwareVendor>
        implements SoftwareVendorRepositoryExtension {
	public SoftwareVendorRepositoryExtensionImpl() {
		super(SoftwareVendor.class);
	}

	@Override
	public Page<SoftwareVendor> findSoftwareVendors(Pageable pageable, SoftwareVendorSearchParams params) {
		String methodName = "findSoftwareVendors";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>(2);
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			criteria = addCriteria(criteria, "editable", params.getEditable());
		}

		return doProcessQuery(pageable, criteria);
	}
}