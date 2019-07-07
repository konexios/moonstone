package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.DeviceActionType;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class DeviceActionTypeRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceActionType>
        implements DeviceActionTypeRepositoryExtension {

	public DeviceActionTypeRepositoryExtensionImpl() {
		super(DeviceActionType.class);
	}

	@Override
	public Page<DeviceActionType> findDeviceActionTypes(Pageable pageable, KronosDocumentSearchParams params) {
		String methodName = "findDeviceActionTypes";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
		}

		return doProcessQuery(pageable, criteria);
	}

	@Override
	public List<DeviceActionType> findByApplicationIdAndSystemNames(String applicationId, String[] systemNames) {
		String methodName = "findByApplicationIdAndSystemNames";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria = addCriteria(criteria, "applicationId", applicationId);
		criteria = addCriteria(criteria, "systemName", systemNames);

		return doProcessQuery(criteria);
	}
}
