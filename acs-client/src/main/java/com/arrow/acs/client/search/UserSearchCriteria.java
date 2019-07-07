package com.arrow.acs.client.search;

import com.arrow.acs.client.model.UserStatus;

public class UserSearchCriteria extends SearchCriteria {
	private static final String LOGIN = "login";
	private static final String COMPANY_HID = "companyHid";
	private static final String STATUS = "status";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String SIP_URI = "sipUri";
	private static final String EMAIL = "email";
	private static final String ROLE_HIDS = "roleHids";
	private PageSearchCriteria pageSearchCriteria = new PageSearchCriteria();

	public UserSearchCriteria withLogin(String login) {
		simpleCriteria.put(LOGIN, login);
		return this;
	}

	public UserSearchCriteria withCompanyHid(String companyHid) {
		simpleCriteria.put(COMPANY_HID, companyHid);
		return this;
	}

	public UserSearchCriteria withStatus(UserStatus status) {
		simpleCriteria.put(STATUS, status.name());
		return this;
	}

	public UserSearchCriteria withFirstName(String firstName) {
		simpleCriteria.put(FIRST_NAME, firstName);
		return this;
	}

	public UserSearchCriteria withLastName(String lastName) {
		simpleCriteria.put(LAST_NAME, lastName);
		return this;
	}

	public UserSearchCriteria withSipUri(String sipUri) {
		simpleCriteria.put(SIP_URI, sipUri);
		return this;
	}

	public UserSearchCriteria withEmail(String email) {
		simpleCriteria.put(EMAIL, email);
		return this;
	}

	public UserSearchCriteria withRoleHids(String... roleHids) {
		arrayCriteria.put(ROLE_HIDS, roleHids);
		return this;
	}

	public UserSearchCriteria withPage(long page) {
		pageSearchCriteria.withPage(page);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}

	public UserSearchCriteria withSize(long size) {
		pageSearchCriteria.withSize(size);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}
}
