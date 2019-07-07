package com.arrow.dashboard.runtime;

import org.springframework.util.Assert;

import com.arrow.dashboard.messaging.Router;
import com.arrow.dashboard.messaging.topic.TopicProviderMessenger;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.ConfigurationManager;
import com.arrow.dashboard.widget.configuration.Page;

/**
 * Special service to provide and manage {@link Router} routers<br>
 * 
 * @author dantonov
 *
 */
public class RouterRuntimeManager extends RuntimeManagerAbstract<Router> {

	private TopicProviderMessenger messenger;

	public void setTopicProviderMessenger(TopicProviderMessenger messenger) {
		this.messenger = messenger;
	}

	public void registerRouter(WidgetRuntimeInstance widgetRuntimeInstance, ConfigurationManager configurationManager) {
		Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");
		Assert.notNull(configurationManager, "configurationManager is null");

		String method = "registerNewRoute";
		logInfo(method, "...");
		logDebug(method,
		        "Routing service start create a new route for widget " + widgetRuntimeInstance.getWidgetRuntimeId());

		registerRuntimeInstance(widgetRuntimeInstance.getWidgetRuntimeId(),
		        new Router(widgetRuntimeInstance, configurationManager, messenger));
	}

	/**
	 * Method to delete existing route for widget instance
	 * 
	 * @param widgetRuntimeId
	 */
	public void unregisterRouter(String widgetRuntimeId) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

		String method = "unregisterRoute";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeId);

		Router router = unregisterRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetId: %s", widgetRuntimeId);
			return;
		}

		router.close();
		router = null;
	}

	/**
	 * Method to route incoming message for widget
	 * 
	 * @param widgetRuntimeId
	 * @param endpoint
	 * @param message
	 */
	public void route(String widgetRuntimeId, String endpoint, String message) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
		Assert.hasText(endpoint, "endpoint is empty");
		// MESSAGE MAY BE EMPTY! this is not a requirement Assert.hasText(message, "message is empty");

		String method = "route";
		logInfo(method, "...");
		logDebug(method, "widgetRuntimeId: %s, endpoint: %s, message: %s", widgetRuntimeId, endpoint, message);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetRuntimeId=" + widgetRuntimeId);
			return;
		}

		router.routeMessage(endpoint, message);
	}

	/**
	 * Method to route configuration request for the widget
	 * 
	 * @param widgetRuntimeId
	 */
	public void routeConfigurationRequest(String widgetRuntimeId) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

		String method = "routeConfigurationRequest";
		logInfo(method, "...");
		logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetRuntimeId=" + widgetRuntimeId);
			return;
		}

		router.routeConfigurationRequest();
	}

	/**
	 * Method to route configuration page request for the widget
	 * 
	 * @param widgetRuntimeId
	 * @param configuration
	 */
	public void routeConfigurationPageRequest(String widgetRuntimeId, Configuration configuration) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
		Assert.notNull(configuration, "configuration is null");

		String method = "routeConfigurationPageRequest";
		logInfo(method, "...");
		logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetRuntimeId=" + widgetRuntimeId);
			return;
		}

		router.routeConfigurationPageRequest(configuration);
	}

	public void routeConfigurationSaveRequest(String widgetRuntimeId, Configuration configuration, String who) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
		Assert.notNull(configuration, "configuration is null");

		String method = "routeConfigurationSaveRequest";
		logInfo(method, "...");
		logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetRuntimeId=" + widgetRuntimeId);
			return;
		}

		router.routeConfigurationSaveRequest(configuration, who);

		logDebug(method, "Routing service has processed configuration page request for " + widgetRuntimeId);

	}

	/**
	 * Method to route fast configuration request for the widget
	 * 
	 * @param widgetInstanceId
	 */
	public void routeFastConfigurationRequest(String widgetRuntimeId) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

		String method = "routeFastConfigurationRequest";
		logInfo(method, "...");
		logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetRuntimeId=" + widgetRuntimeId);
			return;
		}

		router.routeFastConfigurationRequest();
	}

	/**
	 * Method to route fast configuration page request for the widget
	 * 
	 * @param widgetRuntimeId
	 * @param page
	 */
	public void routeFastConfigurationPageRequest(String widgetRuntimeId, Page page) {
		Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
		Assert.notNull(page, "page is null");

		String method = "routeFastConfigurationPageRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeId);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetRuntimeId=" + widgetRuntimeId);
			return;
		}

		router.routeFastConfigurationPageRequest(page);
	}

	/**
	 * Method to route deletion request for the widget
	 * 
	 * @param widgetRuntimeId
	 */
	public void routeDeteleWidget(String widgetRuntimeId) {
		Assert.hasText(widgetRuntimeId, "widgitRuntimeId is empty");

		String method = "routeDeteleWidget";
		logInfo(method, "widgetId: %s", widgetRuntimeId);

		Router router = getRuntimeInstance(widgetRuntimeId);
		if (router == null) {
			logError(method, "Router not found! widgetId: %s", widgetRuntimeId);
			return;
		}

		router.routeDeleteRequest();
	}

}
