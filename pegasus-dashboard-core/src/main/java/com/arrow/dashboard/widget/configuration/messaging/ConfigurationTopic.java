package com.arrow.dashboard.widget.configuration.messaging;

import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.ConfigurationManager;

/**
 * Interface to send configuration out by {@link ConfigurationManager}
 * 
 * @author dantonov
 *
 */
public interface ConfigurationTopic {
	public void send(Configuration configuraion);
}
