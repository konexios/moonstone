package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.data.location.LastLocation;

public class LastLocationRepositoryExtensionImpl extends RepositoryExtensionAbstract<LastLocation>
        implements LastLocationRepositoryExtension {

	public LastLocationRepositoryExtensionImpl() {
		super(LastLocation.class);
	}

	public List<LastLocation> findLastLocations(LastLocationSearchParams params) {
		String methodName = "findLastLocations";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "objectType", params.getObjectTypes());
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
		}

		return doFind(doProcessCriteria(criteria));
	}
}
