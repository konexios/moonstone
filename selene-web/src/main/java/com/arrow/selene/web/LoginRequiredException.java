package com.arrow.selene.web;

public class LoginRequiredException extends RuntimeException {
	private static final long serialVersionUID = -3067709756915495158L;

	public LoginRequiredException() {
		super("Login required!");
	}
}
