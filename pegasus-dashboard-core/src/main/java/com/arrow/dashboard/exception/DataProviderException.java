package com.arrow.dashboard.exception;

/**
 * Exception for data providers
 * 
 * @author dantonov
 *
 */
public class DataProviderException extends Exception {
	public DataProviderException() {
		super();
	}

	public DataProviderException(String arg0) {
		super(arg0);
	}

	public DataProviderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DataProviderException(Throwable arg0) {
		super(arg0);
	}
}
