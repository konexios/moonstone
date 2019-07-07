package com.arrow.pegasus.client.model;

import java.io.Serializable;

public class SamlAuthenticationModel implements Serializable {

	private static final long serialVersionUID = -3545107136581654061L;

	private String idp;
	private String nameId;

	public SamlAuthenticationModel withIdp(String idp) {
		setIdp(idp);
		return this;
	}

	public SamlAuthenticationModel withNameId(String nameId) {
		setNameId(nameId);
		return this;
	}

	public String getIdp() {
		return idp;
	}

	public void setIdp(String idp) {
		this.idp = idp;
	}

	public String getNameId() {
		return nameId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}
}
