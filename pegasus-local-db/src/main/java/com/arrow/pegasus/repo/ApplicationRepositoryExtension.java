package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;

public interface ApplicationRepositoryExtension extends RepositoryExtension<Application> {

	public List<Application> findApplications(ApplicationSearchParams params);
	
    public Page<Application> findApplications(Pageable pageable, ApplicationSearchParams params);

    public List<Application> findByProductIdOrProductExtensionId(String productId, Boolean enabled, String... companyIds);

    public long findApplicationCount(ApplicationSearchParams params);
}
