package com.arrow.pegasus.client.model;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.User;

public class UserChangeModel implements Serializable {

	private static final long serialVersionUID = -2783586668060449022L;

	private User user;
	private String who;

	public UserChangeModel withUser(User user) {
		setUser(user);
		return this;
	}

	public UserChangeModel withWho(String who) {
		setWho(who);
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}
}
