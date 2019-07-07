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

import com.arrow.acs.client.model.ModelAbstract;

public class LastLocationModel extends ModelAbstract<LastLocationModel> {
	private static final long serialVersionUID = 1431304627786033124L;

	private String objectType;
	private String objectHid;
	private Double latitude;
	private Double longitude;
	private String locationType;
	private String timestamp;

	@Override
	protected LastLocationModel self() {
		return this;
	}

	public LastLocationModel withObjectType(String objectType) {
		setObjectType(objectType);
		return this;
	}

	public LastLocationModel withObjectHid(String objectHid) {
		setObjectHid(objectHid);
		return this;
	}

	public LastLocationModel withLatitude(Double latitude) {
		setLatitude(latitude);
		return this;
	}

	public LastLocationModel withLongitude(Double longitude) {
		setLongitude(longitude);
		return this;
	}

	public LastLocationModel withLocationType(String locationType) {
		setLocationType(locationType);
		return this;
	}

	public LastLocationModel withTimestamp(String timestamp) {
		setTimestamp(timestamp);
		return this;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectHid() {
		return objectHid;
	}

	public void setObjectHid(String objectHid) {
		this.objectHid = objectHid;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
