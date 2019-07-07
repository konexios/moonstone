package com.arrow.dashboard.widget.configuration.messaging;

import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardEntityAbstract;
import com.arrow.dashboard.messaging.topic.TopicProviderMessenger;

/**
 * Defines common data for topic providers used in configuration processing
 * 
 * @author dantonov
 *
 */
public abstract class AbstractTopicProvider extends DashboardEntityAbstract {
	protected String widgetId;
	protected String dashboardId;
	private TopicProviderMessenger messenger;

	public AbstractTopicProvider(String widgetId, TopicProviderMessenger messenger) {
		super();
		this.widgetId = widgetId;
		this.messenger = messenger;
	}

	/**
	 * Method to send an object to topic
	 * 
	 * @param topicURL
	 * @param object
	 * @throws ConfigurationMessageException
	 */
	public void send(String topicURL, Object object) throws ConfigurationMessageException {
		Assert.hasText(topicURL, "topicUrl is empty");
		Assert.notNull(object, "object is null");

		String method = "send";
		logInfo(method, "...");
		logDebug(method, "topic: " + topicURL);

		messenger.send(topicURL, object);
	}
}
