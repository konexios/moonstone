package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface SocialEventRegistrationRepositoryExtension extends RepositoryExtension<SocialEventRegistration> {

	public Page<SocialEventRegistration> findSocialEventRegistrations(Pageable pageable,
			SocialEventRegistrationSearchParams params);

	public List<SocialEventRegistration> findSocialEventRegistrations(SocialEventRegistrationSearchParams params);

	public List<SocialEventRegistration> findCaseInsensitiveByEmail(String email);
}
