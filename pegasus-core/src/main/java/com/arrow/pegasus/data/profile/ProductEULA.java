package com.arrow.pegasus.data.profile;

import java.io.Serializable;
import java.time.Instant;

public class ProductEULA implements Serializable {
	private static final long serialVersionUID = 6440174590851255079L;

	private String url;
	private Instant date;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
}
