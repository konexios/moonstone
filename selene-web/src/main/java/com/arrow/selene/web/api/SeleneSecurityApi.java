package com.arrow.selene.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.selene.Loggable;
import com.arrow.selene.model.StatusModel;
import com.arrow.selene.web.LoginRequiredException;
import com.arrow.selene.web.api.data.UserModel;

@RestController
@RequestMapping("/api/selene/security")
public class SeleneSecurityApi extends Loggable {

	protected String getAuthenticatedUser() {

		String methodName = "getAuthenticatedUser";
		logDebug(methodName, "....");

		Object principal = null;

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null) {
				throw new LoginRequiredException();
			}
			principal = auth.getPrincipal();

			logDebug(methodName, "Principal: %s", principal);

			return (String) principal;
		} catch (LoginRequiredException e) {
			throw e;
		} catch (Throwable e) {
			throw new LoginRequiredException();
		}
	}

	@RequestMapping(value = "/login")
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public StatusModel login() {

		String methodName = "login";
		logDebug(methodName, "...");

		return new StatusModel().withStatus("ERROR")
		        .withMessage("Either your username and/or password was either misspelled or incorrect.");
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public UserModel user() {

		String methodName = "user";
		logDebug(methodName, "...");

		String authenticatedUser = getAuthenticatedUser();
		logDebug(methodName, "authenticatedUser: %s", authenticatedUser);

		return new UserModel(authenticatedUser);
	}
}
