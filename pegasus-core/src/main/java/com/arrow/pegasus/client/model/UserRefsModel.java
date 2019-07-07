package com.arrow.pegasus.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Role;

public class UserRefsModel implements Serializable {

	private static final long serialVersionUID = 5004187210277623467L;

	private User user;
	private Company refCompany;
	private List<Role> refRoles = new ArrayList<>();

	public UserRefsModel withUser(User user) {
		setUser(user);
		return this;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public List<Role> getRefRoles() {
		return refRoles;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (user != null) {
			this.refCompany = user.getRefCompany();
			this.refRoles = user.getRefRoles();
		}
	}
}
