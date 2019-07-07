package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.repo.params.PrivilegeSearchParams;

public interface PrivilegeRepositoryExtension extends RepositoryExtension<Privilege> {

	public Page<Privilege> findPrivileges(Pageable pageable, PrivilegeSearchParams params);

	public List<Privilege> findPrivileges(PrivilegeSearchParams params);
}
