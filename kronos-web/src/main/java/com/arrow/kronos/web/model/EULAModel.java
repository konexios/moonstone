package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;

public class EULAModel implements Serializable {
	private static final long serialVersionUID = -7624938202790227568L;

	private String url;
	private Instant date;
	private boolean needsToAgree;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public EULAModel withUrl(String url) {
		setUrl(url);

		return this;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public EULAModel withDate(Instant date) {
		setDate(date);

		return this;
	}

	public boolean isNeedsToAgree() {
		return needsToAgree;
	}

	public void setNeedsToAgree(boolean needsToAgree) {
		this.needsToAgree = needsToAgree;
	}

	public EULAModel withNeedsToAgree(boolean needsToAgree) {
		setNeedsToAgree(needsToAgree);

		return this;
	}
}
