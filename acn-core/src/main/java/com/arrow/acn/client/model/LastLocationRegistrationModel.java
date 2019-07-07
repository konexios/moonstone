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

public class LastLocationRegistrationModel implements Serializable {
	private static final long serialVersionUID = 1912437041926487256L;

	private Double latitude;
	private Double longitude;
	private String locationType;

	public LastLocationRegistrationModel withLatitude(Double latitude) {
		setLatitude(latitude);
		return this;
	}

	public LastLocationRegistrationModel withLongitude(Double longitude) {
		setLongitude(longitude);
		return this;
	}

	public LastLocationRegistrationModel withLocationType(String locationType) {
		setLocationType(locationType);
		return this;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
}
