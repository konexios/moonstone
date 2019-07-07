package com.arrow.dashboard.widget.configuration.messaging;

import com.arrow.dashboard.widget.configuration.ConfigurationManager;
import com.arrow.dashboard.widget.configuration.Page;

/**
 * Interface to send fast configuration page out by {@link ConfigurationManager}
 * 
 * @author dantonov
 *
 */
public interface FastConfigurationTopic {
	public void send(Page page);
}
