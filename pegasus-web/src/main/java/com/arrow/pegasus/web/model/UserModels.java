package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.security.AuthType;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CoreUserModels;

public class UserModels extends CoreUserModels {

	public static class UserFilterOptions implements Serializable {
		private static final long serialVersionUID = -955216069550551696L;

		private List<CompanyModels.CompanyOption> companyOptions;
		private UserStatus[] statusOptions;

		public UserFilterOptions() {
		}

		public List<CompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public void setCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			this.companyOptions = companyOptions;
		}

		public UserFilterOptions withCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			setCompanyOptions(companyOptions);

			return this;
		}

		public UserStatus[] getStatusOptions() {
			return statusOptions;
		}

		public void setStatusOptions(UserStatus[] statusOptions) {
			this.statusOptions = statusOptions;
		}

		public UserFilterOptions withStatusOptions(UserStatus[] statusOptions) {
			setStatusOptions(statusOptions);

			return this;
		}
	}

	public static class UserRoleOption extends RoleModels.RoleOption {
		private static final long serialVersionUID = -3551627318198703200L;

		private String applicationName;
		private String description;
		private boolean enabled;

		public UserRoleOption(String applicationName, Role role) {
			super(role);
			this.applicationName = applicationName;
			this.description = role.getDescription();
			this.enabled = role.isEnabled();
		}

		public String getApplicationName() {
			return applicationName;
		}

		public String getDescription() {
			return description;
		}

		public boolean isEnabled() {
			return enabled;
		}
	}

	public static class UserStatusOption implements Serializable {
		private static final long serialVersionUID = 6728677940083899185L;

		private String id;
		private String name;

		public UserStatusOption(UserStatus userStatus) {
			this.id = userStatus.name();
			this.name = userStatus.name();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class UserOption implements Serializable {
		private static final long serialVersionUID = 3610982195114886571L;

		private String id;
		private String name;

		public UserOption(User user) {
			this.id = user.getId();
			this.name = user.getContact().getLastName() + ", " + user.getContact().getFirstName();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class UserCompanyOption extends UserOption {

		private static final long serialVersionUID = -7139068447812872730L;

		private String companyId;

		public UserCompanyOption(User user) {
			super(user);
			this.companyId = user.getCompanyId();
		}

		public String getCompanyId() {
			return companyId;
		}
	}

	public static class UserList extends CoreDocumentModel {
		private static final long serialVersionUID = 6846624439553260689L;

		private String firstName;
		private String lastName;
		private String fullName;
		private String login;
		private String companyName;
		private UserStatus status;
		private boolean admin;

		public UserList(User user, String decryptedLogin, Company company) {
			super(user.getId(), user.getHid());
			this.firstName = user.getContact().getFirstName();
			this.lastName = user.getContact().getLastName();
			this.fullName = user.getContact().fullName();
			this.login = decryptedLogin;
			this.companyName = company != null ? company.getName() : "UNKNOWN (" + user.getCompanyId() + ")";
			this.status = user.getStatus();
			this.admin = user.isAdmin();
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getFullName() {
			return fullName;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public UserList withLogin(String login) {
			setLogin(login);

			return this;
		}

		public String getCompanyName() {
			return companyName;
		}

		public UserStatus getStatus() {
			return status;
		}

		public boolean isAdmin() {
			return admin;
		}
	}

	public static class UserModel extends CoreDocumentModel {
		private static final long serialVersionUID = -5636360694194674160L;

		private String firstName;
		private String middleName;
		private String lastName;
		private String login;
		private UserStatus status;
		private boolean admin;
		private String office;
		private String extension;
		private String fax;
		private String cell;
		private String home;
		private String email;
		private String address1;
		private String address2;
		private String city;
		private String state;
		private String postalCode;
		private String country;
		private String companyId;
		private String parentUserId;
		private List<String> roleIds;
		private Instant lastLogin;

		public UserModel() {
			super(null, null);
		}

		public UserModel(User user, String decryptedLogin) {
			super(user.getId(), user.getHid());
			if (user.getContact() != null) {
				this.firstName = user.getContact().getFirstName();
				this.middleName = user.getContact().getMiddleName();
				this.lastName = user.getContact().getLastName();
				this.extension = user.getContact().getMonitorExt();
				this.fax = user.getContact().getFax();
				this.cell = user.getContact().getCell();
				this.home = user.getContact().getHome();
				this.email = user.getContact().getEmail();
				this.office = user.getContact().getOffice();
			}

			this.login = decryptedLogin;
			this.status = user.getStatus();
			this.admin = user.isAdmin();
			this.companyId = user.getCompanyId();
			this.roleIds = user.getRoleIds();
			this.parentUserId = user.getParentUserId();

			if (user.getAddress() != null) {
				this.address1 = user.getAddress().getAddress1();
				this.address2 = user.getAddress().getAddress2();
				this.city = user.getAddress().getCity();
				this.state = user.getAddress().getState();
				this.postalCode = user.getAddress().getZip();
				this.country = user.getAddress().getCountry();
			}
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public UserModel withLogin(String login) {
			setLastLogin(lastLogin);

			return this;
		}

		public UserStatus getStatus() {
			return status;
		}

		public boolean isAdmin() {
			return admin;
		}

		public String getOffice() {
			return office;
		}

		public String getExtension() {
			return extension;
		}

		public String getFax() {
			return fax;
		}

		public String getCell() {
			return cell;
		}

		public String getHome() {
			return home;
		}

		public String getEmail() {
			return email;
		}

		public String getCompanyId() {
			return companyId;
		}

		public String getParentUserId() {
			return parentUserId;
		}

		public List<String> getRoleIds() {
			return roleIds;
		}

		public String getMiddleName() {
			return middleName;
		}

		public String getAddress1() {
			return address1;
		}

		public String getAddress2() {
			return address2;
		}

		public String getCity() {
			return city;
		}

		public String getState() {
			return state;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public String getCountry() {
			return country;
		}

		public Instant getLastLogin() {
			return lastLogin;
		}

		public void setLastLogin(Instant lastLogin) {
			this.lastLogin = lastLogin;
		}

		public UserModel withLastLogin(Instant lastLogin) {
			setLastLogin(lastLogin);
			return this;
		}
	}

	public static class UserUpsert implements Serializable {
		private static final long serialVersionUID = -2708427682883841430L;

		private UserModel user;
		private List<UserStatusOption> statusOptions = new ArrayList<>();
		private List<CompanyOption> companyOptions = new ArrayList<>();
		private List<CompanyOption> companyRoleOptions = new ArrayList<>();
		private List<UserRoleOption> roleOptions = new ArrayList<>();
		private List<UserCompanyOption> userCompanyOptions = new ArrayList<>();

		/**
		 * @param user
		 * @param statusOptions
		 * @param companyOptions
		 * @param companyRoleOptions
		 * @param roleOptions
		 * @param userCompanyOptions
		 */
		public UserUpsert(UserModel user, List<UserStatusOption> statusOptions, List<CompanyOption> companyOptions,
		        List<CompanyOption> companyRoleOptions, List<UserRoleOption> roleOptions,
		        List<UserCompanyOption> userCompanyOptions) {
			this.user = user;
			this.statusOptions = statusOptions;
			this.companyOptions = companyOptions;
			this.companyRoleOptions = companyRoleOptions;
			this.roleOptions = roleOptions;
			this.userCompanyOptions = userCompanyOptions;
		}

		public UserModel getUser() {
			return user;
		}

		public List<UserStatusOption> getStatusOptions() {
			return statusOptions;
		}

		public List<CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public List<CompanyOption> getCompanyRoleOptions() {
			return companyRoleOptions;
		}

		public List<UserRoleOption> getRoleOptions() {
			return roleOptions;
		}

		public List<UserCompanyOption> getUserCompanyOptions() {
			return userCompanyOptions;
		}
	}

	public static class UserProfileModel extends ModelAbstract<UserProfileModel> {
		private static final long serialVersionUID = -7751131920798616180L;

		private String firstName;
		private String middleName;
		private String lastName;
		private String companyId;
		private String parentUserId;
		private String office;
		private String extension;
		private String fax;
		private String cell;
		private String home;
		private String email;
		private String address1;
		private String address2;
		private String city;
		private String state;
		private String postalCode;
		private String country;

		@Override
		protected UserProfileModel self() {
			return this;
		}

		public UserProfileModel withFirstName(String firstName) {
			setFirstName(firstName);
			return self();
		}

		public UserProfileModel withMiddleName(String middleName) {
			setMiddleName(middleName);
			return self();
		}

		public UserProfileModel withLastName(String lastName) {
			setLastName(lastName);
			return self();
		}

		public UserProfileModel withCompanyId(String companyId) {
			setCompanyId(companyId);
			return self();
		}

		public UserProfileModel withOffice(String office) {
			setOffice(office);
			return self();
		}

		public UserProfileModel withExtension(String extension) {
			setExtension(extension);
			return self();
		}

		public UserProfileModel withFax(String fax) {
			setFax(fax);
			return self();
		}

		public UserProfileModel withCell(String cell) {
			setCell(cell);
			return self();
		}

		public UserProfileModel withHome(String home) {
			setHome(home);
			return self();
		}

		public UserProfileModel withEmail(String email) {
			setEmail(email);
			return self();
		}

		public UserProfileModel withAddress1(String address1) {
			setAddress1(address1);
			return self();
		}

		public UserProfileModel withAddress2(String address2) {
			setAddress2(address2);
			return self();
		}

		public UserProfileModel withCity(String city) {
			setCity(city);
			return self();
		}

		public UserProfileModel withState(String state) {
			setState(state);
			return self();
		}

		public UserProfileModel withPostCode(String postalCode) {
			setPostalCode(postalCode);
			return self();
		}

		public UserProfileModel withCountry(String country) {
			setCountry(country);
			return self();
		}

		public UserProfileModel withParentUserId(String parenyUserId) {
			setParentUserId(parenyUserId);
			return self();
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getMiddleName() {
			return middleName;
		}

		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getOffice() {
			return office;
		}

		public void setOffice(String office) {
			this.office = office;
		}

		public String getExtension() {
			return extension;
		}

		public void setExtension(String extension) {
			this.extension = extension;
		}

		public String getFax() {
			return fax;
		}

		public void setFax(String fax) {
			this.fax = fax;
		}

		public String getCell() {
			return cell;
		}

		public void setCell(String cell) {
			this.cell = cell;
		}

		public String getHome() {
			return home;
		}

		public void setHome(String home) {
			this.home = home;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getAddress1() {
			return address1;
		}

		public void setAddress1(String address1) {
			this.address1 = address1;
		}

		public String getAddress2() {
			return address2;
		}

		public void setAddress2(String address2) {
			this.address2 = address2;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getParentUserId() {
			return parentUserId;
		}

		public void setParentUserId(String parentUserId) {
			this.parentUserId = parentUserId;
		}
	}

	public static class UserAccountModel extends ModelAbstract<UserAccountModel> {
		private static final long serialVersionUID = 1966034505328385854L;

		private String login;
		private String companyId;
		private boolean admin;
		private UserStatus status;
		private List<String> roleIds;

		@Override
		protected UserAccountModel self() {
			return this;
		}

		public UserAccountModel withLogin(String login) {
			setLogin(login);
			return self();
		}

		public UserAccountModel withCompanyId(String companyId) {
			setCompanyId(companyId);
			return self();
		}

		public UserAccountModel withAdmin(boolean admin) {
			setAdmin(admin);
			return self();
		}

		public UserAccountModel withStatus(UserStatus status) {
			setStatus(status);
			return self();
		}

		public UserAccountModel withRoleIds(List<String> roles) {
			setRoleIds(roles);
			return self();
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public boolean isAdmin() {
			return admin;
		}

		public void setAdmin(boolean admin) {
			this.admin = admin;
		}

		public UserStatus getStatus() {
			return status;
		}

		public void setStatus(UserStatus status) {
			this.status = status;
		}

		public List<String> getRoleIds() {
			return roleIds;
		}

		public void setRoleIds(List<String> roleIds) {
			this.roleIds = roleIds;
		}
	}

	public static class UserAuthenticationModel implements Serializable {

		private static final long serialVersionUID = -262425528776963659L;

		private String authId;
		private String authHid;
		private AuthType type;
		private String provider;
		private String principal;
		private boolean enabled;

		protected UserAuthenticationModel self() {
			return this;
		}

		public UserAuthenticationModel withAuthId(String authId) {
			setAuthId(authId);
			return self();
		}

		public UserAuthenticationModel withAuthHid(String authHid) {
			setAuthHid(authHid);
			return self();
		}

		public UserAuthenticationModel withType(AuthType type) {
			setType(type);
			return self();
		}

		public UserAuthenticationModel withProvider(String provider) {
			setProvider(provider);
			return self();
		}

		public UserAuthenticationModel withPrincipal(String principal) {
			setPrincipal(principal);
			return self();
		}

		public UserAuthenticationModel withEnabled(boolean enabled) {
			setEnabled(enabled);
			return self();
		}

		public String getAuthId() {
			return authId;
		}

		public void setAuthId(String authId) {
			this.authId = authId;
		}

		public String getAuthHid() {
			return authHid;
		}

		public void setAuthHid(String authHid) {
			this.authHid = authHid;
		}

		public AuthType getType() {
			return type;
		}

		public void setType(AuthType type) {
			this.type = type;
		}

		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		public String getPrincipal() {
			return principal;
		}

		public void setPrincipal(String principal) {
			this.principal = principal;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class UserAuthenticationUpsert implements Serializable {

		private static final long serialVersionUID = -2924437587626256205L;

		private UserModel user;
		private List<UserAuthenticationModel> authentications;
		private List<CompanyModels.CompanyAuthOption> authOptions;

		public UserAuthenticationUpsert(List<UserAuthenticationModel> authentications,
		        List<CompanyModels.CompanyAuthOption> authOptions, User user, String decryptedLogin) {

			this.user = new UserModel(user, decryptedLogin);
			this.authentications = authentications;
			this.authOptions = authOptions;
		}

		public List<UserAuthenticationModel> getAuthentications() {
			return authentications;
		}

		public UserModel getUser() {
			return user;
		}

		public List<CompanyModels.CompanyAuthOption> getAuthOptions() {
			return authOptions;
		}
	}

	public static class PasswordChangeModel extends ModelAbstract<PasswordChangeModel> {

		private static final long serialVersionUID = 6203919288545358483L;

		private String currentPassword;
		private String newPassword;

		public String getCurrentPassword() {
			return currentPassword;
		}

		public void setCurrentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		@Override
		protected PasswordChangeModel self() {
			return this;
		}
	}
}
