package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface SoftwareReleaseTransRepositoryExtension extends RepositoryExtension<SoftwareReleaseTrans> {

	public Page<SoftwareReleaseTrans> findSoftwareReleaseTrans(Pageable pageable,
	        SoftwareReleaseTransSearchParams params);

	public List<SoftwareReleaseTrans> findSoftwareReleaseTrans(SoftwareReleaseTransSearchParams params);

	public long findSoftwareReleaseTransCount(SoftwareReleaseTransSearchParams params);
}