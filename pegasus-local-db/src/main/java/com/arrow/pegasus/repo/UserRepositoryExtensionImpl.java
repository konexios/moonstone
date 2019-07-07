package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.repo.params.UserSearchParams;

public class UserRepositoryExtensionImpl extends RepositoryExtensionAbstract<User> implements UserRepositoryExtension {

	public UserRepositoryExtensionImpl() {
		super(User.class);
	}

	public User doFindByLogin(String login) {
		Query query = new Query(Criteria.where("hashedLogin").is(getCrypto().internalHash(login.toLowerCase())));
		return getOperations().findOne(query, User.class);
	}

	/**
	 * This method was implemented prior to the implementation of
	 * UserSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findUsers(com.arrow.pegasus.repo.params.UserSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<User> doFindByCompanyIdAndRoleIds(String companyId, String... roleIds) {

		UserSearchParams params = new UserSearchParams();
		if (companyId != null)
			params.addCompanyIds(companyId);
		if (roleIds != null)
			params.addRoleIds(roleIds);

		return findUsers(params);
	}

	/**
	 * This method was implemented prior to the implementation of
	 * UserSearchParams.
	 *
	 * @deprecated use
	 *             {@link #findUsers(org.springframework.data.domain.Pageable pageable, com.arrow.pegasus.repo.params.UserSearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public Page<User> findUsers(Pageable pageable, String companyId, String[] roleIds, EnumSet<UserStatus> statuses) {

		String methodName = "findUsers";
		logInfo(methodName, "...");

		UserSearchParams params = new UserSearchParams();
		if (companyId != null)
			params.addCompanyIds(companyId);
		if (roleIds != null)
			params.addRoleIds(roleIds);
		if (statuses != null)
			params.setStatuses(statuses);

		return findUsers(pageable, params);
	}

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
	        EnumSet<UserStatus> statuses, String firstName, String lastName) {
		String methodName = "findThemisUsers";
		logInfo(methodName, "...");

		UserSearchParams params = new UserSearchParams();
		if (companyId != null)
			params.addCompanyIds(companyId);
		if (roleIds != null)
			params.addRoleIds(roleIds);
		if (statuses != null)
			params.setStatuses(statuses);
		if (firstName != null)
			params.setFirstName(firstName);
		if (lastName != null)
			params.setLastName(lastName);

		return findUsers(pageable, params);
	}

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
	        String firstName, String lastName, String sipUri, String email) {
		String methodName = "findUsers";
		logInfo(methodName, "...");

		UserSearchParams params = new UserSearchParams();
		if (companyId != null)
			params.addCompanyIds(companyId);
		if (login != null)
			params.setLogin(login);
		if (statuses != null)
			params.setStatuses(statuses);
		if (firstName != null)
			params.setFirstName(firstName);
		if (lastName != null)
			params.setLastName(lastName);
		if (sipUri != null)
			params.setSipUri(sipUri);
		if (email != null)
			params.setEmail(email);

		return findUsers(pageable, params);
	}

	public Page<User> findUsers(Pageable pageable, UserSearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findUsers";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	public List<User> findUsers(UserSearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findUsers";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(UserSearchParams params) {

		List<Criteria> criteria = new ArrayList<>();

		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());

		if (StringUtils.isEmpty(params.getFirstNameLastNameAndLogin())) {
			if (!StringUtils.isEmpty(params.getFirstName()))
				criteria.add(Criteria.where("contact.firstName").regex(params.getFirstName(), "i"));

			if (!StringUtils.isEmpty(params.getLastName()))
				criteria.add(Criteria.where("contact.lastName").regex(params.getLastName(), "i"));

			if (!StringUtils.isEmpty(params.getLogin()))
				criteria.add(
				        Criteria.where("hashedLogin").is(getCrypto().internalHash(params.getLogin().toLowerCase())));
		} else {
			criteria.add(new Criteria().orOperator(
			        Criteria.where("contact.firstName").regex(params.getFirstNameLastNameAndLogin(), "i"),
			        Criteria.where("contact.lastName").regex(params.getFirstNameLastNameAndLogin(), "i"),
			        Criteria.where("hashedLogin")
			                .is(getCrypto().internalHash(params.getFirstNameLastNameAndLogin().toLowerCase()))));
		}

		if (!StringUtils.isEmpty(params.getEmail()))
			criteria.add(Criteria.where("contact.email").regex(params.getEmail(), "i"));

		if (!StringUtils.isEmpty(params.getSipUri()))
			criteria.add(Criteria.where("contact.sipUri").regex(params.getSipUri(), "i"));

		criteria = addCriteria(criteria, "companyId", params.getCompanyIds());
		criteria = addCriteria(criteria, "status", params.getStatuses());
		criteria = addCriteria(criteria, "admin", params.getAdmin());
		criteria = addCriteria(criteria, "roleIds", params.getRoleIds());

		return criteria;
	}
}
