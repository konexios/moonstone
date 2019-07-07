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

public class AcsRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 8444679053010167231L;

    public AcsRuntimeException(String message) {
        super(message);
    }

    public AcsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
