package com.arrow.selene.data;

public class Azure extends BaseEntity {
	private static final long serialVersionUID = -3341445375601081508L;

	private String host;
	private String accessKey;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
}
