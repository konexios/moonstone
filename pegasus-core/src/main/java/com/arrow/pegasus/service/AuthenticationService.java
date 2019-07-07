package com.arrow.pegasus.service;

import com.arrow.pegasus.data.profile.User;

public interface AuthenticationService {

	public User authenticate(String username, String password);

	public User findByLogin(String username);

	public User samlAuthenticate(String idp, String principal);
}
