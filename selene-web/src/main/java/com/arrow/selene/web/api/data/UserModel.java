package com.arrow.selene.web.api.data;

import java.io.Serializable;

public class UserModel implements Serializable {
	private static final long serialVersionUID = -2323540606048145213L;

	private String username;
	
	public UserModel(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}
