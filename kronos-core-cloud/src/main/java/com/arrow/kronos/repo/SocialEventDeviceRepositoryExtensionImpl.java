package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.SocialEventDevice;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class SocialEventDeviceRepositoryExtensionImpl extends RepositoryExtensionAbstract<SocialEventDevice> implements SocialEventDeviceRepositoryExtension {
	public SocialEventDeviceRepositoryExtensionImpl() {
		super(SocialEventDevice.class);
	}

	@Override
	public Page<SocialEventDevice> findSocialEventDevices(Pageable pageable, SocialEventDeviceSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SocialEventDevice> findSocialEventDevices(SocialEventDeviceSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}
	
	private List<Criteria> buildCriteria(SocialEventDeviceSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			
			criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
			criteria = addCriteria(criteria, "deviceTypeId", params.getDeviceTypeIds());
			criteria = addCriteria(criteria, "macAddress", params.getMacAddresses());
			
			if (params.getCreatedBefore() != null && params.getCreatedAfter() != null) {
				criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), params.getCreatedBefore());
			} else if (params.getCreatedBefore() != null) {
				criteria = addCriteria(criteria, "createdDate", null, params.getCreatedBefore());
			} else if (params.getCreatedAfter() != null) {
				criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), null);
			}
			
			if (params.getUpdatedBefore() != null && params.getUpdatedAfter() != null) {
				criteria = addCriteria(criteria, "lastModifiedDate", params.getUpdatedAfter(),
				        params.getUpdatedBefore());
			} else if (params.getUpdatedBefore() != null) {
				criteria = addCriteria(criteria, "lastModifiedDate", null, params.getUpdatedBefore());
			} else if (params.getUpdatedAfter() != null) {
				criteria = addCriteria(criteria, "lastModifiedDate", params.getUpdatedAfter(), null);
			}
		}
		return criteria;
	}
}
