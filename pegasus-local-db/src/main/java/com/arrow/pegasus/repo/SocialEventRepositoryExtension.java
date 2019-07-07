package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.pegasus.data.SocialEvent;

public interface SocialEventRepositoryExtension extends RepositoryExtension<SocialEvent> {

	Page<SocialEvent> findSocialEvents(Pageable pageable, SocialEventSearchParams params);

	List<SocialEvent> findSocialEvents(SocialEventSearchParams params, Sort sort);

	List<SocialEvent> findSocialEvents(SocialEventSearchParams params);

}
