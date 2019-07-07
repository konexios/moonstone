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

import com.arrow.acs.client.search.SearchCriteria;

public class CloudMqttRequestParams implements Serializable {
	private static final long serialVersionUID = -7465507284897070170L;

	private String requestId;
	private CloudRequestMethodName httpMethod;
	private String url;
	private String jsonBody;
	private SearchCriteria criteria;
	private boolean encrypted;

	public CloudMqttRequestParams withRequestId(String requestId) {
		setRequestId(requestId);
		return this;
	}

	public CloudMqttRequestParams withHttpMethod(CloudRequestMethodName httpMethod) {
		setHttpMethod(httpMethod);
		return this;
	}

	public CloudMqttRequestParams withUrl(String url) {
		setUrl(url);
		return this;
	}

	public CloudMqttRequestParams withJsonBody(String jsonBody) {
		setJsonBody(jsonBody);
		return this;
	}

	public CloudMqttRequestParams withCriteria(SearchCriteria criteria) {
		setCriteria(criteria);
		return this;
	}

	public CloudMqttRequestParams withEncrypted(boolean encrypted) {
		setEncrypted(encrypted);
		return this;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public CloudRequestMethodName getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(CloudRequestMethodName httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}

	public SearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criteria == null) ? 0 : criteria.hashCode());
		result = prime * result + (encrypted ? 1231 : 1237);
		result = prime * result + ((httpMethod == null) ? 0 : httpMethod.hashCode());
		result = prime * result + ((jsonBody == null) ? 0 : jsonBody.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CloudMqttRequestParams other = (CloudMqttRequestParams) obj;
		if (criteria == null) {
			if (other.criteria != null)
				return false;
		} else if (!criteria.equals(other.criteria))
			return false;
		if (encrypted != other.encrypted)
			return false;
		if (httpMethod != other.httpMethod)
			return false;
		if (jsonBody == null) {
			if (other.jsonBody != null)
				return false;
		} else if (!jsonBody.equals(other.jsonBody))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
