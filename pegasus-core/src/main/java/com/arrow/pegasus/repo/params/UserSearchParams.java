package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.profile.UserStatus;

public class UserSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 6100650158368810487L;

	private String firstName;
	private String lastName;
	private String login;
	private String email;
	private String sipUri;
	private Set<String> companyIds;
	private EnumSet<UserStatus> statuses;
	private Boolean admin;
	private Set<String> roleIds;
	private String firstNameLastNameAndLogin;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSipUri() {
		return sipUri;
	}

	public void setSipUri(String sipUri) {
		this.sipUri = sipUri;
	}

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public UserSearchParams addCompanyIds(String... companyIds) {
		this.companyIds = super.addValues(this.companyIds, companyIds);

		return this;
	}

	public EnumSet<UserStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<UserStatus> statuses) {
		this.statuses = statuses;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getFirstNameLastNameAndLogin() {
		return firstNameLastNameAndLogin;
	}

	public void setFirstNameLastNameAndLogin(String firstNameLastNameAndLogin) {
		this.firstNameLastNameAndLogin = firstNameLastNameAndLogin;
	}

	public Set<String> getRoleIds() {
		return roleIds;
	}

	public void addRoleIds(String... roleIds) {
		this.roleIds = super.addValues(this.roleIds, roleIds);
	}
}
