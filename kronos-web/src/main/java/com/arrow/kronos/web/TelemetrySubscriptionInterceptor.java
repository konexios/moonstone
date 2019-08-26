package com.arrow.kronos.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.LoginRequiredException;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.security.CoreUserDetails;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.Loggable;

public class TelemetrySubscriptionInterceptor extends Loggable implements ChannelInterceptor {

	@Autowired
	private KronosCache kronosCache;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		String method = "preSend";

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();
		if (SimpMessageType.SUBSCRIBE.equals(command.getMessageType())) {
			logDebug(method, "---> " + command);
			logDebug(method, "%s", accessor);
			if (accessor.getDestination() != null) {
				Pattern pattern = Pattern.compile(
				        String.format(KronosWebConstants.DEVICE_TELEMETRY_DESTINATION_FORMAT, "(\\S+)", "(\\S+)"));
				Matcher matcher = pattern.matcher(accessor.getDestination());
				String deviceId = null;
				if (matcher.matches()) {
					deviceId = matcher.group(1);
					String telemetryName = matcher.group(2);
					logDebug(method, "%s %s", deviceId, telemetryName);
				} else {
					pattern = Pattern.compile(String.format(KronosWebConstants.DEVICE_DESTINATION_FORMAT, "(\\S+)"));
					matcher = pattern.matcher(accessor.getDestination());
					if (matcher.matches()) {
						deviceId = matcher.group(1);
						logDebug(method, "%s", deviceId);
					}
				}
				if (deviceId != null) {
					Device device = kronosCache.findDeviceById(deviceId);
					if (device == null) {
						throw new AcsLogicalException("device not found");
					}
					if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES", accessor)
					        && !getAuthenticatedUser(accessor).getId().equals(device.getUserId())) {
						throw new AcsLogicalException("user must own the device");
					}
					if (!getApplicationId(accessor).equals(device.getApplicationId())) {
						throw new AcsLogicalException("user and device must have the same application id");
					}
				}
			}
		}

		return message;
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

	protected String getApplicationId(SimpMessageHeaderAccessor headerAccessor) {
		String applicationId = (String) headerAccessor.getSessionAttributes().get(CoreConstant.CURRENT_APPLICATION_ID);
		Assert.hasText(applicationId, "applicationId is empty");

		return applicationId;
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
	}

	@Override
	public boolean preReceive(MessageChannel channel) {
		return true;
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		return message;
	}

	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
	}

}
