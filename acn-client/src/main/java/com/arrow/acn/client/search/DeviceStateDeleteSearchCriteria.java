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

public class DeviceStateDeleteSearchCriteria extends SearchCriteria {

	private static final String REMOVE_DEVICE_STATE_DEFINITION = "removeStatesDefinition";

	public DeviceStateDeleteSearchCriteria withRemoveDefinitions(final boolean removeStatesDefinition) {
		simpleCriteria.put(REMOVE_DEVICE_STATE_DEFINITION, String.valueOf(removeStatesDefinition));
		return this;
	}
}
