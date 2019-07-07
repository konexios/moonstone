package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.SoftwareRelease;

public interface SoftwareReleaseRepositoryExtension extends RepositoryExtension<SoftwareRelease> {
	Page<SoftwareRelease> findSoftwareReleases(Pageable pageable, SoftwareReleaseSearchParams params);

	List<SoftwareRelease> findSoftwareReleases(SoftwareReleaseSearchParams params);
}
