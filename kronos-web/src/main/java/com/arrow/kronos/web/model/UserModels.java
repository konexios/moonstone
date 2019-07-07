package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.List;

import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class UserModels {

	public static class VerifiedUser implements Serializable {
		private static final long serialVersionUID = -2832488089279426842L;

		private String login;
		private String tempPassword;

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public VerifiedUser withLogin(String login) {
			setLogin(login);

			return this;
		}

		public String getTempPassword() {
			return tempPassword;
		}

		public void setTempPassword(String tempPassword) {
			this.tempPassword = tempPassword;
		}

		public VerifiedUser withTempPassword(String tempPassword) {
			setTempPassword(tempPassword);

			return this;
		}
	}

	public static class UserOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 6286389158839274713L;

		public UserOption() {
			super(null, null, null);
		}

		public UserOption(User user) {
			super(user.getId(), user.getHid(), user.getContact().fullName());
		}
	}

	public static class PasswordChangeModel implements Serializable {
		private static final long serialVersionUID = 2809368477470960768L;

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

	}

	public static class PasswordChangeResult implements Serializable {
		private static final long serialVersionUID = 8750533669197960840L;

		private boolean ok;
		private List<String> messages;

		public PasswordChangeResult(List<String> messages) {
			this.ok = messages != null ? messages.size() == 0 : true;
			this.messages = messages;
		}

		public boolean isOk() {
			return ok;
		}

		public List<String> getMessages() {
			return messages;
		}

	}

	public static class PasswordPolicyModel extends PasswordPolicy {

		private static final long serialVersionUID = -5798244150540761841L;

		private String specialCharacters = PasswordPolicy.PEGASUS_SIMPLE_SPECIAL;

		public PasswordPolicyModel(PasswordPolicy passwordPolicy) {
			this.setAllowWhitespace(passwordPolicy.isAllowWhitespace());
			this.setHistorical(passwordPolicy.getHistorical());
			this.setMaxLength(passwordPolicy.getMaxLength());
			this.setMinDigit(passwordPolicy.getMinDigit());
			this.setMinLength(passwordPolicy.getMinLength());
			this.setMinLowerCase(passwordPolicy.getMinLowerCase());
			this.setMinSpecial(passwordPolicy.getMinSpecial());
			this.setMinUpperCase(passwordPolicy.getMinUpperCase());
		}

		public String getSpecialCharacters() {
			return specialCharacters;
		}

		public void setSpecialCharacters(String specialCharacters) {
			this.specialCharacters = specialCharacters;
		}
	}
}
