package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.pegasus.data.SocialEvent;

public class SocialEventRepositoryExtensionImpl extends RepositoryExtensionAbstract<SocialEvent>
        implements SocialEventRepositoryExtension {

	public SocialEventRepositoryExtensionImpl() {
		super(SocialEvent.class);
	}

	@Override
	public Page<SocialEvent> findSocialEvents(Pageable pageable, SocialEventSearchParams params) {
		String method = "findSocialEvents";
		logInfo(method, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SocialEvent> findSocialEvents(SocialEventSearchParams params, Sort sort) {
		String method = "findSocialEvents";
		logInfo(method, "...");
		Query query = doProcessCriteria(buildCriteria(params)).with(sort);
		return doFind(query);
	}

	@Override
	public List<SocialEvent> findSocialEvents(SocialEventSearchParams params) {
		String method = "findSocialEvents";
		logInfo(method, "...");
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(SocialEventSearchParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			if (!StringUtils.isEmpty(params.getName())) {
				criteria.add(Criteria.where("name").regex(params.getName(), "i"));
			}
			if (params.getStartDateFrom() != null || params.getStartDateTo() != null) {
				criteria = addCriteria(criteria, "startDate", params.getStartDateFrom(), params.getStartDateTo());
			}
			if (params.getEndDateFrom() != null || params.getEndDateTo() != null) {
				criteria = addCriteria(criteria, "endDate", params.getEndDateFrom(), params.getEndDateTo());
			}
			if (params.getCreatedDateFrom() != null || params.getCreatedDateTo() != null) {
				criteria = addCriteria(criteria, "createdDate", params.getCreatedDateFrom(), params.getCreatedDateTo());
			}
			criteria = addCriteria(criteria, "zoneId", params.getZoneIds());
			criteria = addCriteria(criteria, "id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
		}
		return criteria;
	}
}
