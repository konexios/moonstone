package com.arrow.pegasus.data.security;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

@Document(collection = "auth")
public class Auth extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 569222720507817051L;

	private final static boolean DEFAULT_ENABLED = true;

	@NotBlank
	private String companyId;
	private AuthType type;
	private AuthLdap ldap;
	private AuthSaml saml;
	private boolean enabled = DEFAULT_ENABLED;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public AuthType getType() {
		return type;
	}

	public void setType(AuthType type) {
		this.type = type;
	}

	public AuthLdap getLdap() {
		return ldap;
	}

	public void setLdap(AuthLdap ldap) {
		this.ldap = ldap;
	}

	public AuthSaml getSaml() {
		return saml;
	}

	public void setSaml(AuthSaml saml) {
		this.saml = saml;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.AUTH;
	}
}
