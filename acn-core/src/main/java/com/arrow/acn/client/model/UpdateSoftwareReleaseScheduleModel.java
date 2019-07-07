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

public class UpdateSoftwareReleaseScheduleModel extends CreateSoftwareReleaseScheduleModel {

	private static final long serialVersionUID = 2775885846842752494L;

	private SoftwareReleaseScheduleStatus status;

	public UpdateSoftwareReleaseScheduleModel withStatus(SoftwareReleaseScheduleStatus status) {
		setStatus(status);
		return this;
	}

	public void setStatus(SoftwareReleaseScheduleStatus status) {
		this.status = status;
	}

	public SoftwareReleaseScheduleStatus getStatus() {
		return status;
	}
}
