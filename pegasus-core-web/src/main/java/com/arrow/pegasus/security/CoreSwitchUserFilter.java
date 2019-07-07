package com.arrow.pegasus.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.Loggable;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.CoreCacheService;

public class CoreSwitchUserFilter extends SwitchUserFilter {

	final static String DEFAULT_SWITCH_USER_URL = "/impersonate/login";
	final static String DEFAULT_EXIT_USER_URL = "/impersonate/logout";
	final static String DEFAULT_TARGET_URL = "/";
	final static String DEFAULT_SWITCH_FAILURE_URL = "/impersonate/failed";

	Loggable logger = new Loggable() {
	};

	@Autowired
	private CoreUserDetailsService userDetailsService;
	@Autowired
	private CoreCacheService coreCache;

	@Override
	public void afterPropertiesSet() {
		String method = "afterPropertiesSet";
		setUserDetailsService(userDetailsService);
		setSwitchUserUrl(DEFAULT_SWITCH_USER_URL);
		setExitUserUrl(DEFAULT_EXIT_USER_URL);
		setTargetUrl(DEFAULT_TARGET_URL);
		setSwitchFailureUrl(DEFAULT_SWITCH_FAILURE_URL);
		super.afterPropertiesSet();
		logger.logInfo(method, "...");
	}

	@Override
	protected Authentication attemptSwitchUser(HttpServletRequest request) throws AuthenticationException {
		String method = "attemptSwitchUser";
		String error = null;
		Authentication newAuth = null;
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		if (currentAuth != null) {
			User currentUser = ((CoreUserDetails) currentAuth.getPrincipal()).getUser();
			Assert.notNull(currentUser, "currentUser not found");
			Company currentCompany = coreCache.findCompanyById(currentUser.getCompanyId());
			Assert.notNull(currentCompany, "currentCompany not found");
			logger.logInfo(method, "currentUser - login: %s, company: %s", currentUser.getLogin(),
			        currentCompany.getName());
			newAuth = super.attemptSwitchUser(request);
			Object principal = newAuth.getPrincipal();
			if (principal instanceof CoreUserDetails) {
				User newUser = ((CoreUserDetails) principal).getUser();
				Assert.notNull(newUser, "newUser not found");
				Company newCompany = coreCache.findCompanyById(newUser.getCompanyId());
				Assert.notNull(newCompany, "newCompany not found");
				logger.logInfo(method, "newUser - login: %s, company: %s", newUser.getLogin(), newCompany.getName());

				boolean allow = false;
				if (!currentUser.isAdmin()) {
					allow = newCompany.getImpersonateUsers() != null
					        && newCompany.getImpersonateUsers().contains(currentUser.getId());
					if (allow) {
						if (newUser.isAdmin()) {
							error = "Super Admin User cannot be impersonated by a normal user!";
							allow = false;
						} else {
							allow = true;
						}
					} else {
						error = "Current User is not allowed to impersonate user of tenantId " + newCompany.getId();
					}

				} else {
					allow = true;
				}

				if (allow) {
					((CoreUserDetails) principal).setImpersonateUser(currentUser);
					logger.logInfo(method, "impersonate request accepted: %s --> %s", currentUser.getLogin(),
					        newUser.getLogin());
				}
			} else {
				error = "UNSUPPORTED principal object found, class: " + principal.getClass().getName();
			}
		} else {
			error = "Current UserContext not found!";
		}
		if (StringUtils.isNotEmpty(error)) {
			logger.logError(method, error);
			throw new AcsLogicalException(error);
		}
		return newAuth;
	}
}
