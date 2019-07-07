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
package com.arrow.acn.client;

public interface ClientConstants {
    long DEFAULT_CLOUD_CONNECTION_RETRY_INTERVAL_MS = 10000L;
    long DEFAULT_CLOUD_SENDING_RETRY_MS = 5000L;

    long DEFAULT_SHUTDOWN_WAITING_MS = 10000L;

    interface Mqtt {
        int DEFAULT_CONNECTION_TIMEOUT_SECS = 60;
        int DEFAULT_KEEP_ALIVE_INTERVAL_SECS = 60;
        long DEFAULT_CHECK_CONNECTION_RETRY_INTERVAL_MS = 5000L;
        long DEFAULT_PAUSE_BEFORE_RECONNECT_MS = 2000L;
    }
}
