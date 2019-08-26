package com.arrow.pegasus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.AuthenticationService;
import com.arrow.pegasus.service.CoreCacheHelper;

import moonstone.acs.Loggable;

public class CoreUserDetailsService extends Loggable implements UserDetailsService {

	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private Crypto crypto;
	@Autowired
	private CoreCacheHelper coreCacheHelper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String method = "loadUserByUsername";
		logInfo(method, "username: %s", username);
		User user = authenticationService.findByLogin(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found for login: " + username);
		}
		// decrypt user's login
		user.setLogin(crypto.internalDecrypt(user.getLogin()));
		return new CoreUserDetails(coreCacheHelper.populateUser(user));
	}
}
