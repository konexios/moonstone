package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class SocialEventRegistrationRepositoryExtensionImpl extends RepositoryExtensionAbstract<SocialEventRegistration> implements SocialEventRegistrationRepositoryExtension {
	public SocialEventRegistrationRepositoryExtensionImpl() {
		super(SocialEventRegistration.class);
	}

	@Override
	public Page<SocialEventRegistration> findSocialEventRegistrations(Pageable pageable, SocialEventRegistrationSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SocialEventRegistration> findSocialEventRegistrations(SocialEventRegistrationSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}
	
	private List<Criteria> buildCriteria(SocialEventRegistrationSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "socialEventId", params.getSocialEventIds());
			criteria = addCriteria(criteria, "name", params.getNames());
			criteria = addCriteria(criteria, "email", params.getEmails());
			criteria = addCriteria(criteria, "status", params.getStatuses());
			criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
			criteria = addCriteria(criteria, "origEmail", params.getOrigEmails());
			
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
	
	@Override
	public List<SocialEventRegistration> findCaseInsensitiveByEmail(String email)
	{
		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("email").regex(Pattern.compile("^" + email + "$", Pattern.CASE_INSENSITIVE)));
		return doProcessQuery(criteria);
	}

}
