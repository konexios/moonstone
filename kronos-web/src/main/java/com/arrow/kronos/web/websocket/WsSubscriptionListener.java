package com.arrow.kronos.web.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.arrow.acs.Loggable;

@Component
public class WsSubscriptionListener extends Loggable {

	private WsSubscription subscription;

	@Autowired
	public WsSubscriptionListener(WsSubscription subscription) {
		this.subscription = subscription;
	}

	public boolean isSubscribed(String name) {
		return subscription.isSubscribed(name);
	}

	@EventListener
	public void subscribeEvent(SessionSubscribeEvent event) {
		String method = "subscribeEvent";

		String subscriptionId = getSubscriptionId(event);
		logDebug(method, "subscriptionId: %s", subscriptionId);

		subscription.subscribe(subscriptionId);
		logDebug(method, "%s %s", subscription, this);
	}

	@EventListener
	public void unsubscribeEvent(SessionUnsubscribeEvent event) {
		String method = "unsubscribeEvent";

		String subscriptionId = getSubscriptionId(event);
		logDebug(method, "subscriptionId: %s", subscriptionId);

		subscription.unsubscribe(subscriptionId);
		logDebug(method, "%s %s", subscription, this);
	}

	private String getSubscriptionId(AbstractSubProtocolEvent event) {
		Message<byte[]> message = event.getMessage();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		return accessor.getSubscriptionId();
	}
}
