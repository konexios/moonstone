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

public class AcsException extends Exception {
    private static final long serialVersionUID = -3766369471827870800L;

    public AcsException(String message) {
        super(message);
    }

    public AcsException(String message, Throwable cause) {
        super(message, cause);
    }
}
