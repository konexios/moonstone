package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.DeviceType;

public class DeviceTypeRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceType>
        implements DeviceTypeRepositoryExtension {

	public DeviceTypeRepositoryExtensionImpl() {
		super(DeviceType.class);
	}

	@Override
	public Page<DeviceType> findDeviceTypes(Pageable pageable, DeviceTypeSearchParams params) {
		String methodName = "findDeviceTypes";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			criteria = addCriteria(criteria, "deviceProductId", params.getDeviceProductIds());
		}

		return doProcessQuery(pageable, criteria);
	}

	@Override
	public List<DeviceType> doFindAllByIdsCompanyIdAndEnabled(List<String> ids, String companyId, boolean enabled) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria = addCriteria(criteria, "id", ids);
		criteria = addCriteria(criteria, "companyId", companyId);
		criteria = addCriteria(criteria, "enabled", enabled);

		Query query = doProcessCriteria(criteria);

		return doFind(query);
	}

	public List<DeviceType> findByCompanyIdAndNames(String companyId, String[] names) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria = addCriteria(criteria, "name", names);
		criteria = addCriteria(criteria, "companyId", companyId);

		Query query = doProcessCriteria(criteria);

		return doFind(query);
	}

	@Override
	public List<DeviceType> findDeviceTypes(DeviceTypeSearchParams params) {
		String methodName = "findDeviceTypes";
		logInfo(methodName, "...");
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(DeviceTypeSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			criteria = addCriteria(criteria, "deviceProductId", params.getDeviceProductIds());
			criteria = addCriteria(criteria, "name", params.getNames());
		}
		return criteria;
	}
}
