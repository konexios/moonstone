package com.arrow.pegasus.data.profile;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "user")
public class User extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -4747070194723478579L;

	private final static UserStatus DEFAULT_USER_STATUS = UserStatus.Active;
	private final static boolean DEFAULT_ADMIN = false;

	@NotBlank
	private String login;
	@NotBlank
	@Indexed(unique = true)
	private String hashedLogin;
	@NotBlank
	private String password;
	@NotBlank
	private String salt;
	private UserStatus status = DEFAULT_USER_STATUS;
	@NotBlank
	@Indexed
	private String companyId;
	private String parentUserId;
	private boolean admin = DEFAULT_ADMIN;
	private int failedLogins = 0;
	private long accountLockTimeout = 0;
	private Contact contact;
	private Address address;
	private List<String> roleIds = new ArrayList<>();
	private List<UserAuth> auths = new ArrayList<>();
	private List<String> oldPasswords = new ArrayList<>();
	private List<UserEULA> eulas = new ArrayList<>();

	@Transient
	@JsonIgnore
	private List<Role> refRoles = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Company refCompany;

	public long getAccountLockTimeout() {
		return accountLockTimeout;
	}

	public void setAccountLockTimeout(long accountLockTimeout) {
		this.accountLockTimeout = accountLockTimeout;
	}

	public List<UserAuth> getAuths() {
		return auths;
	}

	public void setAuths(List<UserAuth> auths) {
		this.auths = auths;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Role> getRefRoles() {
		return refRoles;
	}

	public void setRefRoles(List<Role> refRoles) {
		this.refRoles = refRoles;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getHashedLogin() {
		return hashedLogin;
	}

	public void setHashedLogin(String hashedLogin) {
		this.hashedLogin = hashedLogin;
	}

	public List<String> getOldPasswords() {
		return oldPasswords;
	}

	public void setOldPasswords(List<String> oldPasswords) {
		this.oldPasswords = oldPasswords;
	}

	public int getFailedLogins() {
		return failedLogins;
	}

	public void setFailedLogins(int failedLogins) {
		this.failedLogins = failedLogins;
	}

	public UserAuth findFirstEnabledAuth() {
		if (auths != null)
			for (UserAuth auth : auths)
				if (auth.isEnabled())
					return auth;
		return null;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.USER;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public List<UserEULA> getEulas() {
		return eulas;
	}

	public void setEulas(List<UserEULA> eulas) {
		this.eulas = eulas;
	}
}
