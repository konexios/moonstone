package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.DeviceStateTrans;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class DeviceStateTransRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceStateTrans>
        implements DeviceStateTransRepositoryExtension {

	public DeviceStateTransRepositoryExtensionImpl() {
		super(DeviceStateTrans.class);
	}
	
	@Override
	public Page<DeviceStateTrans> findDeviceStateTrans(Pageable pageable,
			DeviceStateTransSearchParams params) {
		String method = "findDeviceStateTrans";
		logInfo(method, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}
	
	private List<Criteria> buildCriteria(DeviceStateTransSearchParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
			criteria = addCriteria(criteria, "type", params.getTypes());
			criteria = addCriteria(criteria, "status", params.getStatuses());
			criteria = addCriteria(criteria, "createdDate", params.getCreatedDateFrom(), params.getCreatedDateTo());
			criteria = addCriteria(criteria, "lastModifiedDate", params.getUpdatedDateFrom(),
			        params.getUpdatedDateTo());
		}
		return criteria;
	}
}