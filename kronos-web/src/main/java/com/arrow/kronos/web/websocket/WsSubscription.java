package com.arrow.kronos.web.websocket;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WsSubscription {

	private Set<String> subscribed = new HashSet<>(); 

	@PostConstruct
	public synchronized void init() {
		subscribed.clear();
	}

	@PreDestroy
	public synchronized void destroy() {
		subscribed.clear();
	}

	public synchronized boolean isSubscribed(String name) {
		return name != null && subscribed.contains(name);
	}

	public synchronized void subscribe(String name) {
		if (name != null) {
			this.subscribed.add(name);
		}
	}

	public synchronized void unsubscribe(String name) {
		if (name != null) {
			this.subscribed.remove(name);
		}
	}

	@Override
	public String toString() {
		return "WsSubscription [isSubscribed=" + subscribed + "]";
	}
}
