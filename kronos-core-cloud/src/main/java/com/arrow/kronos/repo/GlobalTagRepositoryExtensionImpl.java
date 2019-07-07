package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.GlobalTag;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class GlobalTagRepositoryExtensionImpl extends RepositoryExtensionAbstract<GlobalTag>
        implements GlobalTagRepositoryExtension {

	public GlobalTagRepositoryExtensionImpl() {
		super(GlobalTag.class);
	}

	@Override
	public List<GlobalTag> findGlobalTags(GlobalTagRepositoryParams params, Sort sort) {
		String method = "findGlobalTags";
		logDebug(method, "...");
		return doFind(doProcessCriteria(buildCriteria(params)).with(sort));
	}

	@Override
	public List<GlobalTag> findGlobalTags(GlobalTagRepositoryParams params) {
		String method = "findGlobalTags";
		logDebug(method, "...");
		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public Page<GlobalTag> findGlobalTags(Pageable pageable, GlobalTagRepositoryParams params) {
		String method = "findGlobalTags";
		logDebug(method, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	private List<Criteria> buildCriteria(GlobalTagRepositoryParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			if (!StringUtils.isEmpty(params.getName())) {
				criteria.add(Criteria.where("name").regex(params.getName(), "i"));
			}
			if (params.getTagTypes() != null) {
				criteria = addCriteria(criteria, "tagType", params.getTagTypes());
			}
			if (params.getCreatedDateFrom() != null || params.getCreatedDateTo() != null) {
				criteria = addCriteria(criteria, "createdDate", params.getCreatedDateFrom(), params.getCreatedDateTo());
			}
			criteria = addCriteria(criteria, "objectType", params.getObjectTypes());
			criteria = addCriteria(criteria, "id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
		}
		return criteria;
	}
}
