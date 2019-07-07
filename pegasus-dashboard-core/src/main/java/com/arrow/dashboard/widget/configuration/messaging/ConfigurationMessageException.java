package com.arrow.dashboard.widget.configuration.messaging;

/**
 * Exception for all errors in messaging during configuration processing
 * 
 * @author dantonov
 *
 */
public class ConfigurationMessageException extends RuntimeException {
	private static final long serialVersionUID = 7554215925089123957L;

	public ConfigurationMessageException(String message) {
		super(message);
	}

	public ConfigurationMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
