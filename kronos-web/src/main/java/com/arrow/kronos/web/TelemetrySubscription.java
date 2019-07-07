package com.arrow.kronos.web;

import java.util.HashMap;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TelemetrySubscription {

	private HashMap<String, TelemetrySubscriptionModel> subs = new HashMap<>();

	@PreDestroy
	public synchronized void destroy() {
		subs.clear();
	}

	public synchronized TelemetrySubscriptionModel subscribe(String subscriptionId, String gatewayHid, String deviceHid) {
		TelemetrySubscriptionModel sub = new TelemetrySubscriptionModel(gatewayHid, deviceHid);
		subs.put(subscriptionId, sub);
		return sub;
	}

	public synchronized TelemetrySubscriptionModel unsubscribe(String subscriptionId) {
		return subs.remove(subscriptionId);
	}
	
	public synchronized HashMap<String, TelemetrySubscriptionModel> unsubscribeAll() {
		HashMap<String, TelemetrySubscriptionModel> result = new HashMap<>(subs);
		subs.clear();
		return result;
	}

	public static class TelemetrySubscriptionModel {

		private String gatewayHid;
		private String deviceHid;

		public TelemetrySubscriptionModel(String gatewayHid, String deviceHid) {
			this.gatewayHid = gatewayHid;
			this.deviceHid = deviceHid;
		}

		public String getGatewayHid() {
			return gatewayHid;
		}

		public String getDeviceHid() {
			return deviceHid;
		}

		@Override
		public String toString() {
			return "TelemetrySubscriptionModel [gatewayHid=" + gatewayHid + ", deviceHid=" + deviceHid + "]";
		}
	}

}
