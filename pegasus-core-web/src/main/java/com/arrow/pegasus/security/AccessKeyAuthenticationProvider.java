package com.arrow.pegasus.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AccessKey;

public class AccessKeyAuthenticationProvider extends Loggable implements AuthenticationProvider {

	public AccessKeyAuthenticationProvider() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String method = "authenticate";
		AccessKey accessKey = (AccessKey) RequestContextHolder.currentRequestAttributes()
		        .getAttribute(CoreConstant.API_KEY_CONTEXT, RequestAttributes.SCOPE_REQUEST);
		if (accessKey != null) {
			logInfo(method, "found accessKey: %s", accessKey.getId());
			return new UsernamePasswordAuthenticationToken(accessKey, accessKey, Collections.emptyList());
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
