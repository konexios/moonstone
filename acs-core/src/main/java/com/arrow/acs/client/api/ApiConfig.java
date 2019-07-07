/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acs.client.api;

import java.io.Serializable;

public class ApiConfig implements Serializable {
	private static final long serialVersionUID = 3202503635018331127L;

	private static final int DEFAULT_NUM_RETRIES = 3;
	private static final int DEFAULT_RETRY_INTERVAL_SECS = 5;

	private String baseUrl;
	private String baseWebSocketUrl;
	private String apiKey;
	private String secretKey;

	private int numRetries = DEFAULT_NUM_RETRIES;
	private int retryIntervalSecs = DEFAULT_RETRY_INTERVAL_SECS;

	public ApiConfig withNumRetries(int numRetries) {
		setNumRetries(numRetries);
		return this;
	}

	public ApiConfig withRetryIntervalSecs(int retryIntervalSecs) {
		setRetryIntervalSecs(retryIntervalSecs);
		return this;
	}

	public ApiConfig withBaseUrl(String baseUrl) {
		setBaseUrl(baseUrl);
		return this;
	}

	public ApiConfig withBaseWebSocketUrl(String baseWebSocketUrl) {
		setBaseWebSocketUrl(baseWebSocketUrl);
		return this;
	}

	public ApiConfig withApiKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}

	public ApiConfig withSecretkey(String secretKey) {
		setSecretKey(secretKey);
		return this;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseWebSocketUrl() {
		return baseWebSocketUrl;
	}

	public void setBaseWebSocketUrl(String baseWebSocketUrl) {
		this.baseWebSocketUrl = baseWebSocketUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setNumRetries(int numRetries) {
		this.numRetries = numRetries;
	}

	public int getNumRetries() {
		return numRetries;
	}

	public void setRetryIntervalSecs(int retryIntervalSecs) {
		this.retryIntervalSecs = retryIntervalSecs;
	}

	public int getRetryIntervalSecs() {
		return retryIntervalSecs;
	}
}
