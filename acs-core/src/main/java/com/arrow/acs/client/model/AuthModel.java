/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acs.client.model;

public class AuthModel extends AuditableDocumentModelAbstract<AuthModel> {
	private static final long serialVersionUID = -3916291794363534624L;

	private AuthLdapModel ldap;
	private AuthSamlModel saml;
	private String companyHid;
	private AuthType type;
	private boolean enabled;

	@Override
	protected AuthModel self() {
		return this;
	}

	public AuthModel withLdap(AuthLdapModel ldap) {
		setLdap(ldap);
		return this;
	}

	public AuthModel withSaml(AuthSamlModel saml) {
		setSaml(saml);
		return this;
	}

	public AuthModel withCompanyHid(String companyHid) {
		setCompanyHid(companyHid);
		return this;
	}

	public AuthModel withType(AuthType type) {
		setType(type);
		return this;
	}

	public AuthModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getCompanyHid() {
		return companyHid;
	}

	public void setCompanyHid(String companyHid) {
		this.companyHid = companyHid;
	}

	public AuthLdapModel getLdap() {
		return ldap;
	}

	public void setLdap(AuthLdapModel ldap) {
		this.ldap = ldap;
	}

	public AuthSamlModel getSaml() {
		return saml;
	}

	public void setSaml(AuthSamlModel saml) {
		this.saml = saml;
	}

	public AuthType getType() {
		return type;
	}

	public void setType(AuthType type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
