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

public class AccessKeyModel implements Serializable {
	private static final long serialVersionUID = 2009328107616383251L;

	private String pri;
	private String apiKey;
	private String secretKey;
	private String className;

	public AccessKeyModel withPri(String pri) {
		setPri(pri);
		return this;
	}

	public AccessKeyModel withApiKey(String apiKey) {
		setApiKey(apiKey);
		return this;
	}

	public AccessKeyModel withSecretKey(String secretKey) {
		setSecretKey(secretKey);
		return this;
	}

	public AccessKeyModel withClassName(String className) {
		setClassName(className);
		return this;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public String getPri() {
		return pri;
	}

	public void setPri(String pri) {
		this.pri = pri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((pri == null) ? 0 : pri.hashCode());
		result = prime * result + ((secretKey == null) ? 0 : secretKey.hashCode());
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
		AccessKeyModel other = (AccessKeyModel) obj;
		if (apiKey == null) {
			if (other.apiKey != null)
				return false;
		} else if (!apiKey.equals(other.apiKey))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (pri == null) {
			if (other.pri != null)
				return false;
		} else if (!pri.equals(other.pri))
			return false;
		if (secretKey == null) {
			if (other.secretKey != null)
				return false;
		} else if (!secretKey.equals(other.secretKey))
			return false;
		return true;
	}
}
