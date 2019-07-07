package com.arrow.pegasus.data.security;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class AuthSaml implements Serializable {
	private static final long serialVersionUID = -2371790291654382552L;

	@NotBlank
	private String idp;
	private String firstNameAttr;
	private String lastNameAttr;
	private String emailAttr;

	public String getIdp() {
		return idp;
	}

	public void setIdp(String idp) {
		this.idp = idp;
	}

	public String getFirstNameAttr() {
		return firstNameAttr;
	}

	public void setFirstNameAttr(String firstNameAttr) {
		this.firstNameAttr = firstNameAttr;
	}

	public String getLastNameAttr() {
		return lastNameAttr;
	}

	public void setLastNameAttr(String lastNameAttr) {
		this.lastNameAttr = lastNameAttr;
	}

	public String getEmailAttr() {
		return emailAttr;
	}

	public void setEmailAttr(String emailAttr) {
		this.emailAttr = emailAttr;
	}
}
