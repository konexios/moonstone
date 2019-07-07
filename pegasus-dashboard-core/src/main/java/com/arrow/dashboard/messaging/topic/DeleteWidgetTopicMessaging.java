package com.arrow.dashboard.messaging.topic;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.web.UrlUtils;

public class DeleteWidgetTopicMessaging {

	private TopicProviderMessenger messanger;
	private String widgetRuntimeId;

	public DeleteWidgetTopicMessaging(String widgetRuntimeId, TopicProviderMessenger messanger) {
		super();
		this.widgetRuntimeId = widgetRuntimeId;
		this.messanger = messanger;
	}

	public void send(String boardRuntimeId) {
		String topicURL = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_REMOVE_WIDGET, boardRuntimeId);
		// String topicURL =
		// UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
		// DashboardConstants.Topics.DELETE_WIDGET, boardRuntimeId);
		messanger.send(topicURL, widgetRuntimeId);
	}
}
