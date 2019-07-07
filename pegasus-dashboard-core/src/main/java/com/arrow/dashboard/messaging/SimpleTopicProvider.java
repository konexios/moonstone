package com.arrow.dashboard.messaging;

import com.arrow.dashboard.exception.MessagingException;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;

/**
 * Simple topic provider to be used as a {@link TopicProvider} field<br>
 * Messages are routed to topic, defined in annotation
 * 
 * @author dantonov
 *
 */
public interface SimpleTopicProvider {
	/**
	 * Method to send a message to front end widget implementation<br>
	 * Message will be send to topic, defined in {@link TopicProvider}
	 * annotation for this provider
	 * 
	 * @param object
	 *            to be send to front end
	 * @throws MessagingException
	 *             in case of messaging failure
	 */
	void send(Object object) throws MessagingException;
}