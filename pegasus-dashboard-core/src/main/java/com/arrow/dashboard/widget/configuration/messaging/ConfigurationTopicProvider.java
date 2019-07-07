package com.arrow.dashboard.widget.configuration.messaging;

import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.messaging.topic.TopicProviderMessenger;
import com.arrow.dashboard.web.UrlUtils;
import com.arrow.dashboard.widget.configuration.Configuration;

/**
 * Topic provider to send {@link Configuration} to web configurator
 * 
 * @author dantonov
 *
 */
public class ConfigurationTopicProvider extends AbstractTopicProvider {

	public ConfigurationTopicProvider(String widgetId, TopicProviderMessenger messenger) {
		super(widgetId, messenger);
	}

	public void send(Configuration configuraion, String boardRuntimeId) {
		Assert.notNull(configuraion, "configuration is null");
		Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

		String method = "send";
		logInfo(method, "...");

		String topicURL = UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
		        DashboardConstants.Topic.WIDGET_INBOUND_CONFIGURATION, boardRuntimeId, widgetId);
		logDebug(method, "topic: %s", topicURL);

		send(topicURL, configuraion);
	}

}