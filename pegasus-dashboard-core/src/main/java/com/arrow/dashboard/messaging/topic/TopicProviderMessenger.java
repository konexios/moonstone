package com.arrow.dashboard.messaging.topic;

/**
 * Messenger defines real messaging
 * 
 * @author dantonov
 *
 */
public interface TopicProviderMessenger {

	void send(String topic, Object object);

}
