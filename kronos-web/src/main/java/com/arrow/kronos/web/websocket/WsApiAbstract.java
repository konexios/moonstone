package com.arrow.kronos.web.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import com.arrow.acs.Loggable;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.LoginRequiredException;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.security.CoreUserDetails;

public class WsApiAbstract extends Loggable {

	@Autowired
	private KronosCache kronosCache;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	private WsSubscriptionListener wsListener;

	protected boolean isSubscribed(String subscriptionId) {
		return wsListener.isSubscribed(subscriptionId);
	}

	protected WsSubscriptionListener getSubscriptionListener() {
		return wsListener;
	}

	protected KronosCache getKronosCache() {
		return kronosCache;
	}

	protected SimpMessageSendingOperations getMessagingTemplate() {
		return messagingTemplate;
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
}
