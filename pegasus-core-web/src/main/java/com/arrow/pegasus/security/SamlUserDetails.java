package com.arrow.pegasus.security;

import org.springframework.security.saml.SAMLCredential;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Auth;

public class SamlUserDetails extends CoreUserDetails {
	private static final long serialVersionUID = -597612114960327530L;

	private final SAMLCredential samlCredential;
	private final Auth auth;

	public SamlUserDetails(User user, SAMLCredential samlCredential, Auth auth) {
		super(user);
		this.samlCredential = samlCredential;
		this.auth = auth;
	}

	public SAMLCredential getSamlCredential() {
		return samlCredential;
	}

	public Auth getAuth() {
		return auth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((samlCredential == null) ? 0 : samlCredential.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SamlUserDetails other = (SamlUserDetails) obj;
		if (samlCredential == null) {
			if (other.samlCredential != null)
				return false;
		} else if (!samlCredential.getNameID().getValue().equals(other.samlCredential.getNameID().getValue()))
			return false;
		return true;
	}
}
