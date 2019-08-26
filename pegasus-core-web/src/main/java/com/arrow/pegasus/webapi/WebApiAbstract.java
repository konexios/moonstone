package com.arrow.pegasus.webapi;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.EndpointAbstract;
import com.arrow.pegasus.LoginRequiredException;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.security.CoreUserDetails;
import com.arrow.pegasus.service.PlatformConfigService;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.AcsUtils;

public class WebApiAbstract extends EndpointAbstract {

	@Autowired
	private PlatformConfigService platformConfigService;

	protected String getUserId() {
		return getAuthenticatedUser().getId();
	}

	protected void validateApplicationId(HttpSession session, String applicationId) {
		if (!StringUtils.equals(getApplicationId(session), applicationId)) {
			throw new NotAuthorizedException();
		}
	}

	protected String getApplicationId(HttpSession session) {

		Assert.notNull(session, "session is null");

		Application application = getApplication(session);
		Assert.hasText(application.getId(), "applicationId is empty");

		return application.getId();
	}

	protected Application getApplication(HttpSession session) {

		Application application = getCurrentApplication(session);
		Assert.notNull(application, "application is null");

		return application;
	}

	protected User getAuthenticatedUser() {

		Object principal = null;

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null) {
				throw new LoginRequiredException();
			}
			principal = auth.getPrincipal();
			return ((CoreUserDetails) principal).getUser();
		} catch (LoginRequiredException e) {
			throw e;
		} catch (Throwable e) {
			throw new LoginRequiredException();
		}
	}

	protected Application setCurrentApplication(HttpSession session, String applicationId) {
		String method = "setCurrentApplication";
		if (session == null) {
			throw new LoginRequiredException();
		}
		if (StringUtils.isEmpty(applicationId)) {
			throw new AcsLogicalException("empty applicationId");
		}
		Application application = getCoreCacheService().findApplicationById(applicationId);
		if (application == null) {
			throw new AcsLogicalException("invalid applicationId: " + applicationId);
		}
		PlatformConfig platformConfig = platformConfigService.getConfig();
		if (platformConfig.isValidateZone()) {
			String zoneId = platformConfig.getRefZone().getId();
			if (AcsUtils.isNotEmpty(application.getZoneId()) && !application.getZoneId().equals(zoneId)) {
				throw new AcsLogicalException("application zone mismatched!");
			}
		} else {
			logWarn(method, "zone validation is turned off for this zone!");
		}
		session.setAttribute(CoreConstant.CURRENT_APPLICATION_ID, application.getId());

		return application;
	}

	protected Application getCurrentApplication(HttpSession session) {
		if (session == null) {
			throw new LoginRequiredException();
		}
		String applicationId = (String) session.getAttribute(CoreConstant.CURRENT_APPLICATION_ID);
		if (StringUtils.isEmpty(applicationId)) {
			synchronized (this) {
				Map<String, Application> apps = new HashMap<>();
				User user = getAuthenticatedUser();
				for (Role role : user.getRefRoles()) {
					if (!apps.containsKey(role.getApplicationId())) {
						apps.put(role.getApplicationId(), role.getRefApplication());
					}
				}
				if (apps.size() == 0) {
					throw new AcsLogicalException("access denied");
				}
				if (apps.size() > 1) {
					throw new AcsLogicalException("application has not been selected");
				}

				// auto select the application
				applicationId = apps.values().iterator().next().getId();
				session.setAttribute(CoreConstant.CURRENT_APPLICATION_ID, applicationId);
			}
		}

		return getCoreCacheService().findApplicationById(applicationId);
	}

	protected boolean hasAuthority(String authority) {
		boolean result = false;

		Object principal = null;

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null) {
				throw new LoginRequiredException();
			}
			principal = auth.getPrincipal();

			CoreUserDetails cud = (CoreUserDetails) principal;

			for (GrantedAuthority a : cud.getAuthorities()) {
				if (a.getAuthority().equals(authority)) {
					result = true;
					break;
				}
			}
		} catch (LoginRequiredException e) {
			throw e;
		} catch (Throwable e) {
			throw new LoginRequiredException();
		}

		return result;
	}
}
