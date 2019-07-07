package com.arrow.dashboard.messaging.topic;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.web.UrlUtils;

/**
 * Messenger to send message to service
 * 
 * @author alitvin
 *
 */
public class TopicProviderForServiceMessaging {

	private TopicProviderMessenger messanger;

	public TopicProviderForServiceMessaging(TopicProviderMessenger messanger) {
		super();
		this.messanger = messanger;
	}

	public void send(String serviceId, Object object) {
		String url = UrlUtils.fromControllerUrlToServiceUrl(DashboardConstants.MESSAGE_BROKER,
				DashboardConstants.Topic.TOPIC_WIDGET_CONTROLLER_OUTGOING_NEW_DASHBOARD_MESSAGE, serviceId);
		messanger.send(url, object);
	}
}
