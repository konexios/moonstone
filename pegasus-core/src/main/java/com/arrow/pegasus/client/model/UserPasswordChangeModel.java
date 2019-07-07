package com.arrow.pegasus.client.model;

public class UserPasswordChangeModel extends UserChangeModel {

	private static final long serialVersionUID = -2122855788097358626L;

	private String newPassword;

	public UserPasswordChangeModel withNewPassword(String newPassword) {
		setNewPassword(newPassword);
		return this;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
