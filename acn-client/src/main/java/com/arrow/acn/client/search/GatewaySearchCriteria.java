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

import java.time.Instant;

import com.arrow.acs.client.search.SearchCriteria;

public class GatewaySearchCriteria extends SearchCriteria {
	private static final String UIDS = "uids";
	private static final String USER_IDS = "userIds";
	private static final String TYPES = "types";
	private static final String DEVICE_TYPES = "deviceTypes";
	private static final String NODE_HIDS = "nodeHids";
	private static final String OS_NAMES = "osNames";
	private static final String SOFTWARE_NAMES = "softwareNames";
	private static final String SOFTWARE_VERSIONS = "softwareVersions";
	private static final String ENABLED = "enabled";
	private static final String CREATED_BEFORE = "createdBefore";
	private static final String CREATED_AFTER = "createdAfter";
	private static final String UPDATED_BEFORE = "updatedBefore";
	private static final String UPDATED_AFTER = "updatedAfter";
	private PageSearchCriteria pageSearchCriteria = new PageSearchCriteria();

	public GatewaySearchCriteria withUids(String... uids) {
		arrayCriteria.put(UIDS, uids);
		return this;
	}

	public GatewaySearchCriteria withUserIds(String... userIds) {
		arrayCriteria.put(USER_IDS, userIds);
		return this;
	}

	public GatewaySearchCriteria withTypes(String... types) {
		arrayCriteria.put(TYPES, types);
		return this;
	}

	public GatewaySearchCriteria withDeviceTypes(String... deviceTypes) {
		arrayCriteria.put(DEVICE_TYPES, deviceTypes);
		return this;
	}

	public GatewaySearchCriteria withNodeHids(String... nodeHids) {
		arrayCriteria.put(NODE_HIDS, nodeHids);
		return this;
	}

	public GatewaySearchCriteria withOsNames(String... osNames) {
		arrayCriteria.put(OS_NAMES, osNames);
		return this;
	}

	public GatewaySearchCriteria withSoftwareNames(String... softwareNames) {
		arrayCriteria.put(SOFTWARE_NAMES, softwareNames);
		return this;
	}

	public GatewaySearchCriteria withSoftwareVersions(String... softwareVersions) {
		arrayCriteria.put(SOFTWARE_VERSIONS, softwareVersions);
		return this;
	}

	public GatewaySearchCriteria withEnabled(boolean enabled) {
		simpleCriteria.put(ENABLED, Boolean.toString(enabled));
		return this;
	}

	public GatewaySearchCriteria withCreatedBefore(Instant createdBefore) {
		simpleCriteria.put(CREATED_BEFORE, createdBefore.toString());
		return this;
	}

	public GatewaySearchCriteria withCreatedAfter(Instant createdAfter) {
		simpleCriteria.put(CREATED_AFTER, createdAfter.toString());
		return this;
	}

	public GatewaySearchCriteria withUpdatedBefore(Instant updatedBefore) {
		simpleCriteria.put(UPDATED_BEFORE, updatedBefore.toString());
		return this;
	}

	public GatewaySearchCriteria withUpdatedAfter(Instant updatedAfter) {
		simpleCriteria.put(UPDATED_AFTER, updatedAfter.toString());
		return this;
	}

	public GatewaySearchCriteria withPage(long page) {
		pageSearchCriteria.withPage(page);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}

	public GatewaySearchCriteria withSize(long size) {
		pageSearchCriteria.withSize(size);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}
}
