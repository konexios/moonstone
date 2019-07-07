package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.SoftwareProduct;

public class SoftwareProductRepositoryExtensionImpl extends RepositoryExtensionAbstract<SoftwareProduct>
        implements SoftwareProductRepositoryExtension {
	public SoftwareProductRepositoryExtensionImpl() {
		super(SoftwareProduct.class);
	}

	@Override
	public Page<SoftwareProduct> findSoftwareProducts(Pageable pageable, SoftwareProductSearchParams params) {
		String methodName = "findSoftwareReleases";
		logInfo(methodName, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SoftwareProduct> findSoftwareProducts(SoftwareProductSearchParams params) {
		String methodName = "findSoftwareReleases";
		logInfo(methodName, "...");
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(SoftwareProductSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			criteria = addCriteria(criteria, "editable", params.getEditable());
			criteria = addCriteria(criteria, "softwareVendorId", params.getSoftwareVendorIds());
			criteria = addCriteria(criteria, "name", params.getName());
		}
		return criteria;
	}
}
