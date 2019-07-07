package com.arrow.pegasus.util;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail.smtp")
public class SmtpProperties implements Serializable {
	private static final long serialVersionUID = 7555513042344421310L;

	private String auth;
	private String starttls;
	private String host;
	private String port;
	private String from;
	private String userName;
	private String password;

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getStarttls() {
		return starttls;
	}

	public void setStarttls(String starttls) {
		this.starttls = starttls;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
