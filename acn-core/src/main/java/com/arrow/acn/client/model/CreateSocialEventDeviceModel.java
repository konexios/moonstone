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

public class CreateSocialEventDeviceModel implements Serializable {
	private static final long serialVersionUID = -2251195930691371830L;

	private String pinCode;
	private String macAddress;
	private String deviceTypeHid;

	public CreateSocialEventDeviceModel withPinCode(String pinCode) {
		setPinCode(pinCode);
		return this;
	}

	public CreateSocialEventDeviceModel withMacAddress(String macAddress) {
		setMacAddress(macAddress);
		return this;
	}

	public CreateSocialEventDeviceModel withDeviceTypeHid(String deviceTypeHid) {
		setDeviceTypeHid(deviceTypeHid);
		return this;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDeviceTypeHid() {
		return deviceTypeHid;
	}

	public void setDeviceTypeHid(String deviceTypeHid) {
		this.deviceTypeHid = deviceTypeHid;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceTypeHid == null) ? 0 : deviceTypeHid.hashCode());
		result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
		result = prime * result + ((pinCode == null) ? 0 : pinCode.hashCode());
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
		CreateSocialEventDeviceModel other = (CreateSocialEventDeviceModel) obj;
		if (deviceTypeHid == null) {
			if (other.deviceTypeHid != null)
				return false;
		} else if (!deviceTypeHid.equals(other.deviceTypeHid))
			return false;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		if (pinCode == null) {
			if (other.pinCode != null)
				return false;
		} else if (!pinCode.equals(other.pinCode))
			return false;
		return true;
	}
}
