package com.arrow.pegasus.client.model;

import java.io.Serializable;

import com.arrow.pegasus.data.security.Role;

public class RoleChangeModel implements Serializable {

	private static final long serialVersionUID = -3166281639738165395L;

	private Role role;
	private String who;

	public RoleChangeModel withRole(Role role) {
		setRole(role);
		return this;
	}

	public RoleChangeModel withWho(String who) {
		setWho(who);
		return this;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}
}
