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
package com.arrow.acn.client.model;

import java.io.Serializable;

public class AzureConfigModel implements Serializable {
	private static final long serialVersionUID = 522892464697437578L;

	private String host;
	private String accessKey;

	public AzureConfigModel withHost(String host) {
		setHost(host);
		return this;
	}

	public AzureConfigModel withAccessKey(String accessKey) {
		setAccessKey(accessKey);
		return this;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessKey == null) ? 0 : accessKey.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
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
		AzureConfigModel other = (AzureConfigModel) obj;
		if (accessKey == null) {
			if (other.accessKey != null)
				return false;
		} else if (!accessKey.equals(other.accessKey))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		return true;
	}
}
