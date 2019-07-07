package com.arrow.rhea.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.SoftwareVendor;

public interface SoftwareVendorRepositoryExtension extends RepositoryExtension<SoftwareVendor> {
	Page<SoftwareVendor> findSoftwareVendors(Pageable pageable, SoftwareVendorSearchParams params);
}