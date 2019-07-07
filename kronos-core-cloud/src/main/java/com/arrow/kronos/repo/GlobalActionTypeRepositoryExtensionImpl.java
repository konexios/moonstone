package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class GlobalActionTypeRepositoryExtensionImpl extends RepositoryExtensionAbstract<GlobalActionType>
        implements GlobalActionTypeRepositoryExtension {

	public GlobalActionTypeRepositoryExtensionImpl() {
		super(GlobalActionType.class);
	}

	@Override
	public Page<GlobalActionType> findGlobalActionTypes(Pageable pageable, KronosDocumentSearchParams params) {
		String methodName = "findGlobalActionTypes";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}

		return doProcessQuery(pageable, criteria);
	}

}
