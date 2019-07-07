package com.arrow.pegasus.client.model;

import java.io.Serializable;

import com.arrow.pegasus.data.security.Privilege;

public class PrivilegeChangeModel implements Serializable {

	private static final long serialVersionUID = 1190839191054453037L;

	private Privilege privilege;
	private String who;

	public PrivilegeChangeModel withPrivilege(Privilege privilege) {
		setPrivilege(privilege);
		return this;
	}

	public PrivilegeChangeModel withWho(String who) {
		setWho(who);
		return this;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}
}
