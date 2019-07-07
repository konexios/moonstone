package com.arrow.apollo.web.model;

import java.io.Serializable;
import java.util.List;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.web.model.ModelAbstract;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

/**
 * This is Model class gives AutheticationDetails.This model class having
 * serializable class containing property autheticationType and its getter
 * method
 */
public class LoginModels {

	public static class AutheticationDetails implements Serializable {
		private static final long serialVersionUID = 8773747263558740780L;

		private String autheticationType;

		public String getAutheticationType() {
			return autheticationType;
		}

		public void setAutheticationType(String autheticationType) {
			this.autheticationType = autheticationType;
		}

		public AutheticationDetails(String autheticationType) {
			this.autheticationType = autheticationType;
		}
	}

	public static class PasswordChangeModel extends ModelAbstract<PasswordChangeModel> {

		private static final long serialVersionUID = -3947460545166803896L;
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

	public static class UserModel extends CoreDocumentModel {
		private static final long serialVersionUID = -5636360694194674160L;

		private String firstName;
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
		private String companyId;
		private List<String> roleIds;

		public UserModel() {
			super(null, null);
		}

		public UserModel(User user) {
			super(user.getId(), user.getHid());
			this.firstName = user.getContact().getFirstName();
			this.lastName = user.getContact().getLastName();
			this.login = user.getLogin();
			this.status = user.getStatus();
			this.admin = user.isAdmin();
			this.office = user.getContact().getOffice();
			this.extension = user.getContact().getMonitorExt();
			this.fax = user.getContact().getFax();
			this.cell = user.getContact().getCell();
			this.home = user.getContact().getHome();
			this.email = user.getContact().getEmail();
			this.companyId = user.getCompanyId();
			this.roleIds = user.getRoleIds();
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

		public List<String> getRoleIds() {
			return roleIds;
		}
	}

}
