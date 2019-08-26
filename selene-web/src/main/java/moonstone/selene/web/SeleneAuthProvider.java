package moonstone.selene.web;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import moonstone.selene.Loggable;
import moonstone.selene.web.service.GatewayService;

@Component
public class SeleneAuthProvider extends Loggable implements AuthenticationProvider {

	@Autowired
	private final GatewayService gatewayService;

	public SeleneAuthProvider() {
		logInfo(getClass().getSimpleName(), "...");
		gatewayService = GatewayService.getInstance();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String method = "authenticate";
		String login = String.valueOf(authentication.getPrincipal());
		String password = String.valueOf(authentication.getCredentials());
		logInfo(method, "login: %s", login);
		logInfo(method, "password: %s", password);
		if (gatewayService.webAuthenticate(login, password)) {
			return new UsernamePasswordAuthenticationToken(login, password, Collections.emptySet());
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
