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
import java.util.Arrays;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.SoftwareReleaseScheduleStatus;
import com.arrow.acs.client.search.SearchCriteria;

public class SoftwareReleaseScheduleSearchCriteria extends SearchCriteria {

	private static final String FROM_SCHEDULED_DATE = "fromScheduledDate";
	private static final String TO_SCHEDULED_DATE = "toScheduledDate";
	private static final String SOFTWARE_RELEASE_HIDS = "softwareReleaseHids";
	private static final String DEVICE_CATEGORY = "deviceCategory";
	private static final String OBJECT_HIDS = "objectHids";
	private static final String STATUSES = "statuses";
	private static final String NOTIFY_ON_START = "notifyOnStart";
	private static final String NOTIFY_ON_END = "notifyOnEnd";

	private PageSearchCriteria pageSearchCriteria = new PageSearchCriteria();

	public SoftwareReleaseScheduleSearchCriteria withFromScheduledDate(Instant fromScheduledDate) {
		simpleCriteria.put(FROM_SCHEDULED_DATE, fromScheduledDate.toString());
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withToScheduledDate(Instant toScheduledDate) {
		simpleCriteria.put(TO_SCHEDULED_DATE, toScheduledDate.toString());
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withSoftwareReleaseHids(String... softwareReleaseHids) {
		arrayCriteria.put(SOFTWARE_RELEASE_HIDS, softwareReleaseHids);
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withDeviceCategory(AcnDeviceCategory deviceCategory) {
		simpleCriteria.put(DEVICE_CATEGORY, String.valueOf(deviceCategory));
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withObjectHids(String... objectHids) {
		arrayCriteria.put(OBJECT_HIDS, objectHids);
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withStatuses(SoftwareReleaseScheduleStatus... statuses) {
		if (statuses != null) {
			arrayCriteria.put(STATUSES,
			        Arrays.stream(statuses).map(Object::toString).toArray(String[]::new));
		}
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withNotifyOnStart(boolean notifyOnStart) {
		simpleCriteria.put(NOTIFY_ON_START, String.valueOf(notifyOnStart));
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withNotifyOnEnd(boolean notifyOnEnd) {
		simpleCriteria.put(NOTIFY_ON_END, String.valueOf(notifyOnEnd));
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withPage(long page) {
		pageSearchCriteria.withPage(page);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}

	public SoftwareReleaseScheduleSearchCriteria withSize(long size) {
		pageSearchCriteria.withSize(size);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}
}
