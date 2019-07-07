package com.arrow.dashboard.exception;

/**
 * Exception for messages processing
 * 
 * @author dantonov
 *
 */
public class MessagingException extends Exception {
	public MessagingException() {
		super();
	}

	public MessagingException(String arg0) {
		super(arg0);
	}

	public MessagingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MessagingException(Throwable arg0) {
		super(arg0);
	}
}
