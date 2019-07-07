package com.arrow.kronos.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface GlobalActionRepositoryExtension extends RepositoryExtension<GlobalAction>{

	Page<GlobalAction> findGlobalActions(Pageable pageable, KronosDocumentSearchParams params);
}
