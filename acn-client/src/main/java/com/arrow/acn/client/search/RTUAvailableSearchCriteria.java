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
package com.arrow.acn.client.search;

import com.arrow.acs.client.search.SearchCriteria;

public class RTUAvailableSearchCriteria extends SearchCriteria {

	private static final String DEVICE_TYPE_HID = "deviceTypeHid";

	public RTUAvailableSearchCriteria withDeviceTypeHid(final String deviceTypeHid) {
		simpleCriteria.put(DEVICE_TYPE_HID, String.valueOf(deviceTypeHid));
		return this;
	}
	
	public void setDeviceTypeHid(final String deviceTypeHid) {
		simpleCriteria.put(DEVICE_TYPE_HID, String.valueOf(deviceTypeHid));
	}
}
