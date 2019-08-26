package com.arrow.dashboard.web.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.util.Assert;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.runtime.model.UserRuntimeInstance;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.LoginRequiredException;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.security.CoreUserDetails;

import moonstone.acs.Loggable;

public abstract class WebSocketController extends Loggable {
	@Autowired
	protected RedisOperationsSessionRepository sessionRepository;

	protected String getUserId(SimpMessageHeaderAccessor headerAccessor) {
		return getAuthenticatedUser(headerAccessor).getId();
	}

	protected User getAuthenticatedUser(SimpMessageHeaderAccessor headerAccessor) {
		String method = "getAuthenticatedUser";
		try {
			Authentication auth = (Authentication) headerAccessor.getUser();
			User user = ((CoreUserDetails) auth.getPrincipal()).getUser();
			return user;
		} catch (Exception e) {
			logError(method, e);
			throw new LoginRequiredException();
		}
	}

	protected boolean hasAuthority(String authority, SimpMessageHeaderAccessor headerAccessor) {
		String method = "hasAuthority";
		boolean result = false;
		try {
			Authentication auth = (Authentication) headerAccessor.getUser();
			CoreUserDetails principal = (CoreUserDetails) auth.getPrincipal();
			for (GrantedAuthority a : principal.getAuthorities()) {
				if (a.getAuthority().equals(authority)) {
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			logError(method, e);
			throw new LoginRequiredException();
		}
		return result;
	}

	protected String getApplicationId(SimpMessageHeaderAccessor headerAccessor) {
		String applicationId = (String) headerAccessor.getSessionAttributes().get(CoreConstant.CURRENT_APPLICATION_ID);
		Assert.hasText(applicationId, "applicationId is empty");

		return applicationId;
	}

	protected String getWebSocketSessionId(AbstractSubProtocolEvent event) {
		Message<byte[]> message = event.getMessage();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		return accessor.getSessionId();
	}

	protected String getSessionId(SimpMessageHeaderAccessor headerAccessor) {
		Assert.notNull(headerAccessor, "headerAccessor is null");

		String method = "getSessionId";
		logInfo(method, "...");

		String sessionId = (String) headerAccessor.getSessionAttributes()
		        .get(DashboardConstants.SessionAttribute.HTTP_SESSION_ID_ATTRIBUTE);
		Assert.hasText(sessionId, "sessionId is empty");
		logInfo(method, "sessionId: %s", sessionId);

		return sessionId;
	}

	protected Session getSession(SimpMessageHeaderAccessor headerAccessor) {
		Assert.notNull(headerAccessor, "headerAccessor is null");

		String method = "getSession";
		logInfo(method, "...");

		String sessionId = getSessionId(headerAccessor);
		Assert.hasText(sessionId, "sessionId is empty");

		Session session = sessionRepository.findById(sessionId);
		Assert.notNull(session, "Session not found! sessionId=" + sessionId);

		return session;
	}

	protected UserRuntimeInstance getUserRuntimeInstance(SimpMessageHeaderAccessor headerAccessor) {
		User authenticatedUser = getAuthenticatedUser(headerAccessor);
		UserRuntimeInstance userRuntimeInstance = new UserRuntimeInstance();
		userRuntimeInstance.setLogin(authenticatedUser.getLogin());
		userRuntimeInstance.setUserId(authenticatedUser.getId());
		userRuntimeInstance.setCompanyId(authenticatedUser.getCompanyId());
		userRuntimeInstance.setApplicationId(getApplicationId(headerAccessor));
		Authentication auth = (Authentication) headerAccessor.getUser();
		CoreUserDetails principal = (CoreUserDetails) auth.getPrincipal();
		userRuntimeInstance.setUserAuthorities(
		        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

		return userRuntimeInstance;
	}
}