package com.arrow.pegasus.repo;

import java.util.EnumSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.repo.params.UserSearchParams;

public interface UserRepositoryExtension extends RepositoryExtension<User> {

	public User doFindByLogin(String login);

	/**
	 * This method was implemented prior to the implementation of
	 * UserSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findUsers(com.arrow.pegasus.repo.params.UserSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<User> doFindByCompanyIdAndRoleIds(String companyId, String... roleIds);

	/**
	 * This method was implemented prior to the implementation of
	 * UserSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findUsers(org.springframework.data.domain.Pageable pageable, com.arrow.pegasus.repo.params.UserSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public Page<User> findUsers(Pageable pageable, String applicationId, String[] roleIds,
	        EnumSet<UserStatus> statuses);

	/**
	 * This method was implemented prior to the implementation of
	 * UserSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findUsers(org.springframework.data.domain.Pageable pageable, com.arrow.pegasus.repo.params.UserSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public Page<User> findThemisUsers(Pageable pageable, String companyId, String[] roleIds,
	        EnumSet<UserStatus> statuses, String firstName, String lastName);

	/**
	 * This method was implemented prior to the implementation of
	 * UserSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findUsers(org.springframework.data.domain.Pageable pageable, com.arrow.pegasus.repo.params.UserSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public Page<User> findUsers(Pageable pageable, String companyId, String login, EnumSet<UserStatus> statuses,
	        String firstName, String lastName, String sipUri, String email);

	public Page<User> findUsers(Pageable pageable, UserSearchParams params);

	public List<User> findUsers(UserSearchParams params);
}
