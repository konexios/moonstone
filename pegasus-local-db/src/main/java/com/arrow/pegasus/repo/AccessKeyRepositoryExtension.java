package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;

public interface AccessKeyRepositoryExtension extends RepositoryExtension<AccessKey> {
	public List<AccessKey> findByPri(String pri);

	public AccessKey findOwnerByPri(String pri);

	public Page<AccessKey> findAccessKeys(Pageable pageable, AccessKeySearchParams params);
}
