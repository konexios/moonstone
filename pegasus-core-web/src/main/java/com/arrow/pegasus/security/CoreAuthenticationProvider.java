package com.arrow.pegasus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.AuthenticationService;
import com.arrow.pegasus.service.CoreCacheHelper;

public class CoreAuthenticationProvider extends Loggable implements AuthenticationProvider {

	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;
	@Autowired
	private Crypto crypto;

	public CoreAuthenticationProvider() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String method = "authenticate";
		String login = String.valueOf(authentication.getPrincipal());
		String password = String.valueOf(authentication.getCredentials());
		logInfo(method, "login: %s", login);

		User user = authenticationService.authenticate(login, password);
		if (user != null) {
			// decrypt user's login
			user.setLogin(crypto.internalDecrypt(user.getLogin()));
			CoreUserDetails userDetails = new CoreUserDetails(coreCacheHelper.populateUser(user));
			return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		} else {
			logInfo(method, "login failed");
			throw new BadCredentialsException("Bad Credentials");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
}
