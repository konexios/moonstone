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

public class GatewayEventModel implements Serializable {
	private static final long serialVersionUID = -5307084765752243506L;

	private String hid;
	private String name;
	private boolean encrypted = false;
	private Map<String, String> parameters = new LinkedHashMap<>();
	private String signature;
	private String signatureVersion;

	public GatewayEventModel withHid(String hid) {
		setHid(hid);
		return this;
	}

	public GatewayEventModel withName(String name) {
		setName(name);
		return this;
	}

	public GatewayEventModel withEncrypted(boolean encrypted) {
		setEncrypted(encrypted);
		return this;
	}

	public GatewayEventModel withParameters(Map<String, String> parameters) {
		setParameters(parameters);
		return this;
	}

	public GatewayEventModel withSignature(String signature) {
		setSignature(signature);
		return this;
	}

	public GatewayEventModel withSignatureVersion(String signatureVersion) {
		setSignatureVersion(signatureVersion);
		return this;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureVersion() {
		return signatureVersion;
	}

	public void setSignatureVersion(String signatureVersion) {
		this.signatureVersion = signatureVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (encrypted ? 1231 : 1237);
		result = prime * result + ((hid == null) ? 0 : hid.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
		result = prime * result + ((signatureVersion == null) ? 0 : signatureVersion.hashCode());
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
		GatewayEventModel other = (GatewayEventModel) obj;
		if (encrypted != other.encrypted)
			return false;
		if (hid == null) {
			if (other.hid != null)
				return false;
		} else if (!hid.equals(other.hid))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature)) 
			return false;
		if (signatureVersion == null) {
			if (other.signatureVersion != null)
				return false;
		} else if (!signatureVersion.equals(other.signatureVersion)) 
			return false;
		return true;
	}
}
