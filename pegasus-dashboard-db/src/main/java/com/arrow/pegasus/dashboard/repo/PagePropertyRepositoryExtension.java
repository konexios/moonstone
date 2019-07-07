package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface PagePropertyRepositoryExtension extends RepositoryExtension<PageProperty> {

	public Page<PageProperty> findPageProperties(Pageable pageable, PagePropertySearchParams params);

	public List<PageProperty> findPageProperties(PagePropertySearchParams params);

	public List<PageProperty> findPageProperties(PagePropertySearchParams params, Sort sort);
}
