package com.arrow.pegasus.data.profile;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.arrow.pegasus.data.security.AuthType;

public class UserAuth implements Serializable {
	private static final long serialVersionUID = -5183284635498689136L;

	private final static boolean DEFAULT_ENABLED = true;

	@NotBlank
	private AuthType type;
	@NotBlank
	private String refId;
	@NotBlank
	private String principal;
	private boolean enabled = DEFAULT_ENABLED;

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public AuthType getType() {
		return type;
	}

	public void setType(AuthType type) {
		this.type = type;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}