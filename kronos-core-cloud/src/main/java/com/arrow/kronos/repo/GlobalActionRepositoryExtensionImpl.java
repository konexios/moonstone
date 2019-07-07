package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class GlobalActionRepositoryExtensionImpl extends RepositoryExtensionAbstract<GlobalAction>
        implements GlobalActionRepositoryExtension {

	public GlobalActionRepositoryExtensionImpl() {
		super(GlobalAction.class);
	}

	@Override
	public Page<GlobalAction> findGlobalActions(Pageable pageable, KronosDocumentSearchParams params) {
		String methodName = "findGlobalActions";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}

		return doProcessQuery(pageable, criteria);
	}

}
