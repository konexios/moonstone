package com.arrow.dashboard.widget.configuration.messaging;

import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.messaging.topic.TopicProviderMessenger;
import com.arrow.dashboard.web.UrlUtils;
import com.arrow.dashboard.widget.configuration.Page;

/**
 * Topic provider to send {@link Page} to web configurator with 'fast
 * properties'
 * 
 * @author dantonov
 *
 */
public class FastConfigurationTopicProvider extends AbstractTopicProvider {

	public FastConfigurationTopicProvider(String widgetId, TopicProviderMessenger messenger) {
		super(widgetId, messenger);
	}

	public void send(Page page, String boardRuntimeId) {
		Assert.notNull(page, "page is null");
		Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

		String method = "send";
		logInfo(method, "...");

		String topicURL = UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
		        DashboardConstants.Topic.WIDGET_INBOUND_FAST_CONFIGURATION, boardRuntimeId, widgetId);
		logDebug(method, "topic: %s", topicURL);

		send(topicURL, page);
	}
}
