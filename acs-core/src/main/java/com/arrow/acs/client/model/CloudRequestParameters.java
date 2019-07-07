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
package com.arrow.acs.client.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloudRequestParameters implements Serializable {
	private static final long serialVersionUID = -2250244763023086384L;

	public static final String URI_PARAMETER_NAME = "uri";
	public static final String METHOD_PARAMETER_NAME = "method";
	public static final String API_KEY_PARAMETER_NAME = "apiKey";
	public static final String BODY_PARAMETER_NAME = "body";
	public static final String API_REQUEST_SIGNATURE_PARAMETER_NAME = "apiRequestSignature";
	public static final String API_REQUEST_SIGNATURE_VERSION_PARAMETER_NAME = "apiRequestSignatureVersion";
	public static final String TIMESTAMP_PARAMETER_NAME = "timestamp";

	private String uri;
	private CloudRequestMethodName method;
	private String apiKey;
	private String body;
	private String apiRequestSignature;
	private String apiRequestSignatureVersion;
	private String timestamp;

	public CloudRequestParameters withUri(String uri) {
		setUri(uri);
		return this;
	}

	public CloudRequestParameters withMethod(CloudRequestMethodName method) {
		setMethod(method);
		return this;
	}

	public CloudRequestParameters withApiKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}

	public CloudRequestParameters withBody(String body) {
		setBody(body);
		return this;
	}

	public CloudRequestParameters withApiRequestSignature(String apiRequestSignature) {
		setApiRequestSignature(apiRequestSignature);
		return this;
	}

	public CloudRequestParameters withApiRequestSignatureVersion(String apiRequestSignatureVersion) {
		setApiRequestSignatureVersion(apiRequestSignatureVersion);
		return this;
	}

	public CloudRequestParameters withTimestamp(String timestamp) {
		setTimestamp(timestamp);
		return this;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public CloudRequestMethodName getMethod() {
		return method;
	}

	public void setMethod(CloudRequestMethodName method) {
		this.method = method;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getApiRequestSignature() {
		return apiRequestSignature;
	}

	public void setApiRequestSignature(String apiRequestSignature) {
		this.apiRequestSignature = apiRequestSignature;
	}

	public String getApiRequestSignatureVersion() {
		return apiRequestSignatureVersion;
	}

	public void setApiRequestSignatureVersion(String apiRequestSignatureVersion) {
		this.apiRequestSignatureVersion = apiRequestSignatureVersion;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, String> getParameters() {
		Map<String, String> parameters = new LinkedHashMap<>();

		if (getUri() != null) {
			parameters.put(URI_PARAMETER_NAME, getUri());
		}
		if (getMethod() != null) {
			parameters.put(METHOD_PARAMETER_NAME, String.valueOf(getMethod()));
		}
		if (getApiKey() != null) {
			parameters.put(API_KEY_PARAMETER_NAME, getApiKey());
		}
		if (getBody() != null) {
			parameters.put(BODY_PARAMETER_NAME, getBody());
		}
		if (getApiRequestSignature() != null) {
			parameters.put(API_REQUEST_SIGNATURE_PARAMETER_NAME, getApiRequestSignature());
		}
		if (getApiRequestSignatureVersion() != null) {
			parameters.put(API_REQUEST_SIGNATURE_VERSION_PARAMETER_NAME, getApiRequestSignatureVersion());
		}
		if (getTimestamp() != null) {
			parameters.put(TIMESTAMP_PARAMETER_NAME, getTimestamp());
		}

		return parameters;
	}

}
