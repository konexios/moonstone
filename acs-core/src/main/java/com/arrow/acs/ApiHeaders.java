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
package com.arrow.acs;

public interface ApiHeaders {
	String X_ARROW_VERSION = "x-arrow-version";
	String X_ARROW_APIKEY = "x-arrow-apikey";
	String X_ARROW_DATE = "x-arrow-date";
	String X_ARROW_SIGNATURE = "x-arrow-signature";

	String X_ARROW_VERSION_1 = "1";
	String X_ARROW_VERSION_2 = "2";

	// backward compatible
	String X_AUTH_TOKEN = "x-auth-token";
}
