package com.arrow.kronos.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface GlobalActionTypeRepositoryExtension extends RepositoryExtension<GlobalActionType> {

	Page<GlobalActionType> findGlobalActionTypes(Pageable pageable, KronosDocumentSearchParams params);
}
