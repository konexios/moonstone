package com.arrow.widget.socialevent;

import java.io.Serializable;

public class SocialEventLastRegistered implements Serializable {
	private static final long serialVersionUID = 7966842236757185015L;

	private String timestamp;
	private String name;

	public SocialEventLastRegistered(Long timestamp, String name) {
		super();
		this.timestamp = String.valueOf(timestamp);
		this.name = name;
	}
	
	public SocialEventLastRegistered(String timestamp, String name) {
		super();
		this.timestamp = timestamp;
		this.name = name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SocialEventLastRegistered [timestamp=" + timestamp + ", name=" + name + "]";
	}

}
