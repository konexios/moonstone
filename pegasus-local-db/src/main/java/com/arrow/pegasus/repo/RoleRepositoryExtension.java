package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.repo.params.RoleSearchParams;

public interface RoleRepositoryExtension extends RepositoryExtension<Role> {

	/**
	 * This method was implemented prior to the implementation of
	 * RoleSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findRoles(com.arrow.pegasus.repo.params.RoleSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<Role> findByApplicationIds(String... applicationIds);

	/**
	 * This method was implemented prior to the implementation of
	 * RoleSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findRoles(com.arrow.pegasus.repo.params.RoleSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<Role> findByApplicationIdsAndEnabled(boolean enabled, String... applicationIds);

	public Page<Role> findRoles(Pageable pageable, RoleSearchParams params);

	public List<Role> findRoles(RoleSearchParams params);
}
