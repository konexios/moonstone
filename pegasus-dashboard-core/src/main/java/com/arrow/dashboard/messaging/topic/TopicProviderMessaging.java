package com.arrow.dashboard.messaging.topic;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.web.UrlUtils;

public class TopicProviderMessaging {

	private TopicProviderMessenger messenger;
	private String widgetRuntimeId;

	public TopicProviderMessaging(String widgetRuntimeId, TopicProviderMessenger messenger) {
		super();
		this.widgetRuntimeId = widgetRuntimeId;
		this.messenger = messenger;
	}

	public void send(String boardRuntimeId, String topic, Object object) {
		String topicURL = UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
		        DashboardConstants.Topic.WIDGET_OUTBOUND_MESSAGE, boardRuntimeId, widgetRuntimeId, topic);
		messenger.send(topicURL, object);
	}
}
