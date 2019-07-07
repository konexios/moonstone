package com.arrow.apollo;

import com.arrow.acs.AcsSystemException;

public class ApolloSystemException extends AcsSystemException {
	private static final long serialVersionUID = 6315971105253807704L;

	public ApolloSystemException(String message) {
		super(message);
	}

	public ApolloSystemException(String message, Throwable cause) {
		super(message, cause);
	}
}
